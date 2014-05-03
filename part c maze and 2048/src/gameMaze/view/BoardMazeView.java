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
//	final Image imgW = new Image(getDisplay(), "imagesMaze/brown_wall.jpg");
	final Image imgM = new Image(getDisplay(),"imagesMaze/white_mouse.png");
	final Image imgM2 = new Image(getDisplay(),"imagesMaze/cabel_mouse.jpg");
	final Image imgM3 = new Image(getDisplay(),"imagesMaze/red_mouse.jpg");
	final Image imgC = new Image(getDisplay(),"imagesMaze/usb.jpg");
	final Image imgC2 = new Image(getDisplay(),"imagesMaze/usb_symb.png");
	final Image imgG = new Image(getDisplay(),"imagesMaze/mouse_clipart.png");
	final Image imgG2 = new Image(getDisplay(),"imagesMaze/red_curser.png");
	

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
					//	mx -= 1;
					//	my -= 1;
						
						for(int j=0; j<rows;j++){
							for(int i=0;i<columns;i++){
								switch (mazeData[j][i]) {
								case -1:
									// e.gc.drawRectangle(new Rectangle(i*mx, j*my, mx, my));
							//		ImageData idW = imgW.getImageData().scaledTo(mx, my);
							//		Image imgWnew = new Image(getDisplay(), idW);
							//		e.gc.drawImage(imgWnew, i*mx, j*my);
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
							//		imgWnew.dispose();
									break;
								case 0:
									e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE)); 
									e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									break;
								case 1:									
									ImageData idM =imgM3.getImageData().scaledTo(mx, my);									
									Image imgMnew = new Image(getDisplay(), idM);
									e.gc.drawImage(imgMnew, i*mx, j*my);
							//		e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_RED)); 
							//		e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									imgMnew.dispose();
									break;
								case 2:	
									ImageData idC =imgC2.getImageData().scaledTo(mx, my);									
									Image imgCnew = new Image(getDisplay(), idC);
									e.gc.drawImage(imgCnew, i*mx, j*my);
								//	e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_YELLOW)); 
								//	e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									imgCnew.dispose();
									break;
								//e.gc.fillRectangle(i*mx, j*my, mx, my);
								case 3:									
									ImageData idG =imgG2.getImageData().scaledTo(mx, my);									
									Image imgGnew = new Image(getDisplay(), idG);
									e.gc.drawImage(imgGnew, i*mx, j*my);
								//	e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
								//	e.gc.fillRectangle(new Rectangle(i*mx, j*my, mx, my));
									imgGnew.dispose();
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
						e.gc.dispose();
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
