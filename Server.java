import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
		
		public static void main(String[] args) throws Exception {
			
			System.out.println("Server is running.");
	        System.out.println("Waiting for connection....");
	       
	        ServerSocket server = new ServerSocket(9090);
	        Socket client = server.accept();
			Implementation obj = new Implementation(client, 0);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String IP = input.readLine();
			String date = input.readLine();
			
			obj.run(IP, date);
			
	        }
	    }

