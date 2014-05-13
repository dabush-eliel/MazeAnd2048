package view.maze;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import view.View;

public class MazeView extends Observable implements View,Runnable {
	
	private Display display;	// = new Display();
	private Shell shell 	;	//= new Shell(display);
	private ScoreLabel scoreLabel;
	private MenuMaze menu ;		//= new Menu2048(shell, SWT.BAR);
	private BoardMazeView board;
	private int userCommand = 0;
	private int [][] data;
	private ButtonsMaze buttons;
	private boolean succeed 	= false;
	private boolean twoMoves 	= false;
	private boolean  up 	  	= false;
	private boolean  down 		= false;
	private boolean right 		= false;
	private boolean left  		= false;
	private String fileName	  	= null;
	
	
	public MazeView(int [][] data) {
		this.data = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				this.data[i][j] = data[i][j];
			}
		}
	}

	@Override
	public void displayData(int [][] data) {
		board.setMazeData(data);
		display.syncExec(new Runnable() {		
			@Override
			public void run() {	
				board.redraw();
			}
		});
	}

	@Override
	public void displayScore(int score) {
		scoreLabel.setText("Score: "+score);		
	}

	@Override
	public int getUserCommand() {
		return userCommand;
	}

	@Override
	public void initView() {
		display = new Display();
		shell = new Shell(display);
		
		shell.setText("Eliel's MAZE");
		shell.setLayout(new GridLayout(4,true));
		shell.setSize(750,600);;
		
		menu = new MenuMaze(shell, SWT.BAR);
		shell.setMenuBar(menu.getMenuBar());
		
		scoreLabel = new ScoreLabel(shell,SWT.FILL);
		
		board = new BoardMazeView(shell, SWT.BORDER, data.length, data[0].length);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,10));
		board.setMazeData(data);	
		board.setFocus();
		
		buttons = new ButtonsMaze(shell, SWT.PUSH);
		
		menuListeners();
		movementListener();
		buttonsListenerstener();
			
		shell.open();
		
		
	}
	
	@Override
	public void gameOver(boolean succeed) {
		this.succeed = succeed;		
		gameOverAction();		
	}

	@Override
	public void run() {
		initView();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
		display.dispose();
	}
	
	
	// ----------			LISTENERS          ----------
	
	// menu buttons listener
	public void menuListeners(){
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				menu.getFileSaveItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String path = save();
						if(path != null){
							userCommand = 11;
							setChanged();
							notifyObservers("save");
						}
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getFileLoadItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String path = save();
						if(path != null){
							userCommand = 12;
							setChanged();
							notifyObservers("load");
						}						
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getFileExitItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						shell.close();
						display.dispose();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getEditRestartItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						userCommand = 9;
						setChanged();
						notifyObservers();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getEditUndoItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						userCommand = 10;
						setChanged();
						notifyObservers();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});				
			}
		});
	}
	
	// game movement listener
	public void movementListener(){
		display.syncExec(new Runnable() {	
			@Override
			public void run() {
				board.addMouseMoveListener(new MouseMoveListener() {
					
					@Override
					public void mouseMove(MouseEvent e) {
					}
				});
				
				board.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseUp(MouseEvent e) {
						if(board.isMouseMoved()){
							int x_move = board.getxBm()-board.getX();
							int y_move = board.getyBm()-board.getY();
							int mvx = x_move;
							int mvy = y_move;
							boolean up = true;
							boolean right = false;
							
							if((x_move) < 0 ){
								right = true;
								mvx = x_move*-1;
							}
							if(y_move < 0){
								up = false;
								mvy = y_move*-1;
							}
							if(mvx>board.getMx() && mvy>board.getMy() ){
								if (up == true) {
									if(right == true){
										userCommand = 5;
										setChanged();
										notifyObservers();
									}else{
										userCommand = 6;
										setChanged();
										notifyObservers();
									}
								} else {
									if(right){
										userCommand = 7;
										setChanged();
										notifyObservers();
									}else{
										userCommand = 8;
										setChanged();
										notifyObservers();
									}
								}
							}else if(mvx>board.getMx()){
								if(right){
									userCommand = 3;
									setChanged();
									notifyObservers();
								}else{
									userCommand = 4;
									setChanged();
									notifyObservers();
								}
							}else{
								if(up == true){
									userCommand = 1;
									setChanged();
									notifyObservers();
								}else{
									userCommand = 2;
									setChanged();
									notifyObservers();
								}
							}
						}
					}
					
					@Override
					public void mouseDown(MouseEvent e) {
						
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						board.setFocus();
					}
				});
				
				board.addKeyListener(new KeyListener() {	
					@Override
					public void keyReleased(KeyEvent e) { 
						
					}
					
					@Override
					public void keyPressed(KeyEvent e) {
						int pressed = 0;
						switch(e.keyCode){
						
						case SWT.ARROW_UP:
							pressed = SWT.ARROW_UP;		
						/*	if(pressed == SWT.ARROW_UP){
								userCommand = 1;
								setChanged();
								notifyObservers();
								break;
							}*/
							if(getTwoMoves()){
								twoMoves = false;
								if(right){
									userCommand = 5;
									setChanged();
									notifyObservers();
									right = false;
								}else if(left){
									userCommand = 6;
									setChanged();
									notifyObservers();
									left = false;
								}
								up = false;
							}else{								
								twoMoves = true;
								up = true;
								display.timerExec(120, new Runnable() {
									@Override
									public void run() {										
										if(getTwoMoves()){
											userCommand = 1;
											setChanged();
											notifyObservers();
											twoMoves = false;
										}
										up = false;	
									}
								});
							}							
							break;
							
						case SWT.ARROW_DOWN:
							pressed = SWT.ARROW_DOWN;							
							if(getTwoMoves()){
								twoMoves = false;
						/*		if(pressed == SWT.ARROW_DOWN){
									userCommand = 2;
									setChanged();
									notifyObservers();
									break;
								}*/
								if(right){
									userCommand = 7;
									setChanged();
									notifyObservers();
									right = false;
								}
								if(left){
									userCommand = 8;
									setChanged();
									notifyObservers();
									left = false;
								}
								down = false;
							}else{								
								twoMoves = true;
								down = true;								
								display.timerExec(120, new Runnable() {
									@Override
									public void run() {									
										if(getTwoMoves()){
											userCommand = 2;
											setChanged();
											notifyObservers();
											twoMoves = false;
										}
										down = false;
									}
								});
							}
							break;
							
						case SWT.ARROW_RIGHT:	
							pressed = SWT.ARROW_RIGHT;							
							if(getTwoMoves()){
								twoMoves = false;
							/*	if(pressed == SWT.ARROW_RIGHT){
									userCommand = 3;
									setChanged();
									notifyObservers();
									break;
								}*/
								if(up){
									userCommand = 5;
									setChanged();
									notifyObservers();
									up = false;
									break;
								}
								if(down){
									userCommand = 7;
									setChanged();
									notifyObservers();
									down = false;
									break;
								}		
								right = false;
							}else{
								twoMoves = true;
								right = true;
								display.timerExec(120, new Runnable() {
									@Override
									public void run() {										
										if(getTwoMoves()){
											userCommand = 3;
											setChanged();
											notifyObservers();
											twoMoves = false;											
										}					
										right = false;
									}
								});
							}
							break;
							
						case SWT.ARROW_LEFT:
							pressed = SWT.ARROW_LEFT;							
							if(getTwoMoves()){
								twoMoves = false;
							/*	if(pressed == SWT.ARROW_LEFT){
									userCommand = 4;
									setChanged();
									notifyObservers();
									break;
								}*/
								if(up){
									userCommand = 6;
									setChanged();
									notifyObservers();
									up = false;
								}
								if(down){
									userCommand = 8;
									setChanged();
									notifyObservers();
									down = false;
								}
								left = false;
							}else{								
								twoMoves = true;
								left = true;
								display.timerExec(120, new Runnable() {
									@Override
									public void run() {
										if(getTwoMoves()){
											userCommand = 4;
											setChanged();
											notifyObservers();
											twoMoves = false;											
										}					
										left = false;
									}
								});
							}
							break;	
							
						// play with the numbers on the right side of the keyboard --> num lk should be off	
						case SWT.PAGE_UP:
							userCommand = 5;
							setChanged();
							notifyObservers();
							break;
						case SWT.HOME:
							userCommand = 6;
							setChanged();
							notifyObservers();
							break;
						case SWT.PAGE_DOWN:
							userCommand = 7;
							setChanged();
							notifyObservers();
							break;
						case SWT.END:
							userCommand = 8;
							setChanged();
							notifyObservers();
							break;
						}						
					}
				});			
			}
		});
	}
	
	// buttons listener
	public void buttonsListenerstener(){
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				
				buttons.getRestart().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						userCommand  = 9;
						setChanged();
						notifyObservers();
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
					}
				});
				
				buttons.getUndo().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						userCommand = 10;
						setChanged();
						notifyObservers();
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
					}
				});
				
				buttons.getSave().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String path = save();
						if(path != null){
							userCommand = 11;
							setChanged();
							notifyObservers("save");
						}
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				buttons.getLoad().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String path = load();
						if(path != null){
							userCommand = 12;
							setChanged();
							notifyObservers("load");
						}
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				buttons.getExit().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						// EXIT
						board.dispose();
						shell.dispose();
						display.dispose();
						System.exit(0);
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
	
	public void gameOverAction(){
		
		final Shell shell2 = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		shell2.setText("Amazing!");
		shell2.setSize(220, 200);
		shell2.setLocation(new Point(shell.getLocation().x + 50, shell.getLocation().y + 50));
		shell2.setLayout(new FillLayout());
		
	//	Image winBG = new Image(display, "images/congrats.png");
		Canvas c = new Canvas(shell2, SWT.BORDER);
		c.setSize(shell2.getSize());
	//	Button con = new Button(c, SWT.PUSH);
		Button restartB = new Button(c, SWT.PUSH);
		Button exitB = new Button(c, SWT.PUSH);
		
	//	con.setText("Continue the game");
		restartB.setText("Restart");
		exitB.setText("Exit");
		
	//	con.setBounds(5, 50, 200, 25);
		restartB.setBounds(5, 80, 200, 25);
		exitB.setBounds(5, 110, 200, 25);
		
		c.setBackground(new Color(display, 139, 139, 131));
		c.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
				e.gc.drawString("Congrats! \n You won the game.", 10, 5, true);
				
			}
		});
		
		restartB.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				userCommand = 9;
				setChanged();
				notifyObservers();
				shell2.close();
				board.setFocus();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		exitB.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				display.dispose();
				System.exit(0);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		shell2.setFocus();
		shell2.open();
		
	/*	MessageBox gameOverBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		gameOverBox.setText("You found the goal");
		gameOverBox.setMessage("Game Over. Start a new game?");				
		int msg = gameOverBox.open();
		if(msg == SWT.YES){
			// start a new game
			userCommand  = 9;
			setChanged();
			notifyObservers();
			board.setFocus();
		}else if(msg == SWT.NO){
			// EXIT
//			board.dispose();
//			shell.dispose();
//			display.dispose();	
//			System.exit(0);
		}
	*/	
	}
	
	public String save(){
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("Save Game");
		fd.setFilterPath("LoadAndSave/");
		String[] filterExt = {"*.maze.txt","*.txt","*.xml" , "*.*"};
		String[] filterNames = {"Maze Text Files (*.maze.txt)","Text Files (*.txt)","XML Files (*.xml)", "All Files (*.*)"};
		fd.setFilterExtensions(filterExt);
		fd.setFilterNames(filterNames);
		fileName = fd.open();
		return fileName;
	}
	
	public String load(){
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Load Maze");
		fd.setFilterPath("LoadAndSave/");
		String[] filterExt = {"*.maze.txt","*.txt","*.xml" , "*.*"};
		String[] filterNames = {"Maze Text Files (*.maze.txt)","Text Files (*.txt)","XML Files (*.xml)", "All Files (*.*)"};
		fd.setFilterExtensions(filterExt);
		fd.setFilterNames(filterNames);
		fileName = fd.open();
		return fileName;
	}
	
	@Override
	public String getFileNamePath() {
		return fileName;
	}
	
	public boolean getTwoMoves(){
		return twoMoves;
	}

	@Override
	public void setFileNamePath(String save) {
		// TODO Auto-generated method stub
		
	}

}
