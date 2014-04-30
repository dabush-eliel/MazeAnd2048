package gameMaze.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BoardMazeView extends Canvas {
	
	int[][] mazeData;
	int maxX;
	int maxY;

	public BoardMazeView(Composite parent, int style, final int rows,final int columns) {
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
						
						int mx = maxX/rows, my = maxY/columns;
						mx -= 1;
						my -= 1;
						
						for(int j=0; j<rows;j++){
							for(int i=0;i<columns;i++){
								switch (mazeData[i][j]) {
								case -1:
									// e.gc.drawRectangle(new Rectangle(i*mx, j*my, mx, my));
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								case 0:
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								case 1:
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								case 2:
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_YELLOW)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								//e.gc.fillRectangle(i*mx, j*my, mx, my);
								case 3:
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;	
								
								default:
									break;
								}
								if((i!=0)){
									e.gc.drawLine(i*mx,0,i*mx,my*columns);
								}
							}
							if(j != 0){
								e.gc.drawLine(0,j*my,mx*rows,j*my);
							}
						}
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
