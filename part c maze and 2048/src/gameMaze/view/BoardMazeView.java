package gameMaze.view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BoardMazeView extends Canvas {
	
	int[][] mazeData;
	int maxX;
	int maxY;

	public BoardMazeView(Composite parent, int style, int rows, int columns) {
		super(parent, style);
		mazeData =  new int[rows][columns];
		
		getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				addPaintListener(new PaintListener() {
					
					@Override
					public void paintControl(PaintEvent e) {
						BoardMazeView maze = (BoardMazeView) e.widget;
						maxX = maze.getSize().x;
						maxY = maze.getSize().y;
						
					}
				});
			}
		});
	}
	
	public void setMazeData(int[][] data) {
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				mazeData[i][j] = data[i][j];
			}
		}
	}

}
