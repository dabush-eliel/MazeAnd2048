package gameMaze.view;

import game2048.view.BoardView;
import game2048.view.components.Buttons2048;
import game2048.view.components.Menu2048;
import game2048.view.components.ScoreLabel;
import gameMaze.view.components.ButtonsMaze;
import gameMaze.view.components.MenuMaze;

import java.util.Observable;
import java.util.Timer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MazeView extends Observable implements View,Runnable {
	
	private Display display;	// = new Display();
	private Shell shell 	;	//= new Shell(display);
	private ScoreLabel scoreLabel;
	private MenuMaze menu ;		//= new Menu2048(shell, SWT.BAR);
	private BoardMazeView board;
	private int userCommand = 0;
	private int [][] data;
	private ButtonsMaze buttons;
	private boolean succeed = false;
	
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
		display.asyncExec(new Runnable() {		
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
		shell.setLayout(new GridLayout(3,false));
		shell.setSize(500 , 525);
		
		menu = new MenuMaze(shell, SWT.BAR);
		shell.setMenuBar(menu.getMenuBar());
		
		scoreLabel = new ScoreLabel(shell,SWT.FILL);
		buttons = new ButtonsMaze(shell, SWT.PUSH);
		
		board = new BoardMazeView(shell, SWT.BORDER, data.length, data[0].length);
		board.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,1));
		board.setMazeData(data);	
		board.setFocus();
		
		menuListeners();
		movementListener();
		buttonsListenerstener();
			
		shell.open();
		
		
	}
	
	@Override
	public void gameOver(boolean succeed) {
		this.succeed = succeed;		
//		gameOverAction();		
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
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
				menu.getFileLoadItem().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
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
						
					}
					
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						shell.setFocus();
					}
				});
				
				board.addKeyListener(new KeyListener() {	
					@Override
					public void keyReleased(KeyEvent e) { 
	/*					switch(e.keyCode){
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
			*/			
					}
					
					@Override
					public void keyPressed(KeyEvent e) {
						switch(e.keyCode){
						case SWT.ARROW_UP:
							board.addKeyListener(new KeyListener() {
								
								@Override
								public void keyReleased(KeyEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void keyPressed(KeyEvent arg0) {
									// TODO Auto-generated method stub
									
								}
							});
							userCommand = 1;
							break;
						case SWT.ARROW_DOWN:
							userCommand = 2;
							break;
						case SWT.ARROW_RIGHT:
							userCommand = 3;
							break;
						case SWT.ARROW_LEFT:
							userCommand = 4;
							break;
						default:
							userCommand = 0;
							break;
						}
						setChanged();
						notifyObservers();
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
						userCommand  = 5;
						setChanged();
						notifyObservers();
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
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						
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
			MessageBox winBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CLOSE); // close doesnt work !! :O
			winBox.setText("Congrats");
			winBox.setMessage("Congratulations. You won the game! \n Do you want continue the game?");				
			int msg = winBox.open();
			if(msg == SWT.NO){
				// start a new game
				userCommand  = 5;
				setChanged();
				notifyObservers();
			}else if(msg == SWT.YES){
				userCommand = 11;
				setChanged();
				notifyObservers();
			}/*else if(msg == SWT.CLOSE){
				System.exit(0);
			}*/

		}
	}

}
