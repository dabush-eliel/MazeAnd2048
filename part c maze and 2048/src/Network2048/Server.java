package Network2048;

import java.util.Observable;

import old_mvp.Model;
import old_mvp.Presenter;
import old_mvp.View;
import view.game2048.Game2048View;
import model.Game2048Model;
import algorithms.MyAlgo;
import algorithms.Solver;

public class Server {

	
	public void startGame(){
		String gameType = "2048";
		switch (gameType) {
		case "2048":
			Model model2048	= new Game2048Model();	
			View view2048 = new Game2048View(model2048.getData());
			System.out.println("game2048 model&view initialized!");
			Presenter presenter 		= new Presenter(model2048, view2048);		
			((Observable) view2048).addObserver(presenter);
			((Observable) model2048).addObserver(presenter);
			System.out.println("observers initialized");
			
			Thread viewThread = new Thread((Game2048View) view2048);
			viewThread.start();
			System.out.println("Game 2048 started.");
			
			String solverStr = "MyAlgo";
			Solver solver = null;
			switch (solverStr) {
			case "MyAlgo":
				solver = new MyAlgo();
				solver.calculator(model2048);
				break;

			default:
				break;
			}
			break;
		case "maze":
			
			break;
		default:
			break;
		}
	}
}
