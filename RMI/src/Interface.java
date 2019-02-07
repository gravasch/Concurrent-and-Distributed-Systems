import java.rmi.Remote; 
import java.rmi.RemoteException;

// Creating Remote interface for our application 
public interface Interface extends Remote {  
	
	// Creating Remote interface for our application 
	   void incCounter() throws RemoteException;
	   void decCounter() throws RemoteException;	
	   int getCounter() throws RemoteException;
}