package view.game2048;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is a Canvas on the gui, which shows a tiles with pictures above.
 * @author Eliel Dabush and Oleg Glizerin
 * 
 *
 */
public class BoardView extends Canvas {

	int [][] boardData;
//	int command = 0;
	int maxX;
	int maxY;
	
	final Image img0 = new Image(this.getDisplay(),"images2048/my_0.png");
	final Image img2 = new Image(this.getDisplay(), "images2048/my_2.png");
	final Image img4 = new Image(this.getDisplay(), "images2048/my_4.png");
	final Image img8 = new Image(this.getDisplay(), "images2048/my_8.png");
	final Image img16 = new Image(this.getDisplay(), "images2048/my_16.png");
	final Image img32 = new Image(this.getDisplay(), "images2048/my_32.png");
	final Image img64 = new Image(this.getDisplay(), "images2048/my_64.png");
	final Image img128 = new Image(this.getDisplay(), "images2048/my_128.png");
	final Image img256 = new Image(this.getDisplay(), "images2048/my_256.png");
	final Image img512 = new Image(this.getDisplay(), "images2048/my_512.png");
	final Image img1024 = new Image(this.getDisplay(), "images2048/my_1024.png");
	final Image img2048 = new Image(this.getDisplay(), "images2048/my_2048.png");
	final Image img4096 = new Image(this.getDisplay(),"images2048/my_4096.png");
	final Image img8192 = new Image(this.getDisplay(),"images2048/my_8192.png");
	
	
	
	/**
	 * 
	 * @param parent its a shell
	 * @param style int the represent a style of shell, we using SWT library to be accurate so it get SWT.BORDER .
	 * 
	 */

	public BoardView(Composite parent, int style) {
		super(parent, style);
		this.getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				addPaintListener(new PaintListener() {
					
					@Override
					public void paintControl(PaintEvent e) {
						BoardView board = (BoardView) e.widget;
						
						maxX = board.getSize().x;
						maxY = board.getSize().y;
			
						int sqrWidth 	= (maxX-25)/boardData.length;
						int sqrHeight 	= (maxY-25)/boardData[0].length;
						int min 		= Math.min(sqrWidth, sqrHeight);
						//drawing the pictures
						if(boardData != null){	
							for(int i=0;i<boardData.length;i++){
								for(int j=0;j<boardData[0].length;j++){
									switch(boardData[i][j]){
									case 0:
										ImageData id0 =img0.getImageData().scaledTo(min, min);
										Image img0new = new Image(board.getDisplay(), id0);
										e.gc.drawImage(img0new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img0new.dispose();
										break;
									case 2:
										ImageData id2 = img2.getImageData().scaledTo(min, min);
										Image img2new = new Image(board.getDisplay(), id2);
										e.gc.drawImage(img2new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img2new.dispose();
										break;
									case 4:
										ImageData id4 = img4.getImageData().scaledTo(min, min);
										Image img4new = new Image(board.getDisplay(), id4);
										e.gc.drawImage(img4new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img4new.dispose();
										break;
									case 8:
										ImageData id8 = img8.getImageData().scaledTo(min, min);
										Image img8new = new Image(board.getDisplay(), id8);
										e.gc.drawImage(img8new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img8new.dispose();
										break;
									case 16:
										ImageData id16 = img16.getImageData().scaledTo(min, min);
										Image img16new = new Image(board.getDisplay(), id16);
										e.gc.drawImage(img16new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img16new.dispose();
										break;
									case 32:
										ImageData id32 = img32.getImageData().scaledTo(min, min);
										Image img32new = new Image(board.getDisplay(), id32);
										e.gc.drawImage(img32new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img32new.dispose();
										break;
									case 64:
										ImageData id64 = img64.getImageData().scaledTo(min, min);
										Image img64new = new Image(board.getDisplay(), id64);
										e.gc.drawImage(img64new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img64new.dispose();
										break;
									case 128:
										ImageData id128 = img128.getImageData().scaledTo(min, min);
										Image img128new = new Image(board.getDisplay(), id128);
										e.gc.drawImage(img128new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img128new.dispose();
										break;
									case 256:
										ImageData id256 = img256.getImageData().scaledTo(min, min);
										Image img256new = new Image(board.getDisplay(), id256);
										e.gc.drawImage(img256new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img256new.dispose();
										break;
									case 512:
										ImageData id512 = img512.getImageData().scaledTo(min, min);
										Image img512new = new Image(board.getDisplay(), id512);
										e.gc.drawImage(img512new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img512new.dispose();
										break;
									case 1024:
										ImageData id1024 = img1024.getImageData().scaledTo(min, min);
										Image img1024new = new Image(board.getDisplay(), id1024);
										e.gc.drawImage(img1024new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img1024new.dispose();
										break;
									case 2048:
										ImageData id2048 = img2048.getImageData().scaledTo(min, min);
										Image img2048new = new Image(board.getDisplay(), id2048);
										e.gc.drawImage(img2048new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img2048new.dispose();
										break;
									case 4096:
										ImageData id4096 = img4096.getImageData().scaledTo(min, min);
										Image img4096new = new Image(board.getDisplay(), id4096);
										e.gc.drawImage(img4096new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img4096new.dispose();
										break;
									case 8192:
										ImageData id8192 = img8192.getImageData().scaledTo(min, min);
										Image img8192new = new Image(board.getDisplay(), id8192);
										e.gc.drawImage(img8192new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										img8192new.dispose();
										break;
									}
								}
							}
							
						}
					}
				});
				
			}
		});
	}
	
	public void setBoardData(int[][] data) {
		boardData = new int[data.length][data[0].length];
		
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				boardData[i][j] = data[i][j];
			}
		}
	}
}
