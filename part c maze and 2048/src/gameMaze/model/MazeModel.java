package gameMaze.model;

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

public class MazeModel extends Observable implements Model{
	private int rows						= 20+2;   // NEED TO SET	height + 2
	private int columns						= 20+2;	// NEED TO SET	width + 2
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
	private String fileName					= null;
	
	
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
			undoMove();
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


	public void setOld_mouse(Stack<int[]> old_mouse) {
		this.old_mouse = old_mouse;
	}


	public void setOld_score(Stack<Integer> old_score) {
		this.old_score = old_score;
	}


	public void setOld_states(Stack<int[][]> old_states) {
		this.old_states = old_states;
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
					/*	if(maze[i][j] == -1){
							out.write(""+5+',');
						}else{
							out.write(""+maze[i][j]+',');
						}*/
						out.write(""+maze[i][j]+',');
						
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
						if(last[m][n] == 5){
							last[m][n] = -1;
						}
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

}
