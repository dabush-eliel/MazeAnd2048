package gameMaze.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import com.thoughtworks.xstream.XStream;

import maze.Maze;
import maze.MazeDomain;
import model.algorithms.State;

public class MazeModel extends Observable implements Model{
	final private int rows					= 20+2;   // NEED TO SET	height + 2
	final private int columns				= 20+2;	// NEED TO SET	width + 2
	private int walls[][]					= {{6,4},{5,5},{7,7},{19,20},{19,19},{19,18},{19,17},{19,16},{19,15},{19,13},{19,12}};		// NEED TO SET	
	private int maze[][];
	// we use int array of 2 num's as a point - s[0] = X , s[1] = Y
	private int[] start_s 					= new int[2]; 			// NEED TO SET
	private int[] goal_s					= new int[2]; 			// NEED TO SET
	private int[] mouse						= new int[2];			// current position
	private boolean succeed 				= false;
	private int score						= 0;
	private Stack <int []> old_mouse		= new Stack<int []>();
	private Stack <Integer>	old_score		= new Stack <Integer>();
	private Stack <int [][]> old_states		= new Stack <int [][]>();
	
// ------------------------ Maze & MazeDomain & algorithms ---------------------------//
	
	State start;
	State goal;
//	Maze m;
//	MazeDomain md;
	
	
	public MazeModel(int r, int c){
		// we want to get the size of the maze from the player
	//	this.rows = r;
	//	this.columns = c;
		this.maze = new int[rows][columns];
		initGame();
	}
	
	
	public MazeModel() {
		this.maze = new int[rows][columns];
		initGame();
		start = new State(start_s[0]+","+start_s[1]);
		goal  = new State(goal_s[0]+","+goal_s[1]);
//		md = new MazeDomain(new Maze(height, width, start, goal, walls));
	}
	
	// we find some path from the start to the goal and we open the access to this path if there any walls - because we must a some path in the maze
	
	// find a random spot for the mouse & goal & walls 
	private void setPositions() {
		
		int numOfWalls 	= ((rows*columns))*3/4;
		walls 			= new int[numOfWalls][2];		
		boolean go = true; 
		Random rand = new Random();
		ArrayList<int[]> usedWalls = new ArrayList<int[]>();
		
		//int start[] = new int[2];
		start_s[0] = rand.nextInt(rows-2)+1;
		start_s[1] = rand.nextInt(columns-2)+1;
		
		//int goal[] = new int[2];
		goal_s[0] = rand.nextInt(rows-2)+1;
		goal_s[1] = rand.nextInt(columns-2)+1;
		
		for(int i=0;i<numOfWalls;i++){			
			go = true;
			while(go){
				int w[] = new int[2];
				w[0] = rand.nextInt(rows-2)+1;
				w[1] = rand.nextInt(columns-2)+1;

				if(!(usedWalls.contains(w))){
					go = false;
					usedWalls.add(w);
					walls[i][0] = w[0];
					walls[i][1] = w[1];
				}
			}
		}
	}
	
