package game2048.view;
import game2048.view.components.Buttons2048;
import game2048.view.components.Menu2048;
import game2048.view.components.ScoreLabel;
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


public class Game2048View extends Observable implements View, Runnable{
	
	private Display display;	// = new Display();
	private Shell shell 	;	//= new Shell(display);
	private ScoreLabel scoreLabel;
	private Menu2048 menu ;		//= new Menu2048(shell, SWT.BAR);
	private BoardView board;
	private int userCommand = 0;
	private int [][] data;
	private Buttons2048 buttons;
	private boolean succeed = false;
	
	private String fileName = "";
	
	public Game2048View(int [][] data) {
		this.data = new int[data.length][data[0].length];
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	public void initView(int [][] data){		
		display = new Display();
		shell 	= new Shell(display);
		
		shell.setText("2048 Eliel's edition");
		shell.setLayout(new GridLayout(3,true));
		shell.setSize(400 , 345);
		
		menu = new Menu2048(shell, SWT.BAR);
		shell.setMenuBar(menu.getMenuBar());
		
		scoreLabel = new ScoreLabel(shell,SWT.FILL);
		
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
	
	@Override
	public void displayBoard(int [][] d) {	
		board.setBoardData(d);
		display.syncExec(new Runnable() {		
			@Override
			public void run() {	
				board.redraw();
			}
		});
	}

	@Override
	public int getUserCommand() {
		return userCommand;
	}

	@Override
	public void displayScore(int score) {
		scoreLabel.setText("Score: "+score);
	}

	@Override
	public void gameOver(boolean succeed) {
		this.succeed = succeed;		
		gameOverAction();
	}
	
	@Override
	public void run() {	
		initView(data);
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
	
	// game movement listener
	public void movementListener(){
		display.syncExec(new Runnable() {	
			@Override
			public void run() {
				board.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub
						
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
		/*				switch(e.keyCode){
						case SWT.ARROW_UP:
							userCommand = 1;
						case SWT.ARROW_DOWN:
							userCommand = 2;
						case SWT.ARROW_RIGHT:
							userCommand = 3;
						case SWT.ARROW_LEFT:
							userCommand = 4;					
						default:
							userCommand = 0;
						}
						setChanged();
						notifyObservers();
		*/			}
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
			}
		});
	}
	
	public void gameOverAction(){
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
		/*	MessageBox winBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CLOSE); // close doesnt work !! :O
			winBox.setText("Congrats");
			winBox.setMessage("Congratulations. You won the game! \n Do you want continue the game?");				
			int msg = winBox.open();
			//winBox.
			if(msg == SWT.NO){
				// start a new game
			//	userCommand  = 5;
			//	setChanged();
				notifyObservers();
			}else if(msg == SWT.YES){
				userCommand = 11;
				setChanged();
				notifyObservers();
			}/*else if(msg == SWT.CLOSE){
				System.exit(0);
			}	*/
			
			final Shell shell2 = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
			shell2.setText("Amazing!");
			shell2.setSize(220, 200);
			shell2.setLocation(new Point(shell.getLocation().x + 50, shell.getLocation().y + 50));
			shell2.setLayout(new FillLayout());
			
		//	Image winBG = new Image(display, "images/congrats.png");
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
	
	@Override
	public String getFileNamePath(){
		return this.fileName;
	}

	@Override
	public void setFileNamePath(String save) {
		this.fileName = save;
	}

	
	
}
