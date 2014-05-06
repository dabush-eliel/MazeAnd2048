package gameMaze.controller;

import gameMaze.controller.Presenter;
import gameMaze.model.MazeModel;
import gameMaze.model.Model;
import gameMaze.view.MazeView;
import gameMaze.view.View;
import java.util.Observable;

public class RunForTest {
	
	public static void main(String[] args) {
		Model modelMaze				= new MazeModel();	
		View viewMaze				= new MazeView(modelMaze.getMaze());
		System.out.println("model and view initialized");
		Presenter presenter 		= new Presenter(modelMaze, viewMaze);		
		((Observable) modelMaze).addObserver(presenter);
		((Observable) viewMaze).addObserver(presenter);

		Thread viewThread = new Thread((MazeView) viewMaze);
		viewThread.start();
		System.out.println("thread started");
	}
	
}