	@Override
	public void initGame() {
		start_s[0] 	= 20;
		start_s[1]	= 20;
		goal_s[0] 	= 1;
		goal_s[1] 	= 1;
		
		// method to find automatically what will be the start & goal & walls for the maze that user asked for
		
			//	setPositions();	
		
		// set maze start = 1, goal = 2, walls = -1, allowed moves = 0 / 2 -> goal;
		// we also set the boundaries to be -1 instead of been out of the array limits
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++){
				maze[i][j] = 0;
			}
		}
		
		for(int[] w : walls){
			maze[w[0]][w[1]] = -1;
		}
		
		maze[start_s[0]][start_s[1]] = 1;
		maze[goal_s[0]][goal_s[1]] = 2;
		
		// current mouse state
		mouse[0] = start_s[0];
		mouse[1] = start_s[1];
		
		// boundaries - set it to '-1' to get a black frame
		for(int j=0; j<maze[0].length; j++){
			maze[0][j] 			= -1;
			maze[j][0] 			= -1;
			maze[rows-1][j]	 	= -1;
			maze[j][columns-1] 	= -1;
		}
		
		succeed = false;
		score = 0;
		old_mouse.clear();
		old_score.clear();
		old_states.clear();
		
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
			save();
			setChanged();
			notifyObservers();
			break;
		case 12:
			load();
			setChanged();
			notifyObservers();
			break;
		}
	}

	@Override
	public void moveUP() {
		int s = maze[mouse[0]-1][mouse[1]];
		if(s != -1 ){
			
			// save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
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
			System.out.println("movedUP");
		}
	}

	@Override
	public void moveDown() {
		int s = maze[mouse[0]+1][mouse[1]];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
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
	public void moveRight() {
		int s = maze[mouse[0]][mouse[1]+1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}

			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);

			
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
	public void moveLeft() {
		int s = maze[mouse[0]][mouse[1]-1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
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

		}		
	}

	@Override
	public void moveUpRight() {
		int s = maze[mouse[0]-1][mouse[1]+1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
			
			if(s == 2){
				// found the exit ! 
				succeed = true;

				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]--;
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 3;
				score += 15;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0;
				mouse[0]--;
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 1;
				score += 15;
			}
			System.out.println("movedUpRight");
		}		
	}

	@Override
	public void moveUpLeft() {
		int s = maze[mouse[0]-1][mouse[1]-1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
			if(s == 2){
				// found the exit ! 
				succeed = true;

				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]--;
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 3;
				score += 15;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0;
				mouse[0]--;
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 1;
				score += 15;
			}
			System.out.println("movedUpLeft");
		}
		
	}

	@Override
	public void moveDownRight() {
		int s = maze[mouse[0]+1][mouse[1]+1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
			if(s == 2){
				// found the exit ! 
				succeed = true;

				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]++;
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 3;
				score += 15;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0;
				mouse[0]++;
				mouse[1]++;
				maze[mouse[0]][mouse[1]] = 1;
				score += 15;
			}
			System.out.println("movedDownRight");
		}
		
	}

	@Override
	public void moveDownLeft() {
		int s = maze[mouse[0]+1][mouse[1]-1];
		if(s != -1 ){
			
			// 	save the last board b4 change it
			// keep it general so we can use it if we will use moving walls
			int [][] lastPosition = new int[rows][columns];
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					lastPosition[i][j] = maze[i][j];
				}
			}
			
			int lastMouse[] = new int[2];
			lastMouse[0] = mouse[0];
			lastMouse[1] = mouse[1];
			old_mouse.push(lastMouse);
			old_score.push(score);
			old_states.push(lastPosition);
			
			if(s == 2){
				// found the exit ! 
				succeed = true;

				maze[mouse[0]][mouse[1]] = 0; 
				mouse[0]++;
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 3;
				score += 15;
			}else{
				// make the move
				maze[mouse[0]][mouse[1]] = 0;
				mouse[0]++;
				mouse[1]--;
				maze[mouse[0]][mouse[1]] = 1;
				score += 15;
			}
			System.out.println("movedDownLeft");
		}
		
	}
	

	@Override
	public int[][] getMaze() {
		return maze;
	}

	@Override
	public void restartGame() {
		initGame();
	}

	@Override
	public void undoMove() {
		if(!(old_states.empty()) && !(old_score.empty())){
			int [][] last_board2048		= new int[rows][columns];
			last_board2048 				= old_states.pop();
			score 						= old_score.pop();
			int [] last_mosue			= old_mouse.pop(); 
			mouse[0] = last_mosue[0];
			mouse[1] = last_mosue[1];
			
			for (int i = 0 ; i < rows ; i++){
				for (int j = 0 ; j < columns ; j++){
					maze[i][j] = last_board2048[i][j];
				}	
			}	
			setChanged();
			notifyObservers();
		}
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


	@Override
	public void save() {

		
	}


	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getFileNameToSave() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setFileNameToSave(String save) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getFileNameToLoad() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setFileNameToLoad(String load) {
		// TODO Auto-generated method stub
		
	}

}
