package game2048.view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class BoardView extends Canvas {

	int [][] boardData = new int[4][4];
//	int command = 0;
	int maxX;
	int maxY;

	public BoardView(Composite parent, int style) {
		super(parent, style);
		
	//	int boardSize = Math.min(parent.getSize().x,parent.getSize().y-50);
	//	setSize(boardSize,boardSize);
		this.getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				addPaintListener(new PaintListener() {
					
					@Override
					public void paintControl(PaintEvent e) {
						BoardView board = (BoardView) e.widget;
						
						maxX = board.getSize().x;
						maxY = board.getSize().y;
						int mx = maxX/2, my = maxY/2;
						
				//		int lineSize = Math.min(maxX, maxY);
						
				//		Rectangle rec 	= new Rectangle(1, 1, maxX-8, maxY-10);
						
						int sqrWidth 	= (maxX-25)/4;
						int sqrHeight 	= (maxY-25)/4;
						int min 		= Math.min(sqrWidth, sqrHeight);
					//	Image imgBoard = new Image(board.getDisplay(),"Z:/Prog/dev/2048_view/myBoard.png");
					//	e.gc.drawImage(imgBoard, 1, 1);
					//	e.gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
					//	imgBoard.getImageData().scaledTo((imgBoard.getBounds().width), imgBoard.getBounds().height);
					/*	e.gc.drawRectangle(rec);
						e.gc.drawLine(rec.x, rec.y, rec.x, rec.height); 
						e.gc.drawLine(rec.x+1*(sqrWidth), rec.y, rec.x+1*(sqrWidth), rec.height);
						e.gc.drawLine(rec.x+2*(sqrWidth), rec.y, rec.x+2*(sqrWidth), rec.height);
						e.gc.drawLine(rec.x+3*(sqrWidth), rec.y, rec.x+3*(sqrWidth), rec.height);
						e.gc.drawLine(rec.x, rec.y, rec.width, rec.y);
						e.gc.drawLine(rec.x, rec.y+1*(sqrHeight), rec.width, rec.y+1*(sqrHeight));
						e.gc.drawLine(rec.x, rec.y+2*(sqrHeight), rec.width, rec.y+2*(sqrHeight));
						e.gc.drawLine(rec.x, rec.y+3*(sqrHeight), rec.width, rec.y+3*(sqrHeight));
				*/		
						if(boardData != null){	
							for(int i=0;i<boardData.length;i++){
								for(int j=0;j<boardData[0].length;j++){
								//	e.gc.drawString(""+boardData[i][j], rec.x+sqrWidth/2+j*(sqrWidth), rec.y+sqrHeight/3+i*(sqrHeight));
									switch(boardData[i][j]){
									case 0:
										Image img0 = new Image(board.getDisplay(),"images/my_0.png");
										ImageData id0 =img0.getImageData().scaledTo(min, min);									
										Image img0new = new Image(board.getDisplay(), id0);
										e.gc.drawImage(img0new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 2:
										Image img2 = new Image(board.getDisplay(),"images/my_2.png");
										ImageData id2 = img2.getImageData().scaledTo(min, min);
										Image img2new = new Image(board.getDisplay(), id2);
										e.gc.drawImage(img2new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 4:
										Image img4 = new Image(board.getDisplay(),"images/my_4.png");
										ImageData id4 = img4.getImageData().scaledTo(min, min);
										Image img4new = new Image(board.getDisplay(), id4);
										e.gc.drawImage(img4new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 8:
										Image img8 = new Image(board.getDisplay(),"images/my_8.png");
										ImageData id8 = img8.getImageData().scaledTo(min, min);
										Image img8new = new Image(board.getDisplay(), id8);
										e.gc.drawImage(img8new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 16:
										Image img16 = new Image(board.getDisplay(),"images/my_16.png");
										ImageData id16 = img16.getImageData().scaledTo(min, min);
										Image img16new = new Image(board.getDisplay(), id16);
										e.gc.drawImage(img16new,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 32:
										Image img32 = new Image(board.getDisplay(),"images/my_32.png");
										ImageData id32 = img32.getImageData().scaledTo(min, min);
										Image img32new = new Image(board.getDisplay(), id32);
										e.gc.drawImage(img32new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 64:
										Image img64 = new Image(board.getDisplay(),"images/my_64.png");
										ImageData id64 = img64.getImageData().scaledTo(min, min);
										Image img64new = new Image(board.getDisplay(), id64);
										e.gc.drawImage(img64new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 128:
										Image img128 = new Image(board.getDisplay(),"images/my_128.png");
										ImageData id128 = img128.getImageData().scaledTo(min, min);
										Image img128new = new Image(board.getDisplay(), id128);
										e.gc.drawImage(img128new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 256:
										Image img256 = new Image(board.getDisplay(),"images/my_256.png");
										ImageData id256 = img256.getImageData().scaledTo(min, min);
										Image img256new = new Image(board.getDisplay(), id256);
										e.gc.drawImage(img256new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 512:
										Image img512= new Image(board.getDisplay(),"images/my_512.png");
										ImageData id512 = img512.getImageData().scaledTo(min, min);
										Image img512new = new Image(board.getDisplay(), id512);
										e.gc.drawImage(img512new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 1024:
										Image img1024 = new Image(board.getDisplay(),"images/my_1024.png");
										ImageData id1024 = img1024.getImageData().scaledTo(min, min);
										Image img1024new = new Image(board.getDisplay(), id1024);
										e.gc.drawImage(img1024new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 2048:
										Image img2048 = new Image(board.getDisplay(),"images/my_2048.png");
										ImageData id2048 = img2048.getImageData().scaledTo(min, min);
										Image img2048new = new Image(board.getDisplay(), id2048);
										e.gc.drawImage(img2048new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 4096:
										Image img4096 = new Image(board.getDisplay(),"images/my_4096.png");
										ImageData id4096 = img4096.getImageData().scaledTo(min, min);
										Image img4096new = new Image(board.getDisplay(), id4096);
										e.gc.drawImage(img4096new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 8192:
										Image img8192 = new Image(board.getDisplay(),"images/my_8192.png");
										ImageData id8192 = img8192.getImageData().scaledTo(min, min);
										Image img8192new = new Image(board.getDisplay(), id8192);
										e.gc.drawImage(img8192new, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
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
	//	this.boardData = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				boardData[i][j] = data[i][j];
			}
		}
	}
}
