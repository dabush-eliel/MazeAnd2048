package gameMaze.view;

import gameMaze.view.components.ButtonsMaze;
import gameMaze.view.components.MenuMaze;
import gameMaze.view.components.ScoreLabel;

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
		shell.setSize(550 , 525);
		
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
						default:
							//userCommand = 0;
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
				
				buttons.getExit().addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						// EXIT
						display.sleep();
						shell.dispose();
						display.dispose();						
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
		MessageBox gameOverBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		gameOverBox.setText("You found the goal");
		gameOverBox.setMessage("Game Over. Start a new game?");				
		int msg = gameOverBox.open();
		if(msg == SWT.YES){
			// start a new game
			userCommand  = 5;
			board.setFocus();
			setChanged();
			notifyObservers();
		}else if(msg == SWT.NO){
			// EXIT
			display.sleep();
			shell.dispose();
			display.dispose();	
			
		}
	}

}
