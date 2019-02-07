import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;

import sun.rmi.transport.Channel;
import sun.rmi.transport.Connection;

public class Client implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    
    private static Instant instant = null;
	private static InetAddress IP = null;

    
    public Client() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }
    
	
	public static String getIP() { 
	      return IP.toString(); 
	   } 
	
	public static String getDate() { 
	      return instant.toString(); 
	   } 
	

    public static void main(String[] argv) {
    	
    	IP=InetAddress.getLocalHost();
        instant = Instant.now();
       
		System.out.println("Client " + IP + " sending requests to server - " + instant);
		System.out.println("=======================================================\n");
		
        try (Client rabbitmqRpc = new Client()) {
            for (int i = 0; i < 10; i++) {
                String request;

                request = "getCounter";
                request = "incCounter";
                request = "decrement";
                
                System.out.println(" [x] Requesting " + request);
                String response = rabbitmqRpc.call(request);
                System.out.println(" [.] Response '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}
