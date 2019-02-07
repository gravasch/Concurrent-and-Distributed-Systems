import java.net.InetAddress;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.time.Instant;

public class Client {
	
	private static final String HOST = "localhost";
	private static int PORT = Registry.REGISTRY_PORT;
	
	
	private static Instant instant = null;
	private static InetAddress IP = null;
	
	public static String getIP() { 
	      return IP.toString(); 
	   } 
	
	public static String getDate() { 
	      return instant.toString(); 
	   } 
	
		
   public static void main(String[] args)throws Exception {  
      try { 
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry(HOST, PORT); 
         
         // Looking up the registry for the remote object 
         Interface stub = (Interface) registry.lookup("Counter"); 
         String rmiObjName = "Counter";
         //Calling the remote method using the obtained object 
         
         System.out.println("Client " + IP + " sending info to server - " + instant);
         System.out.println("=======================================================\n");
         
           
         System.out.println("Counter Values:");
         for (int i=0; i<6; i++) {
            stub.incCounter();
            System.out.println("Counter Value " + stub.getCounter() + " and sleep for 2sec");
    		Thread.sleep(2000);
    		System.out.println(stub.getCounter());
    		stub.decCounter();
    		System.out.println("Counter Value " + stub.getCounter() + " and sleep for 2sec");
    		Thread.sleep(2000);
    		System.out.println(stub.getCounter());
 
         }  

      } catch (Exception e) { 
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
}