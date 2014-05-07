package gameMaze.controller;

import gameMaze.model.Model;
import gameMaze.view.View;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
			view.displayData(model.getMaze());
			view.displayScore(model.getScore());
			if(model.isSucceed()){
				view.gameOver(true);
			}
		}
		if (o == view){
			if(arg1 == "save"){
				model.setFileName(view.getFileNamePath());
			}
			if(arg1 == "load"){
				model.setFileName(view.getFileNamePath());
			}
			
			model.doUserCommand(view.getUserCommand());		
		}	
	}
	
	public Model getModel() {
		return model;
	}

	public View getView() {
		return view;
	}

}
