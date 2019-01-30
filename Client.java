import java.io.*;
import java.net.*;

import java.time.Instant;



public class Client {
	
	private static Instant instant = null;
	private static InetAddress IP = null;

	public static void main(String[] args) throws InterruptedException, IOException{
    	
    	//creating a server socket which listens to a certain socket
        Socket client = new Socket("localhost",9090);
        
        PrintStream output = new PrintStream(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
         
         
        IP=InetAddress.getLocalHost();
        instant = Instant.now();
       
		System.out.println("Client " + IP + " sending info to server - " + instant);
		System.out.println("=======================================================\n");
		
		output.println(IP.toString());
    	output.println(instant.toString());
    	
    	System.out.println("Counter Values:");
    	for (int i=0; i<6; i++) {
    		Implementation.incCounter();
    		output.println("Counter Value " + Implementation.getCounter() + " and sleep for 2sec");
    		Thread.sleep(2000);
    		System.out.println(Implementation.getCounter());
    		Implementation.decCounter();
    		output.println("Counter Value " + Implementation.getCounter() + " and sleep for 2sec");
    		Thread.sleep(2000);
    		System.out.println(Implementation.getCounter());
    	}
    	
    	System.out.println("Final Counter Value :" + Implementation.getCounter());
    	System.out.println("End of story.");
         
        output.close();
		input.close();
		client.close();
    }
}