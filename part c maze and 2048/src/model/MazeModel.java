package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import algorithms.Solver;

public class MazeModel extends Observable implements Model{
	private int rows						= 20+2;   	// NEED TO SET	height + 2
	private int columns						= 20+2;		// NEED TO SET	width + 2
	private int walls[][]					= {{1,7},{1,10},{2,10},{3,10},{1,13},{2,13},{3,14},{7,3},{8,2},{8,9},{6,18},{6,19},{5,19},{4,19},{2,17},{3,17},{4,17},{2,20},{2,19},{7,19},{4,12},
							{19,20},{19,19},{19,14},{19,17},{19,16},{19,13},{19,12},{19,1},{19,3},{19,4},{19,5},{19,6},{19,8},{19,7},{19,10},{19,9},{10,3},{10,4},{12,2},{2,9},{4,15},{5,3},
							{5,8},{5,14},{4,14},{7,11},{7,12},{7,13},{5,10},{5,11},{5,12},{6,9},{7,9},{8,9},{9,9},{9,17},{9,18},{9,19},{11,19},{11,20},{10,1},{10,2},{10,5},{15,12},{17,12},{18,12},
							{13,15},{13,16},{13,17},{12,17},{11,17},{13,19},{14,19},{15,19},{16,19},{17,19},{18,19},{17,1},{15,1},{16,14},{16,15},{17,15},{15,15},{14,11},{14,12},{14,13},{5,2},
							{15,4},{15,5},{15,6},{15,8},{15,9},{14,9},{17,10},{17,9},{17,8},{17,7},{17,6},{10,11},{11,11},{12,11},{10,12},{11,12},{12,12},{10,13},{11,13},{12,13},{2,16},{5,1},
							{15,3},{14,3},{13,3},{13,4},{11,5},{11,6},{11,7},{11,8},{10,9},{13,7},{13,8},{13,9},{12,9},{8,11},{7,14},{7,15},{7,16},{6,16},{9,15},{10,15},{11,15},{8,6},{9,7},{4,1},{4,3},
							{3,2},{2,6},{2,2},{2,3},{2,4},{3,4},{3,5},{3,6},{3,7},{4,7},{5,7},{6,7},{7,7},{18,17},{17,17},{16,17},{15,17},{6,4},{5,5},{7,7},{17,3},{17,4},{8,5},{7,5},{9,4}};
	private int maze[][];
	// start/goal [0] = rows position & [1] = column position
	private int[] start_s 					= new int[2]; 			// NEED TO SET
	private int[] goal_s					= new int[2]; 			// NEED TO SET
	private int[] mouse						= new int[2];			// current position
	private boolean succeed 				= false;
	private int score						= 0;
	private Stack <int []> old_mouse		= new Stack<int []>();
	private Stack <Integer>	old_score		= new Stack <Integer>();
	private Stack <int [][]> old_states		= new Stack <int [][]>();
	private String fileName					= null;
	
	
/*	public MazeModel(int r, int c){
	// we want to get the size of the maze from the player
	//	this.rows = r;
	//	this.columns = c;
		this.maze = new int[rows][columns];
		initGame();
	}
*/	

	
	public MazeModel() {
		this.maze = new int[rows][columns];
		initGame();
	}
	
	// we find some path from the start to the goal and we open the access to this path if there any walls - because we must a some path in the maze
	
