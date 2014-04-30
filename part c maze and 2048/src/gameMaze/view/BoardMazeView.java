package gameMaze.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BoardMazeView extends Canvas {
	
	int[][] mazeData;
	int maxX;
	int maxY;

	public BoardMazeView(final Composite parent, int style, final int rows,final int columns) {
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
								/*	Image imgM = new Image(parent.getDisplay(),"imagesMaze/white_mouse.png");
									ImageData idM =imgM.getImageData().scaledTo(mx, my);									
									Image imgMnew = new Image(parent.getDisplay(), idM);
									e.gc.drawImage(imgMnew, i*mx, j*my);
								*/	e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								case 2:
								/*	Image imgC = new Image(parent.getDisplay(),"imagesMaze/usb.jpg");
									ImageData idC =imgC.getImageData().scaledTo(mx, my);									
									Image imgCnew = new Image(parent.getDisplay(), idC);
									e.gc.drawImage(imgCnew, i*mx, j*my);
								*/	e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_YELLOW)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								//e.gc.fillRectangle(i*mx, j*my, mx, my);
								case 3: 
								/*	Image imgG = new Image(parent.getDisplay(),"imagesMaze/mouse_clipart.png");
									ImageData idG =imgG.getImageData().scaledTo(mx, my);									
									Image imgGnew = new Image(parent.getDisplay(), idG);
									e.gc.drawImage(imgGnew, i*mx, j*my);
								*/	e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
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
