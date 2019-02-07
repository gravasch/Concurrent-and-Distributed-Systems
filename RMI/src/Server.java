import java.rmi.*;
import java.rmi.registry.*;

public class Server {

	private static final int PORT = Registry.REGISTRY_PORT;

	public static void main(String[] args) throws Exception {

		Implementation rObj = new Implementation();
		Registry reg = LocateRegistry.createRegistry(PORT);
		String rmiObjName = "Counter";
		Naming.rebind(rmiObjName, rObj);
		System.out.println("Remote object bounded.");
	}

}
