import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;  

// Implementing the remote interface 
public class Implementation extends UnicastRemoteObject implements Interface {  
	
	public Implementation() throws RemoteException{
		super();
	}
	
	private static int Counter = 0;
    
    //methods for increment, decrement and get value of Counter
	  public synchronized void incCounter() {	Counter++;}
	
	  public synchronized void decCounter() {	Counter--;	}
		
	  public synchronized int getCounter() {	return Counter;	}
	
	  
	  public void run() throws SQLException, ClassNotFoundException {
         
            //creating SQLite Base
          		Class.forName("org.sqlite.JDBC");
      			String url = "jdbc:sqlite:test.db";
      			Connection con = DriverManager.getConnection(url);
      			Statement st = con.createStatement();
      			
      			//Creating table
      			String sql = "CREATE TABLE IF NOT EXISTS Client (\n"
      	                + "	Date Text ,\n"
      	                + "	IP Text ,\n"
      	                + " Increment Int,\n"
      	                + " Decrement Int \n"
      	                + ");";
      			st.execute(sql);
      			
      			
  				String query = "select * from Client";
  				ResultSet rs = st.executeQuery(query);
  			    
  				//saving client's activity in the database
  				System.out.println("Date:" + Client.getDate() + "\n" + "Client: " + Client.getIP());
  				for (int i=0; i<6; i++) {
  					incCounter();
  					int s1 = getCounter();
  					System.out.println("---------------------------------------");
                  	st.execute("insert into `Client`(Date,IP,Increment,Decrement) VALUES ('"+Client.getDate()+"','"+Client.getIP()+"', '"+s1+"')");
                  	decCounter();
  					s1 = getCounter();
  					st.execute("insert into `Client`(Date,IP,Increment,Decrement) VALUES ('"+Client.getDate()+"','"+Client.getIP()+"', '"+s1+"')");
                  	System.out.println("---------------------------------------");
              	}

  				rs.close();
      		             
              System.out.println("\nConnection with client closed.");
              System.out.println("End of session. Bye!");
          }
      
      
         
   }  