	// find a random spot for the mouse & goal & walls 
	@SuppressWarnings("unused")
	private void setPositions() {
		
		int numOfWalls 	= ((rows*columns))*3/4;
		walls 			= new int[numOfWalls][2];		
		boolean go = true; 
		Random rand = new Random();
		ArrayList<int[]> usedWalls = new ArrayList<int[]>();
		
		//int start[] = new int[2];
/*		start_s[0] = rand.nextInt(rows-2)+1;
		start_s[1] = rand.nextInt(columns-2)+1;
		
		//int goal[] = new int[2];
		goal_s[0] = rand.nextInt(rows-2)+1;
		goal_s[1] = rand.nextInt(columns-2)+1;
*/		
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
		
		// setPositions();	
		
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
			moveUp();
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
			undoMove();
			setChanged();
			notifyObservers();
			break;
		}
	}

	@Override
	public void moveUp() {
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
	public int[][] getData() {
		return maze;
	}

	@Override
	public void restartGame() {
		initGame();
	}

	@Override
	public void undoMove() {
		if(!(old_states.empty())){
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


	public void setWalls(int[][] walls) {
		this.walls = walls;
	}


	public void setMaze(int[][] maze) {
		this.maze = maze;
	}


	public void setStart_s(int[] start_s) {
		this.start_s = start_s;
	}


	public void setGoal_s(int[] goal_s) {
		this.goal_s = goal_s;
	}


	public void setMouse(int[] mouse) {
		this.mouse = mouse;
	}


	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}


	public void setScore(int score) {
		this.score = score;
	}

	public int getRows() {
		return rows;
	}


	public int getColumns() {
		return columns;
	}


	public int[][] getWalls() {
		return walls;
	}


	public int[] getStart_s() {
		return start_s;
	}


	public int[] getGoal_s() {
		return goal_s;
	}


	public Stack<int[]> getOld_mouse() {
		return old_mouse;
	}

	/**
	 * Save method: 
	 * (r = rows) \ (c = columns) \ (\n = newLine)
	 *  				first - push the current state to the stack's
	 *  				to load a game, read:
	 *  								size: rows \n columns \n
	 *  								goal: goal[0],goal[1], \n
	 *  								start: start[0],start[1] \n
	 *  								walls: walls.lenght \n  r,c,r,c,r,c,... \n
	 * 									stacks size: old_*.size (for three history stacks) \n
	 * 									mouse's: start[row],start[columns],r,c,r,c,r,c,... \n   
	 * 									score's: score1,score2,score3..., \n
	 * 									states: [0][0],[0][1],..,[0][c],...[1][0],[1][1]..,[1][c]...,[r][c] \n
	 * 									state of succeed: true=1 \ false=0
	 */
	@Override
	public void save(){
		// get file full path&name by fileName 
		File file = new File(fileName);
		BufferedWriter out;
		System.out.println("bufferedWriter.out created");
		
		//	save the last board - put it in history
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
		
		try {
			out = new BufferedWriter(new FileWriter(file));
			System.out.println("bufferedWriter.out initialized");
			// save size of the maze;
			out.write(""+rows);
			out.newLine();
			out.write(""+columns);
			out.newLine();			
			// -------- save start&goal locations -------- //
			// gives:  "row,column" of the goal 
			for(int i : goal_s){
				out.write(""+i+',');
			}
			out.newLine();
			// gives:  "row,column" of the start 
			for(int i : start_s){
				out.write(""+i+',');
			}
			out.newLine(); 
			// save walls 
			out.write(""+walls.length);
			out.newLine();
			for(int w[]  : walls ){
				for(int n : w){
					out.write(""+n+',');
				}
				//out.write(" @ ");
			}
			out.newLine();			
			// ---------- save history ---------- // 
			// save our history size - get size of some  old_stack (all have to be the same size)
			out.write(""+old_states.size());
			out.newLine();
			// save mouse history : in one row separated by '@' ; mouse rows/columns separated by ','
			for(int[] m: old_mouse){
				for(int n: m){
					out.write(""+n+',');
				}
			//	out.write(" @ ");
			}
			out.newLine();
			// save score history : in one row separated by ','
			for(Integer s : old_score){
				out.write(""+s+',');
			}
			out.newLine();
			// save maze state history : each state separated by new line "\n" ; each number (cell value) separated by ','
			for(int [][] s: old_states){
				for(int  i=0 ; i < rows ; i++){
					for(int j=0 ; j < columns ; j++){
						out.write(""+s[i][j]+',');	
					}
					out.newLine();
				}
			}			
		// save succeed value
			if(succeed){
				out.write(""+1);
			}else{
				out.write(""+0);
			}
			out.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	/**
	 * Load method: 
	 * (r = rows) \ (c = columns) \ (\n = newLine)
	 *  				to load a game, read:
	 *  								size: rows \n columns \n
	 *  								goal: goal[0],goal[1], \n
	 *  								start: start[0],start[1] \n
	 *  								walls: walls.lenght \n  r,c,r,c,r,c,... \n
	 * 									stacks size: old_*.size (for three history stacks) \n
	 * 									mouse's: start[row],start[columns],r,c,r,c,r,c,... \n   
	 * 									score's: score1,score2,score3..., \n
	 * 									states: [0][0],[0][1],..,[0][c],...[1][0],[1][1]..,[1][c]...,[r][c] \n
	 * 									state of succeed: true=1 \ false=0
	 *  
	 * 				---After we loaded we have to do undone action---
	 */
	
	@Override
	public void load(){
		// get file full path&name by fileName 
		File file = new File(fileName);
		BufferedReader in;		
		System.out.println("bufferedReader.out created");
		
		try {
			in = new BufferedReader(new FileReader(file));
			System.out.println("bufferedReader.out initialized");
			
			while(!in.ready()){
				// wait until buffer is ready - i must wait because we depends on that action. can't continue without it;
			}
			// read first line
			this.rows 		= Integer.parseInt(in.readLine());			
			this.columns 	= Integer.parseInt(in.readLine());
			// debug syso
			System.out.println("rows:"+rows+" colus:"+columns);
			
			Scanner s = new Scanner(in.readLine());
			s.useDelimiter(",");
			for(int i=0; i<2; i++){
				this.goal_s[i] = s.nextInt();				
			}
			s.close();
			
			s = new Scanner(in.readLine());
			s.useDelimiter(",");
			for(int i=0; i<2; i++){
				this.start_s[i] = s.nextInt();				
			}
			s.close();	
			
			// debug syso
			System.out.println("start position: "+start_s[0]+","+start_s[1]);
			System.out.println("goal position: "+goal_s[0]+","+goal_s[1]);
			// clear stacks 
			this.old_mouse.clear();
			this.old_score.clear();
			this.old_states.clear();
		//	s = new Scanner(in.readLine());
		//	s.close();
			int numOfWalls = Integer.parseInt(in.readLine());
			// debug syso
			System.out.println("num of walls:" + numOfWalls);
			// load walls
			s = new Scanner(in.readLine());
			s.useDelimiter(",");
			walls = new int[numOfWalls][2];
			for(int i=0 ; i<numOfWalls-1 ; i++){
				for(int j=0;j<2;j++){
					walls[i][j] = s.nextInt();	
				}
			}
			s.close();
			
			int numOfMoves = Integer.parseInt(in.readLine());
			// debug syso
			System.out.println("number Of States: "+numOfMoves);
			// mouse states
			s = new Scanner(in.readLine());
			s.useDelimiter(",");
			for(int i=0 ; i<numOfMoves ; i++){
				int mlast[] = new int[2];
				for(int j=0; j<2; j++){
					mlast[j] = s.nextInt();
				}
				old_mouse.push(mlast);
			}
			s.close();
			
			// load all score history
			s = new Scanner(in.readLine());
			s.useDelimiter(",");
			for(int i=0 ; i<numOfMoves ; i++){
				old_score.push(s.nextInt());
			}
			s.close();
			
			// debug syso
			System.out.println("current score: "+old_score.peek());
			// load all maze states from file
			for(int i=0 ; i<numOfMoves ; i++){
				int [][] last = new int[rows][columns];
				for(int m=0 ; m<rows ; m++ ){
					s = new Scanner(in.readLine());
					s.useDelimiter(",");
					for(int n=0 ; n<columns ; n++){
						last[m][n] = s.nextInt();
					}
				}
				old_states.push(last);
			}
			s.close();
			
			int x = Integer.parseInt(in.readLine());
			// debug syso
			System.out.println("succeed = "+x);
			if(x == 0){
				succeed = false;
			}else{
				succeed = true;
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public void saveXML() {
		// get file full path&name by fileName 
		File file = new File(fileName);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file));
	//		XStream xstream = new XStream();
	//		out.write(xstream.toXML(this));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	public void loadXML() {
		//XStream xstream = new XStream();
		MazeModel mm = new MazeModel();
		Object obj = (MazeModel) xstream.fromXML(new File(fileName));
		System.out.println(obj);
	}*/


	@Override
	public String getFileName() {
		return fileName;
	}


	@Override
	public void setFileName(String name) {
		fileName = name;
	}

	@Override
	public boolean isStuck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getAI(Solver sol) {
		// TODO Auto-generated method stub
		
	}

}
