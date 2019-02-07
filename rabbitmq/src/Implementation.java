/** Created by Christoforos Gravas - 26/1/2019
         * Services this thread's client by first sending the client a welcome
         * message then client sends his IP and connection date/time to server.
         * After this client increments and after 2" decrements a public counter by 1
         * and server save in a database clients activity.
**/

import java.io.IOException;
import java.sql.*;


public class Implementation extends Thread {
        
   	private static int Counter = 0;

    
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
        public static void run(Integer value) throws IOException {
                    	
            try {     	
            	
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
        	                + " Counter Value Int,\n"
        	                + ");";
        			st.execute(sql);
        			
        			
    				String query = "select * from Client";
    				ResultSet rs = st.executeQuery(query);
    			    
    				//saving client's activity in the database
    				System.out.println("Date:" + Client.getDate() + "\n" + "Client: " + Client.getIP() );
    				for (int i=0; i<6; i++) {
                    	st.execute("insert into `Client`(Date,IP,Increment,Decrement) VALUES ('"+Client.getDate()+"','"+Client.getIP()+"', '"+value+"')");
                    	System.out.println("---------------------------------------");
                	}

    				rs.close();
    				st.close();
    				con.close();
        		} catch(Exception e) {
        			System.out.println(e);
        		}
            } finally {
                              
                System.out.println("\nConnection with client closed.");
                System.out.println("End of session. Bye!");
            }
        }
	}
 
