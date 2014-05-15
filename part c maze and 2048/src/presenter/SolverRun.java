package presenter;

import java.util.Observable;

import algorithms.Minimax;
import algorithms.MyAlgo;
import Network2048.Server;
import model.Game2048Model;
import model.Model;
import view.View;
import view.game2048.Game2048View;

public class SolverRun {

	public static void main(String[] args) {

		Model model2048				= new Game2048Model();	
		View view2048				= new Game2048View(model2048.getData());
		System.out.println("game2048 model&view initialized!");
		Presenter presenter 		= new Presenter(model2048, view2048);		
		((Observable) view2048).addObserver(presenter);
		((Observable) model2048).addObserver(presenter);
		System.out.println("observers initialized");
		
		Server server = new Server();
		
		Thread sThread = new Thread(server);
		sThread.start();
		System.out.println("server started.");	
		
		
		model2048.getAI(new Minimax());
	}

}
