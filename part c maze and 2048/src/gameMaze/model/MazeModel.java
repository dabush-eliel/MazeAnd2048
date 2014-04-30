package gameMaze.model;

import java.util.Observable;
import java.util.Stack;

public class MazeModel extends Observable implements Model{
	final private int rows 					= 12;					// NEED TO SET	
	final private int columns 				= 12;					// NEED TO SET	
	private int walls[][]					= {{6,4},{5,5},{7,7}};		// NEED TO SET	
	private int maze[][]					= new int[rows][columns];
	// we use int array of 2 num's like a point - .[0] = x , .[1] = y
	private int[] start_s 					= new int[2]; 			// NEED TO SET
	private int[] goal_s					= new int[2]; 			// NEED TO SET
	private int[] mouse						= new int[2];			// current position
	private boolean succeed 				= false;
	private int score						= 0;
	private Stack <Integer>	old_score		= new Stack <Integer>();
	private Stack <int [][]> old_states		= new Stack <int [][]>();
	
	public MazeModel(){
		initGame();
	}
	
	@Override
	public void initGame() {
		start_s[0] 	= 2;
		start_s[1]	= 10;
		goal_s[0] 	= 3;
		goal_s[1] 	= 1;
		// set maze start = 1, goal = 2, walls = -1, allowed moves = 0 / 2 -> goal;
		// we also set the boundaries to be -1 instead of been out of the array limits
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++){
				maze[i][j] = 0;
			}
		}
		maze[start_s[0]][start_s[1]] = 1;
		maze[goal_s[0]][goal_s[1]] = 2;
		for(int[] w : walls){
			maze[w[0]][w[1]] = -1;
		}
		// current mouse state
		mouse[0] = start_s[0];
		mouse[1] = start_s[1];
		
		// boundaries
		for(int j=0; j<maze[0].length; j++){
			maze[0][j] 			= -1;
			maze[j][0] 			= -1;
			maze[rows-1][j]	 	= -1;
			maze[j][columns-1] 	= -1;
		}
	}

	@Override
	public void doUserCommand(int num) {
		switch(num){
		case 1:
			moveUP();
			setChanged();
			notifyObservers();
			break;
		case 2:
			moveDown();
			setChanged();
			notifyObservers();
			break;
		case 3:
			moveRight();
			setChanged();
			notifyObservers();
			break;
		case 4:
			moveLeft();
			setChanged();
			notifyObservers();
			break;
		case 5:
			moveUpRight();
			setChanged();
			notifyObservers();
			break;
		case 6:
			moveUpLeft();
			setChanged();
			notifyObservers();
			break;
		case 7:
			moveDownRight();
			setChanged();
			notifyObservers();
			break;
		case 8:
			moveDownLeft();
			setChanged();
			notifyObservers();
			break;
		case 9:
			restartGame();
			setChanged();
			notifyObservers();
			break;
		case 10:
			undoMove();
			setChanged();
			notifyObservers();
			break;
		case 11:
			break;
		case 12:
			break;
		}
	}

	@Override
	public void moveUP() {
		int s = maze[mouse[0]][mouse[1]-1];
		if(s != -1 ){
			if(s == 2){
				// found the exit ! 
				succeed = true;

				maze[mouse[0]][mouse[1]] = 0; 
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 3;
				score += 10;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 1;
				score += 10;
			}
			System.out.println("movedUP");
		}
	}

	@Override
	public void moveDown() {
		int s = maze[mouse[0]][mouse[1]+1];
		if(s != -1 ){
			if(s == 2){
				// found the exit ! 
				succeed = true;
				
				maze[mouse[0]][mouse[1]] = 0;
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 3;
				score += 10;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 1;
				score += 10;
			}
			
		}		
	}

	@Override
	public void moveRight() {
		int s = maze[mouse[0]+1][mouse[1]];
		if(s != -1 ){
			if(s == 2){
				// found the exit ! 
				succeed = true;
				
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]++;
				maze[mouse[0]][mouse[1]] = 3;
				score += 10;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]++;
				maze[mouse[0]][mouse[1]] = 1;
				score += 10;
			}

		}		
	}

	@Override
	public void moveLeft() {
		int s = maze[mouse[0]-1][mouse[1]];
		if(s != -1 ){
			if(s == 2){
				// found the exit ! 
				succeed = true;
				
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]--;
				maze[mouse[0]][mouse[1]] = 3;
				score += 10;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]--;
				maze[mouse[0]][mouse[1]] = 1;
				score += 10;
			}

		}		
	}

	@Override
	public void moveUpRight() {

		
	}

	@Override
	public void moveUpLeft() {

		
	}

	@Override
	public void moveDownRight() {

		
	}

	@Override
	public void moveDownLeft() {

		
	}
	

	@Override
	public int[][] getMaze() {
		return maze;
	}

	@Override
	public void restartGame() {
				
	}

	@Override
	public void undoMove() {
		// TODO Auto-generated method stub
		
	}
	
	public int[] getMouse() {
		return mouse;
	}

	public boolean isSucceed() {
		return succeed;
	}

	public int getScore() {
		return score;
	}

	public Stack<Integer> getOld_score() {
		return old_score;
	}

	public Stack<int[][]> getOld_states() {
		return old_states;
	}

}
