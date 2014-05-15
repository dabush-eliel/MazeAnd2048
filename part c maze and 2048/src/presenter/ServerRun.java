package presenter;

import java.util.Observable;

import model.Game2048Model;
import model.Model;
import view.View;
import view.game2048.Game2048View;
import Network2048.GameHandler;
import Network2048.Server;
import algorithms.MyAlgo;

public class ServerRun {


	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
/*
		Model model2048				= new Game2048Model();	
		View view2048				= new Game2048View(model2048.getData());
		System.out.println("game2048 model&view initialized!");
		Presenter presenter 		= new Presenter(model2048, view2048);		
		((Observable) view2048).addObserver(presenter);
		((Observable) model2048).addObserver(presenter);
		System.out.println("observers initialized");
		
*/		Server server = new Server(2022, 1, new GameHandler());
		
		Thread sThread = new Thread(server);
		sThread.start();	
		
//		model2048.getAI("localhost",2022,new MyAlgo());
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.close();		
		
	}

}
