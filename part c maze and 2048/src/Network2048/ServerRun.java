package Network2048;


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
		Server server = new Server(2032, 1, new GameHandler());		
		Thread sThread = new Thread(server);
		sThread.start();	
		
		try {
			Thread.sleep(480000);
		} catch (InterruptedException e) {
			System.out.println("INTERUPTED EXCEPTION, Nevermore13");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
		server.close();		
		
	}

}
