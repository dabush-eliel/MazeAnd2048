package gameMaze.controller;

import gameMaze.model.Model;
import gameMaze.view.View;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import com.thoughtworks.xstream.XStream;



public class Presenter implements Observer,Runnable, Serializable{
	
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
				XStream xstream = new XStream();
				String modelXML = xstream.toXML(model);
				String viewXML = xstream.toXML(view);
				
				System.out.println(modelXML);
				System.out.println(viewXML);
			}
			if(arg1 == "load"){
				
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


	@Override
	public void run() {
//		view.initView();
//		view.run();
	}
	
}
