package game2048.view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
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
	//					int mx = maxX/2, my = maxY/2;
						
				//		int lineSize = Math.min(maxX, maxY);
						
				//		Rectangle rec 	= new Rectangle(1, 1, maxX-8, maxY-10);
						
						int sqrWidth 	= (maxX-25)/4;
						int sqrHeight 	= (maxY-25)/4;
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
										e.gc.drawImage(img0, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 2:
										Image img2 = new Image(board.getDisplay(),"images/my_2.png");
										e.gc.drawImage(img2, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 4:
										Image img4 = new Image(board.getDisplay(),"images/my_4.png");
										e.gc.drawImage(img4,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 8:
										Image img8 = new Image(board.getDisplay(),"images/my_8.png");
										e.gc.drawImage(img8,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 16:
										Image img16 = new Image(board.getDisplay(),"images/my_16.png");
										e.gc.drawImage(img16,(j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 32:
										Image img32 = new Image(board.getDisplay(),"images/my_32.png");
										e.gc.drawImage(img32, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 64:
										Image img64 = new Image(board.getDisplay(),"images/my_64.png");
										e.gc.drawImage(img64, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 128:
										Image img128 = new Image(board.getDisplay(),"images/my_128.png");
										e.gc.drawImage(img128, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 256:
										Image img256 = new Image(board.getDisplay(),"images/my_256.png");
										e.gc.drawImage(img256, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 512:
										Image img512= new Image(board.getDisplay(),"images/my_512.png");
										e.gc.drawImage(img512, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 1024:
										Image img1024 = new Image(board.getDisplay(),"images/my_1024.png");
										e.gc.drawImage(img1024, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 2048:
										Image img2048 = new Image(board.getDisplay(),"images/my_2048.png");
										e.gc.drawImage(img2048, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 4096:
										Image img4096 = new Image(board.getDisplay(),"images/my_4096.png");
										e.gc.drawImage(img4096, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
										break;
									case 8192:
										Image img8192 = new Image(board.getDisplay(),"images/my_8192.png");
										e.gc.drawImage(img8192, (j+1)*5+j*sqrWidth, (i+1)*5+i*sqrHeight);
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
