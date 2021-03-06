package view.game2048;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import view.View;

/**
 * An Observable class that contains the view of the game, it notify the presenter when view changed.
 * @author Eliel Dabush and Oleg Glizerin.
 *
 */
public class Game2048View extends Observable implements View, Runnable{
	/**
	 * @param display display.
	 * @param shell shell.
	 * @param scoreLabel shows the score in a label.
	 * @param hintLabel shows the hint in a label.
	 * @param menu shows a menu above the labels.
	 * @param board style of board.
	 * @param userCommand (commands that changes the view of the game) zero at start.
	 * @param data its a state of game.
	 * @param buttons buttons.
	 * @param succeed true if win otherwise false.
	 * @param scoreHolder score holder.
	 * @param mouseDownX the x when mouse pressed.
	 * @param mouseDownY the y when mouse pressed.
	 * @param fileName string of path.
	 */
	private Display display;	// = new Display();
	private Shell shell 	;	//= new Shell(display);
	private ScoreLabel scoreLabel;
	private Hint hint;
	private Menu2048 menu ;		//= new Menu2048(shell, SWT.BAR);
	private BoardView board;
	private int userCommand = 0;
	private int [][] data;
	private Buttons2048 buttons;
	private boolean succeed = false;
//	private boolean gameOver = false;
	@SuppressWarnings("unused")
	private int scoreHolder;
	private int depth;
	private int hintsNum;
	private int mouseDownX = 0;		    //when mouse pressed, its X
	private int mouseDownY = 0; 	    //when mouse pressed, its Y
	private String fileName = "";
	
