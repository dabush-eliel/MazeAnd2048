package presenter;

import java.util.Observable;

import view.View;
import view.maze.MazeView;
import model.MazeModel;
import model.Model;

public class RunMaze {
	
	public static void main(String[] args) {
		Model modelMaze				= new MazeModel();	
		View viewMaze				= new MazeView(modelMaze.getData());
		System.out.println("model and view initialized");
		Presenter presenter 		= new Presenter(modelMaze, viewMaze);		
		((Observable) modelMaze).addObserver(presenter);
		((Observable) viewMaze).addObserver(presenter);

		Thread viewThread = new Thread((MazeView) viewMaze);
		viewThread.start();
		System.out.println("thread started");
	}	
}
