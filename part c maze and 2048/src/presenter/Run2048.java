package presenter;

import java.util.Observable;

import minimax.Board;
import model.Game2048Model;
import model.Model;
import view.View;
import view.game2048.Game2048View;
/**
 * 
 * This is a main for the game 2048.
 * We use the MVP - model, view , presenter design.
 * It means that Model and View don't know each other.
 * The Model contains the logic of the game and the View contains the view of the game.
 * When something changed in Model he tells presenter and it tells to View.
 * The same for View.
 * 
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public class Run2048 {
	
	public static void main(String[] args) {
		Model model2048				= new Game2048Model();	
		View view2048				= new Game2048View(model2048.getData());
		System.out.println("game2048 model&view initialized!");
		Presenter presenter 		= new Presenter(model2048, view2048);		
		((Observable) view2048).addObserver(presenter);
		((Observable) model2048).addObserver(presenter);
		System.out.println("observers initialized");

		Thread viewThread = new Thread((Game2048View) view2048);
		viewThread.start();
		System.out.println("Game 2048 started.");		
			
	}
}
