package gameMaze.controller;

import gameMaze.model.MazeModel;
import gameMaze.model.Model;
import gameMaze.view.View;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;



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
				model.setFileName(view.getFileNamePath());
		//		save();
			}
			if(arg1 == "load"){
				model.setFileName(view.getFileNamePath());
	//			loadGame();
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

	public void loadGame(){
	//	XStream xstream = new XStream();
	//	MazeModel mm = new MazeModel();
	//	mm = (MazeModel) xstream.fromXML(view.getFileNamePath());
	//	if(mm != null){
	//		model = mm;
	//	}
	}
	
	public void save() {
		// get file full path&name by fileName 
		//String fileName = view.getFileNamePath(); 
		File file = new File(view.getFileNamePath());
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file));
		//	XStream xstream = new XStream();
		//	out.write(xstream.toXML(model));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	@Override
	public void run() {
//		view.initView();
//		view.run();
	}
	
}
