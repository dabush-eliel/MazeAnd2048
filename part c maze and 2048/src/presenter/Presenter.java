package presenter;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

/**
 * Presenter class is a connection between Model and View that 
 * implements Observer for the Model and View could notify him with changes.
 * 
 * 
 * @author Eliel Dabush and Oleg Glizerin
 * 
 */

public class Presenter implements Observer{
	
	Model model;
	View view;
	
	/**
	 * The constructor init from the main.
	 * @param model Holds a model (logic, actions and etc) of the game.
	 * @param view Hold a view (gui, pictures, buttons and etc) of the game.
	 */
	
	public Presenter(Model model, View view){
		this.model	 = model;
		this.view 	 = view;
	}
	
	/**
	 * @return When some one observarable and notify the observer,
	 * this method called and it knows who send the order and where to deliver it.
	 */
	@Override
	public void update(Observable o, Object arg1) {
		if (o == model){
			view.displayData(model.getData());
			view.displayScore(model.getScore());
			if(arg1 != null){
				view.displayHint(Integer.parseInt(arg1.toString()));
			}
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
			
			model.doUserCommand(view.getUserCommand());		
		}	
	}
	
	/**
	 * model of the game.
	 * @return model.
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * view of the game.
	 * @return view.
	 */
	public View getView() {
		return view;
	}

}
