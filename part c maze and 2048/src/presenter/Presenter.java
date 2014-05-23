package presenter;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;



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
			view.displayData(model.getData());
			view.displayScore(model.getScore());
			view.displayHint(model.getHint());
			
			if(model.isSucceed()){
				view.gameOver(true);
			}
			if(model.isStuck()){
				view.gameOver(false);
			}
			
		}
		if (o == view){
			if(arg1 == "save"){
				model.setFileName(view.getFileNamePath());
			}
			if(arg1 == "load"){
				model.setFileName(view.getFileNamePath());
			}
			if(arg1 == "solve"){
				model.setDepth(view.getDepth());
				model.setHintsNum(view.getHintNum());
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
