package gameMaze.view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BoardMazeView extends Canvas {
	
	private int[][] mazeData;
	private int maxX,mx;
	private int maxY,my;
	private int xBm, yBm; // x and y before mouse movement
	private int x,y;	// position of the mouse
	private boolean drag = false;
	private boolean mouseMoved = false;
	


	//	final Image imgW = new Image(getDisplay(), "imagesMaze/brown_wall.jpg");
	final private Image imgM = new Image(getDisplay(),"imagesMaze/white_mouse.png");
	final private Image imgM2 = new Image(getDisplay(),"imagesMaze/cabel_mouse.jpg");
	final private Image imgM3 = new Image(getDisplay(),"imagesMaze/red_mouse.jpg");
	final private Image imgC = new Image(getDisplay(),"imagesMaze/usb.jpg");
	final private Image imgC2 = new Image(getDisplay(),"imagesMaze/usb_symb.png");
	final private Image imgG = new Image(getDisplay(),"imagesMaze/mouse_clipart.png");
	final private Image imgG2 = new Image(getDisplay(),"imagesMaze/red_curser.png");
	

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
						
						mx = maxX/rows;
						my = maxY/columns;

						
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
									if(!drag){
										x = mx;
										y = my;
										x = x * i;
										y = y * j;
									}
									ImageData idM =imgM3.getImageData().scaledTo(mx, my);									
									Image imgMnew = new Image(getDisplay(), idM);
									e.gc.drawImage(imgMnew, x,y);
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
				
				addMouseMoveListener(new MouseMoveListener() {
					
					@Override
					public void mouseMove(MouseEvent e) {
						if(drag){
							mouseMoved = true;
							// put the mouse img on the center of the curser
							x = e.x-(mx/2);
							y = e.y-(my/2);
							redraw();	
						}
					}
				});
				
				addMouseListener(new org.eclipse.swt.events.MouseListener() {
					
					@Override
					public void mouseUp(MouseEvent e) {						
						getDisplay().timerExec(100, new Runnable() {
							@Override
							public void run() {
								drag = false;
								mouseMoved = false;
								redraw();								
							}
						});
					}
					
					@Override
					public void mouseDown(MouseEvent e) {
						// save the mouse state b4 the move 
						System.out.println("e.x:"+e.x+" e.y:"+e.y);
						System.out.println("x = "+x+" y= "+y);
						// only if the click is down on the mouse img we will set drag = true
						if((e.x>x && e.x<x+mx) && (e.y>y && e.y<y+my)){
							xBm = x;
							yBm = y;
							drag = true;
						}
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						setFocus();
					}
				});;
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
	
	public int getMx() {
		return mx;
	}

	public int getMy() {
		return my;
	}

	public int getxBm() {
		return xBm;
	}

	public int getyBm() {
		return yBm;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isMouseMoved() {
		return mouseMoved;
	}


}
