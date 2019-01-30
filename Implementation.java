/** Created by Christoforos Gravas - 26/1/2019
         * Services this thread's client by first sending the client a welcome
         * message then client sends his IP and connection date/time to server.
         * After this client increments and after 2" decrements a public counter by 1
         * and server save in a database clients activity.
**/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;


public class Implementation extends Thread {
        
	private Socket socket;
   	private static int Counter = 0;

   	
   	public Implementation(Socket socket, int Counter) {
            this.socket = socket;
            System.out.println("Server Message: New client connected.");
        }

    
    	//methods for increment, decrement and get value of Counter
	    public synchronized static void incCounter() {
			Counter++;				
		}
	
		public synchronized static void decCounter() {
			Counter--;
		}
		
		public synchronized static int getCounter() {
			return Counter;
		}
		
		//gets IP and connection date from client & creates the SqLite database 
        public void run(String IP , String date) {
                    	
            try {
            	BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            	
                
              //creating SQLite Base
            	try {
            		Class.forName("org.sqlite.JDBC");
        			String url = "jdbc:sqlite:test.db";
        			Connection con = DriverManager.getConnection(url);
        			Statement st = con.createStatement();
        			
        			//Creating table
        			String sql = "CREATE TABLE IF NOT EXISTS Client (\n"
        	                + "	Date Text ,\n"
        	                + "	IP Text ,\n"
        	                + " Increment Text,\n"
        	                + " Decrement Text \n"
        	                + ");";
        			st.execute(sql);
        			
        			
    				String query = "select * from Client";
    				ResultSet rs = st.executeQuery(query);
    			    
    				//saving client's activity in the database
    				System.out.println("Date:" + date + "\n" + "Client: " + IP );
    				for (int i=0; i<6; i++) {
    					String s1 = input.readLine();
    					String s2 = input.readLine();
                    	st.execute("insert into `Client`(Date,IP,Increment,Decrement) VALUES ('"+date+"','"+IP+"', '"+s1+"', '"+s2+"')");
                    	System.out.println("---------------------------------------");
                    	System.out.println(s1 + " \n" + s2);
                    	System.out.println("---------------------------------------");
                	}

    				rs.close();
    				st.close();
    				con.close();
        		} catch(Exception e) {
        			System.out.println(e);
        		}
            } catch (IOException e) {
                System.out.println("Error handling client");
                System.out.println("End of session");
            } finally {
                try { socket.close(); } catch (IOException e) {}
               
                System.out.println("\nConnection with client closed.");
                System.out.println("End of session. Bye!");
            }
        }
	}
 
