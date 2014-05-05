package game2048.controller;

import game2048.model.Model;
import game2048.view.View;

import java.util.Observable;
import java.util.Observer;



public class Presenter implements Observer{
	
	Model model;
	View view;
	
	public Presenter(Model model, View view){
		this.model	 = model;
		this.view 	 = view;
	}
	
	@Override
	public void update(Observable o, Object arg1) {
		if (o == model){
			if(model.isStuck()){
				view.gameOver(false);
			}
			if(model.isSucceed()){
				view.gameOver(true);
			}
			view.displayBoard(model.getBoard());
			view.displayScore(model.getScore());
		}
		if (o == view && arg1 == null){
			model.doUserCommand(view.getUserCommand());		
		}	
		
		if(o == view && arg1 == "save"){
			model.setFileNameToSave(view.getFileNamePath());
			model.doUserCommand(view.getUserCommand());
		}
		
		if(o == view && arg1 == "load"){
			model.setFileNameToLoad(view.getFileNamePath());
			model.doUserCommand(view.getUserCommand());
			model.undoMove();
		}
	}
	
	public Model getModel() {
		return model;
	}

	public View getView() {
		return view;
	}
}
