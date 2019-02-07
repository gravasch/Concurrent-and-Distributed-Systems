import com.rabbitmq.client.*;

import sun.rmi.transport.Connection;

public class Server {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int getCounter() {
        return Implementation.getCounter();
    }

    private static void incCounter() {
        Implementation.incCounter();
    }

    private static void decCounter() {
        Implementation.decCounter();
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";
                Integer value = 0;
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [.] " + message);

                    if (message.equals("getCounter")) {
                        value = getCounter();
                        response = value.toString();
                    } else if (message.equals("incCounter")) {
                        incCounter();
                        value = getCounter();
                        response = "increment: " + value.toString();
                    } else if (message.equals("decCounter")) {
                        decCounter();
                        value = getCounter();
                        response = "decrement: " + value.toString();
                    } else {
                        response = "Unknown action";
                    }
                    Implementation.run(value);
                    
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
