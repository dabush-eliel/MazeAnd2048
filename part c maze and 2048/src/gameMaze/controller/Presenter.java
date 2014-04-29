package gameMaze.controller;

import gameMaze.model.Model;
import gameMaze.view.View;

import java.util.Observable;
import java.util.Observer;



public class Presenter implements Observer,Runnable{
	
	Model model;
	View view;
	
	public Presenter(Model model, View view){
		this.model	 = model;
		this.view 	 = view;
	}
	
	@Override
	public void update(Observable o, Object arg1) {
		if (o == model){
//			view.displayBoard(model.getBoard());
//			view.displayScore(model.getScore());
		}
		if (o == view){
			model.doUserCommand(view.getUserCommand());		
		}	
	}
	
	public Model getModel() {
		return model;
	}

	public View getView() {
		return view;
	}


	@Override
	public void run() {
//		view.initView();
//		view.run();
	}
	
}
