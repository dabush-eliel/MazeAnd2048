package presenter;

import Network2048.GameHandler;
import Network2048.Server;

public class ServerRun {

	public static void main(String[] args) {
		Server server = new Server(2022, 1, new GameHandler());		
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