	/**
	 * Constructor
	 * @param init the board with received data[][]
	 */
	public Game2048View(int [][] data) {
		this.data = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				this.data[i][j] = data[i][j];
			}
		}
	}
	
		
	/**
	 * Initialize the GUI of the game.
	 * Button lister the style of GUI and the Mouse listener.
	 */
	public void initView(){		
		display = new Display();
		shell 	= new Shell(display);
		
		shell.setText("2048 Eliel's & Oleg's edition");
		shell.setLayout(new GridLayout(3,true));
		shell.setSize(500 , 450);
		
		menu = new Menu2048(shell, SWT.BAR);
		shell.setMenuBar(menu.getMenuBar());
		
		
		scoreLabel = new ScoreLabel(shell,SWT.FILL);
		
		Text t = new Text(shell, SWT.BORDER);
		
		t.addVerifyListener(new VerifyListener(){
			  public void verifyText(VerifyEvent event) {
			 
			    // Assume we don't allow it
				event.doit = false;
			 
			    // Get the character typed
			    char myChar = event.character;
			    String text = ((Text) event.widget).getText();
			 
			 // Allow 0-9
			   if (Character.isDigit(myChar)) event.doit = true;
			 
			   // Allow backspace
			  if (myChar == '\b') {
			  event.doit = true;
			  	
			    }
			 }
					  
			});

		hint = new Hint(shell,SWT.FILL);		
		this.board = new BoardView(shell, SWT.BORDER);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,10));
		board.setBoardData(data);	
		board.setFocus();
		
		buttons = new Buttons2048(shell, SWT.PUSH);
		
		menuListeners();
		movementListener();
		buttonsListenerstener();
			
		shell.open();
	}
	
	/**
	 * Synchronize thread that redraw the board - display the data.
	 */
	@Override
	public void displayData(int [][] d) {
		board.setBoardData(d);
		display.syncExec(new Runnable() {		
			@Override
			public void run() {	
				board.redraw();
			}
		});
	}

	/**
	 * Presenter take the userCommand from here to give it to the model
	 * 1 up, 2 down, 3 right, 4 left, 7 save , 8 load, 5 restart, 6 undo, 12 algo , 13 minimax.
	 * @return the command that has to be redrawed or did.
	 */
	@Override
	public int getUserCommand() {
		return userCommand;
	}

	/**
	 * displayc score Label.
	 */
	@Override
	public void displayScore(final int score) {
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				scoreHolder = score;
				scoreLabel.setText("Score: "+score);
				
			}
		});
	}

	/**
	 * the hint is..
	 */
	public void displayHint(final int num){
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				switch (num) {
				case 1:
					hint.setText("Hint: UP");
					break;
				case 2:
					hint.setText("Hint: DOWN");
					break;
				case 3:
					hint.setText("Hint: RIGHT");
					break;
				case 4:
					hint.setText("Hint: LEFT");
					break;
				default:
					break;
				}
			}
		});
	}
	
	/**
	 * open a game over window - if we won we can continue, restart or leave the game
	 * if we lost we can start a new game or exit.
	 */
	@Override
	public void gameOver(boolean succeed) {
		this.succeed = succeed;		
		display.syncExec(new Runnable() {
			
			@Override
			public void run() {
				gameOverAction();				
			}
		});
	}
	
	/**
	* Run of Runnable Implements
	*/
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
	/**
	 * Listeners that listen to actions of the user.
	 * 
	 */
	 
	 
	/**
	* drop down menu bar listeners
	*/
	// menu buttons listener
	public void menuListeners(){
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				menu.getFileSaveItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						//save
						userCommand = 7;
						String path = saveAction();
						if(path != null){
							setFileNamePath(path);
							setChanged();
							notifyObservers("save");
						}
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
					}
				});
				
				menu.getFileLoadItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						//load
						userCommand = 8;
						String path = loadAction();
						if(path != null){
							setFileNamePath(path);
							setChanged();
							notifyObservers("load");
							board.setFocus();
						}
						
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
						//restart
						userCommand = 5;
						setChanged();
						notifyObservers();
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getEditUndoItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						//undo
						userCommand = 6;
						setChanged();
						notifyObservers();
						board.setFocus();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});	
			}
		});
	}
	
	/**
	 * the movement listener.
	 */
	// game movement listener
	public void movementListener(){
		display.syncExec(new Runnable() {	
			@Override
			public void run() {
				board.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseDown(MouseEvent eventClickDown) {
						setMouseDownX(eventClickDown.x);
						setMouseDownY(eventClickDown.y);
					}
					
					@Override
					public void mouseUp(final MouseEvent eventClickUp) {
						int differenceX = getMouseDownX() - eventClickUp.x;
						int differenceY = getMouseDownY() - eventClickUp.y;
						boolean up = (-50 <= differenceX) && (differenceX <= 50) && (40 <= differenceY) && (differenceY <= 160 );
						boolean down = (-50 <= differenceX) && (differenceX <= 50) && (-160 <= differenceY) && (differenceY <= -60);
						boolean right = (-190 <= differenceX) && (differenceX <= -130) && (-80 <= differenceY) && (differenceY <= 80);
						boolean left = (50 <= differenceX) && (differenceX <= 190) && (-90 <= differenceY) && (differenceY <= 90);
						
						if(up){
							//up
							userCommand = 1;
							setChanged();
							notifyObservers();
						}
						else if(down){
							//down
							userCommand = 2;
							setChanged();
							notifyObservers();
						}
						
						else if(right){
							//right
							userCommand = 3;
							setChanged();
							notifyObservers();
						}
						else if(left){
							//left
							userCommand = 4;
							setChanged();
							notifyObservers();
						}
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						board.setFocus();
					}
					
				});
				
				board.addKeyListener(new KeyListener() {	
					@Override
					public void keyReleased(KeyEvent e) {
						switch(e.keyCode){
						case SWT.ARROW_UP:
							userCommand = 1;
							setChanged();
							notifyObservers();
							break;
						case SWT.ARROW_DOWN:
							userCommand = 2;
							setChanged();
							notifyObservers();
							break;
						case SWT.ARROW_RIGHT:
							userCommand = 3;
							setChanged();
							notifyObservers();
							break;
						case SWT.ARROW_LEFT:
							userCommand = 4;
							setChanged();
							notifyObservers();
							break;
							}
					}
					
					@Override
					public void keyPressed(KeyEvent e) {
					
					}
				});			
			}
		});
	}
	
	/**
	 * buttons listener.
	 */	
	// buttons listener
	public void buttonsListenerstener(){
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				
				buttons.getMinimax().addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							//get Minimax
							if(buttons.getMinimax().getText().contains("Run Minimax")){
								if(hint.getCombo1().getText().toString().contains("7")){
									depth = 7;
								}else{
									depth = Integer.parseInt(hint.getCombo1().getText());
								}
								if(hint.getCombo2().getText().toString().contains("F")){
									hintsNum = Integer.MAX_VALUE;
									buttons.getMinimax().setText("Stop Minimax");
								}else{
									hintsNum = Integer.parseInt(hint.getCombo2().getText());
								}
								
								userCommand  = 13;
								setChanged();
								notifyObservers("solve");
								board.setFocus();
								
							}else{
								buttons.getMinimax().setText("Run Minimax/Get Hints");
								userCommand  = 12;
								setChanged();
								notifyObservers();
								board.setFocus();
							}
		
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent arg0) {
							
						}
				});
					
				buttons.getRestart().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						//restart
						userCommand  = 5;
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
						//undo
						userCommand = 6;
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
						//save
						userCommand = 7;
						String path = saveAction();
						if(path != null){
							setFileNamePath(path);
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
						//load
						userCommand = 8;
						String path = loadAction();
						if(path != null){
							setFileNamePath(path);
							setChanged();
							notifyObservers("load");
							board.setFocus();
						}
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				buttons.getExit().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// EXIT
						shell.dispose();
						display.dispose();
						System.exit(0);
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
		/*		hint.getHintText().addVerifyListener(new VerifyListener() {
					
					@Override
					public void verifyText(VerifyEvent e) {
						e.doit = false;
						
						char ch = e.character;
						String text = ((Text) e.widget).getText();
						
						// allow only 0-9
						if (Character.isDigit(ch)){
							e.doit = true;
						}
						
						if(ch == '\b'){
							e.doit = true;							
						}
					}
				});
		*/	}
		});
	}
	
	/**
	 * Game is over. who won?
	 */
	public void gameOverAction(){
		if(buttons.getMinimax().getText().contains("Stop Minimax")){
			buttons.getMinimax().setText("Run Minimax");
		}
		if(!succeed){
			MessageBox gameOverBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			gameOverBox.setText("Game Over");
			gameOverBox.setMessage("Game Over. Restart game?");				
			int msg = gameOverBox.open();
			if(msg == SWT.YES){
				// start a new game
				userCommand  = 5;
				shell.setFocus();
				setChanged();
				notifyObservers();
			}else if(msg == SWT.NO){
				// EXIT
				System.exit(0);
			}
		}else{
			//continue
			final Shell shell2 = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
			shell2.setText("Amazing!");
			shell2.setSize(220, 200);
			shell2.setLocation(new Point(shell.getLocation().x + 50, shell.getLocation().y + 50));
			shell2.setLayout(new FillLayout());
			
			Canvas c = new Canvas(shell2, SWT.BORDER);
			c.setSize(shell2.getSize());
			Button con = new Button(c, SWT.PUSH);
			Button restartB = new Button(c, SWT.PUSH);
			Button exitB = new Button(c, SWT.PUSH);
			
			con.setText("Continue the game");
			restartB.setText("Restart");
			exitB.setText("Exit");
			
			con.setBounds(5, 50, 200, 25);
			restartB.setBounds(5, 80, 200, 25);
			exitB.setBounds(5, 110, 200, 25);
			
			c.setBackground(new Color(display, 255, 165, 0));
			c.addPaintListener(new PaintListener() {
				
				@Override
				public void paintControl(PaintEvent e) {
					e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_WHITE));
					e.gc.drawString("Congrats! \n You won the game.", 10, 5, true);
					
				}
			});
			
			con.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					userCommand = 11;
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
			restartB.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					//restart
					userCommand = 5;
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
					System.exit(0);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			shell2.setFocus();
			shell2.open();

		}
	}
	
	/**
	 * 
	 * @return path where it should be saved.
	 */	
	public String saveAction(){
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("save");
		fd.setFilterPath("LoadAndSave/");
		String[] filterExt = {"*.txt", "*.xml" , "*.*"};
		String[] filterNames = {"Text Files (*.txt)", "XML Files (*.xml)", "All Files (*.*)"};
		fd.setFilterExtensions(filterExt);
		fd.setFilterNames(filterNames);
		String path = fd.open();
		if(path != null){
			String selected = path;
			fileName = selected;
			return fileName;
		}
		return null;
	}
	
	/**
	 * 
	 * @return the path from where it should be loaded.
	 */
	public String loadAction(){
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("open");
		fd.setFilterPath("LoadAndSave/");
		String[] filterExt = {"*.txt", "*.xml" , "*.*"};
		String[] filterNames = {"Text Files (*.txt)", "XML Files (*.xml)", "All Files (*.*)"};
		fd.setFilterExtensions(filterExt);
		fd.setFilterNames(filterNames);
		String path = fd.open();
		if(path != null){
			String selected = path;
			fileName = selected;
			return fileName;
		}
		else{
			return null;
		}
	}
	
	/**
	 * @return path.
	 */
	@Override
	public String getFileNamePath(){
		return this.fileName;
	}
	/**
	 * sets the path to save.
	 */
	@Override
	public void setFileNamePath(String save) {
		this.fileName = save;
	}
	
	/**
	 * get how deep the user wants the algorithm to run
	 */
	@Override
	public int getDepth() {
		return depth;
	}

	/**
	 * get how many hints the user wants
	 */
	@Override
	public int getHintNum() {
		return hintsNum;
	}
	
	/**
	 * set mouse (x,  )
	 * @param x
	 */
	public void setMouseDownX(int x){
		mouseDownX = x;
	}
	/**
	 * get mouse (x, )
	 * @return
	 */
	public int getMouseDownX(){
		return mouseDownX;
	}
	/**
	 * set mouse( ,y)
	 * @param y
	 */
	public void setMouseDownY(int y){
		mouseDownY = y;
	}
	/**
	 * get mouse ( ,y)
	 * @return
	 */
	public int getMouseDownY(){
		return mouseDownY;
	}



	
}
