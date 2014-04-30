package game2048.controller;

import java.util.Observable;

import game2048.model.Game2048Model;
import game2048.model.Model;
import game2048.view.Game2048View;
import game2048.view.View;

public class Run {

	public static void main(String[] args) {
		Model model2048				= new Game2048Model();	
		View view2048				= new Game2048View(model2048.getBoard());
		System.out.println("game2048 model&view initialized!");
		Presenter presenter 		= new Presenter(model2048, view2048);		
		((Observable) view2048).addObserver(presenter);
		((Observable) model2048).addObserver(presenter);

		Thread viewThread = new Thread((Game2048View) view2048);
		viewThread.start();
	}

}
