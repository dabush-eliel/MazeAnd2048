package presenter;

import Network2048.GameHandler;
import Network2048.Server;


/**
 * The server main.
 * That wait to client to connect.
 * We use Thread pool for some client could connect in same time.
 * 
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public class ServerRun {

	public static void main(String[] args) {
		Server server = new Server(2022, 1, new GameHandler());		
		Thread sThread = new Thread(server);
		sThread.start();	
		
		try {
			Thread.sleep(5*60000);
		} catch (InterruptedException e) {
			System.out.println("INTERUPTED EXCEPTION, Nevermore13");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
		server.close();		
		System.exit(0);
		
	}

}
