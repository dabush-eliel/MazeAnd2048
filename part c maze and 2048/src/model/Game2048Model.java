package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;
import algorithms.Minimax;
import algorithms.MyAlgo;
import algorithms.Solver;

/**
 * This Class, is a Model of the Game 2048.
 * @author Eliel Dabush and Oleg Glizerin 
 *
 */

public class Game2048Model extends Observable implements Model, Serializable {
	private static final long serialVersionUID = 8774547151582184720L;
	private final int size 				= 4;				//	size of row / column (same size for us)
	private int score					= 0;
	private int [][] board2048 			= new int[size][size];
	private Stack <Integer> old_score 	= new Stack <Integer>();
	private Stack <int[][]> old_moves 	= new Stack <int[][]>();
	private boolean succeed				= false;
	private boolean stuck 				= false;
	private boolean check				= true;
	private boolean stop 				= false;
	private int tempScore				= 0;
	private String fileName; 			           		//holds path name to save
	private String host					= "localhost";
	private int port					= 2022;
	private Solver sol;
	private int command;
	
	/**
	 * @param size The Size of the Board.
	 * @param score Holds The current score.
	 * @param board2048 Holds the current board.
	 * @param old_score A stack, that holds the old scores for the method undo.
	 * @param old_moves A stack, that holds the old boards for the method undo.
	 * @param succeed A boolean, if you get the tile 2048 will be true, otherwise false.
	 * @param stuck A boolean, if you get stuck, will be true, otherwise false.
	 * @param check A boolean, for coninue the game after get tile 2048.
	 * @param stop A boolean, the game should stop , will be true , otherwise false.
	 * @param tempScore A score before the moving up,down,right or left, also used for copy constructor.
	 * @param fileName A string that holds the path for load/save.
	 * @param host The ip of the server.
	 * @param port The port of the server.
	 * @param sol Holds a solver Minimax or MyAlgo that implements the Solver interface.
	 * @param command Used for the method getAi for switch the solvers.
	 * @return The constructor initilaize the game with 2 tiles.
	 */
	public Game2048Model() {
		initGame();	
	}
	/**
	 * A copy constructor of a Game2048Model class.
	 * @param gm is a Game2048Model class that you want to copy.
	 * 
	 */
	public Game2048Model(Game2048Model gm) {
		
		this.score = gm.score;
		this.old_score = gm.old_score;
		this.old_moves = gm.old_moves;
		this.succeed = gm.isSucceed();
		this.stuck = gm.isStuck();
		this.check = gm.isCheck();
		this.tempScore = gm.tempScore;
		this.fileName = gm.fileName;
		this.host = gm.host;			// host to connect to get AI / hint
		this.port = gm.port;
		this.sol = gm.sol;
		
		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				this.board2048[i][j] = gm.board2048[i][j];
			}
		}
	}

	/**
	 * @return Initilaize the game with 2 tiles.
	 */
	public void initGame(){
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				this.board2048[i][j]	= 	0;
			}
		}
		//board2048[1][2] = 1024;
		//board2048[2][3] = 1024;
		
		// initialize 2 random Squares with the value: 2 OR 4 --> 90% for 2 and 10% for 4
		int sqr1val = squareVal();		
		int sqr1plc = squarePlace(getFreeSpotsNum());
		setSquare(sqr1val,sqr1plc);
		int sqr2val = squareVal();
		int sqr2plc = squarePlace(getFreeSpotsNum());
		setSquare(sqr2val,sqr2plc);	
		score = 0;
		old_moves.clear();
		old_score.clear();
		succeed 	= false;
		stuck 		= false;
		check 		= true;
	}
	
	
	/**
	 * 
	 * @return 2 with 90% of success and 4 with 10% of success.
	 */
	// get random value - 2 or 4
	private int squareVal(){
		Random rand = new Random();
		int x = rand.nextInt(10);
		if(x < 9){
			return 2;
		}else{
			return 4;
		}
	}
	
	/**
	 * 
	 * @param Number of free possible squares.
	 * @return Where to put the new square.
	 */
	// get where to put the new square 
	private int squarePlace(int num){		// get number of free possible squares
		if(num == 0){
			return 0;
		}
		Random rand = new Random();
		return (rand.nextInt(num)+1);
	}
	
	/**
	 * 
	 * @param sqrVal
	 * @param sqrPlc
	 */

	private void setSquare(int sqrVal, int sqrPlc) {
		if(sqrPlc == 0){
			return;
		}
		int counter = 0;
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				if (board2048[i][j] == 0){						// free spot
					counter++;
					if(sqrPlc == counter){
						this.board2048[i][j] = sqrVal;
						return;
					}
				}
			}
		}		
	}
	/**
	 * 
	 * @return Number of free cells.
	 */
	private int getFreeSpotsNum(){
		int counter = 0;
		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				if(board2048[i][j] == 0){
					counter++;
				}
			}
		}
		return counter;
	}
	
	/**
	 * @return Move up.
	 */
	@Override
	public boolean moveUp() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
		
		tempScore = score;
		
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				vals[j] = board2048[j][i];
			}
			vals = moveZeros(vals);
			vals = moveZeros(vals);
			vals = mergeVals(vals);
			vals = moveZeros(vals);
			
			for (int j = 0 ; j < size ; j++){
				board2048[j][i] = vals[j];
				if(board2048[j][i] == 2048 && check){
					succeed = true;
					check = false;
				}
			}
		}
		// if board doesn't changed don't add new square
		// also, if there is no more free spot & can't make any merge - we stuck ! 
		if(boardChanged(last_board2048, board2048)){
			old_score.push(tempScore);
			old_moves.push(last_board2048);
			return true;
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			stop = true;
			setChanged();
			notifyObservers();
			return false;
		}
		return false;
		
	}
	
	/**
	 * @return Move down.
	 */
	@Override
	public boolean moveDown() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		tempScore = score;
		
		for (int i = 0 ; i < size ; i++){
			for (int j = size-1 ; j >= 0 ; j--){
				vals[size-1-j] = board2048[j][i];
			}
			vals = moveZeros(vals);
			vals = moveZeros(vals);
			vals = mergeVals(vals);
			vals = moveZeros(vals);
			
			for (int j = size-1 ; j >= 0 ; j--){
				board2048[j][i] = vals[size-1-j];
				if(board2048[j][i] == 2048 && check){
					succeed = true;
					check = false;
				}
			}
		}
		// if board doesn't changed don't add new square
		if(boardChanged(last_board2048, board2048)){
			old_score.push(tempScore);
			old_moves.push(last_board2048);
			return true;			
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			stop = true;
			setChanged();
			notifyObservers();
			return false;
		}
		return false;
	}
	/**
	 * @return Move Right.
	 */
	@Override
	public boolean moveRight() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		tempScore = score;
		
		for (int i = 0 ; i < size ; i++){
			for (int j = size-1 ; j >= 0 ; j--){
				vals[size-1-j] = board2048[i][j];
			}
			vals = moveZeros(vals);
			vals = moveZeros(vals);
			vals = mergeVals(vals);
			vals = moveZeros(vals);
			
			for (int j = size-1 ; j >= 0 ; j--){
				board2048[i][j] = vals[size-1-j];
				if(board2048[j][i] == 2048 && check){
					succeed = true;
					check = false;
				}				
			}
		}
		// if board doesn't changed don't add new square
		if(boardChanged(last_board2048, board2048)){
			old_score.push(tempScore);
			old_moves.push(last_board2048);
			return true;
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			stop = true;
			setChanged();
			notifyObservers();
			return false;
		}
		return false;
	}
	/**
	 * @return Move left.
	 */
	@Override
	public boolean moveLeft() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		tempScore = score;
		
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				vals[j] = board2048[i][j];
			}
			vals = moveZeros(vals);
			vals = moveZeros(vals);
			vals = mergeVals(vals);
			vals = moveZeros(vals);
			
			for (int j = 0 ; j < size ; j++){
				board2048[i][j] = vals[j];
				if(board2048[j][i] == 2048 && check){
					succeed = true;
					check = false;
				}
			}
		}
		// if board doesn't changed don't add new square
		if(boardChanged(last_board2048, board2048)){
			old_score.push(tempScore);
			old_moves.push(last_board2048);
			return true;
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			stop = true;
			setChanged();
			notifyObservers();
			return false;
		}
		return false;
	}
	
	private int[] moveZeros(int []vals){
		for (int j = 0 ; j < size ; j++){
			if (vals[j] == 0){
				int k = j;
				while(k < size-1){
					vals[k] = vals[k+1];
					k++;
				}
				vals[k] = 0;
				// we made a move and we might get '0' again in the same cell.
			//	j--;
			}
		}
		return vals;
	}
	
	private int[] mergeVals(int []vals){
		for (int j = 0 ; j < size-1 ; j++){
			if(vals[j] == 0){
				continue;	
			}
			if (vals[j] == vals[j+1]){
				vals[j] 	+= vals[j+1];
				vals[j+1] 	 = 0;
				score 		+= vals[j];
			}
		}
		return vals;
	}
	
	/**
	 * 
	 * @param Old board.
	 * @param New Board.
	 * @return If there was a change return true, otherwise false.
	 */
	// src - source -> the board before the move. dst - destination -> the board after the move.
	private boolean boardChanged(int [][]src, int [][]dst) { 
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				if(src[i][j] != dst[i][j]){
					return true;
				}
			}
		}	
		return false;
	}
	
	/**
	 * @return Not in use.
	 */
	@Override
	public void moveUpRight() {
		
	}
	/**
	 * @return Not in use.
	 */
	@Override
	public void moveUpLeft() {
		
	}
	/**
	 * @return Not in use.
	 */
	@Override
	public void moveDownRight() {
		
	}
	/**
	 * @return Not in use.
	 */
	@Override
	public void moveDownLeft() {
		
	}
	/**
	 * @return Our score.
	 */
	@Override
	public int getScore(){
		return this.score;
	}
	/**
	 * @return Our board.
	 */
	@Override
	public int[][] getData() {
		return board2048;
	}
	/**
	 * @param data is the board received.
	 * @return sets the data (board) to board2048
	 */
	@Override
	public void setData(int[][] data) {
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				board2048[i][j] = data[i][j]; 
			}	
		}
	}
	
	/**
	 * 
	 * @return size of the board.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * 
	 * @return stack with scores.
	 */
	public Stack<Integer> getOld_score() {
		return old_score;
	}
	/**
	 * 
	 * @return stack with moves.
	 */
	public Stack<int[][]> getOld_moves() {
		return old_moves;
	}
	/**
	 * 
	 * @return true if continue after receive tile 2048.
	 */
	public boolean isCheck() {
		return check;
	}
	
	/**
	 * 
	 * @return score before the move.
	 */
	public int getTempScore() {
		return tempScore;
	}
	
	/**
	 * @return init game from begining and notify the presenter.
	 */
	@Override
	public void restartGame() {
		initGame();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * @return 1 move back.
	 */
	@Override
	public void undoMove() {
		if(!(old_moves.empty()) && !(old_score.empty())){		// we can check just one of the Stacks but i prefer to check both
			int [][] last_board2048		= new int[size][size];
			last_board2048 				= old_moves.pop();
			score 						= old_score.pop();
			for (int i = 0 ; i < size ; i++){
				for (int j = 0 ; j < size ; j++){
					board2048[i][j] = last_board2048[i][j];
				}	
			}
			setChanged();
			notifyObservers();
		}		
	}
	/**
	 * @return save the state of the board to txt file.
	 */
	@Override
	public void save(){
		old_moves.push(board2048);
		old_score.push(score);
		File file = new File(fileName);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write("" + old_moves.size());
			out.write("\n");
			for(int k = 0 ; k < old_moves.size() ; k++){
				int[][] gameArrayToWrite = old_moves.get(k);
				for(int i = 0; i < size ; i++ ){
					for(int j = 0 ; j < size; j++){
						out.write("" + gameArrayToWrite[i][j]);
						System.out.print(gameArrayToWrite[i][j]);
						out.write(",");
					}
					System.out.println();
				}
				if(k == old_moves.size() -1){
					System.out.println(1);
				}
				out.write("#" + old_score.get(k));
				System.out.println("score:" + old_score.get(k));
				System.out.println("scoreLast" + old_score.get(old_moves.size() -1));
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.println("io exception occured, Nevermore5");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return load a saved state from a txt file.
	 */
	@Override
	public void load() {
		File file = new File(fileName);
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			int numLines = 0;
			try {
				//ready() is to check if buffer is empty
				if(in.ready()){
					numLines = Integer.parseInt(in.readLine());
				}
			} catch (NumberFormatException e1) {
				System.out.println("NumberFormatException occured, Nevermore 6");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				System.out.println("io exception occured, Nevermore7");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				//ready() is to check if buffer is empty
				if(in.ready()){
				//here it contains each line from text that it reading from it
				String tempLine = "";
				//here it contains score for each line
				int tempScore = 0;
				old_moves.clear();
				old_score.clear();
				for(int k = 0; k < numLines; k++){
					int[][] tempArrayToAdd = new int[size][size];
					tempLine = in.readLine();
					System.out.println(tempLine);
					int i = 0;
					int j = 0;
					boolean flag;
					int indexRunner = 0;
					//run for 17 blocks from text, each ',' separate the block
					for(int z = 0 ; z < 17 ; z++){
						//numToAdd is variable that add number to right [i][j] in tempArrayToAdd
						int numToAdd = 0;
						String strNum = "";
						flag = true;
						while(flag){
							if(Character.toString(tempLine.charAt(indexRunner)).equals(",")){
								numToAdd = Integer.parseInt(strNum);
								indexRunner++;
								flag = false;
							}
							else if(Character.toString(tempLine.charAt(indexRunner)).equals("#")){
								++indexRunner;
								strNum = tempLine.substring(indexRunner);
								tempScore = Integer.parseInt(strNum);
								flag = false;
							}
							else {
								strNum += Character.toString(tempLine.charAt(indexRunner));
								indexRunner++;
							}
						}
						if(z < 16){
							tempArrayToAdd[i][j] = numToAdd;
						}
						j++;
						if(j == 4){
							j = 0;
							i++;
						}
						if(i == 4){
							i = 0;
						}
					}
					indexRunner = 0;
					//for display to show the board and right score
					if(k == numLines -1){
						board2048 = tempArrayToAdd;
						score = tempScore;
					}
					old_moves.push(tempArrayToAdd);
					old_score.push(tempScore);
				}
			//	score = old_score.pop();
				setChanged();
				notifyObservers();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("io exception occured, Nevermore1");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("FILE NOT FOUND EXCEPTION, Nevermore2");
			e.printStackTrace();
		}
		
	}
	/**
	 * @param num is get {0 for init game, 1 for up,2 for down,3 for right,4 for left, 5 for retart game, 6 for undo move, 
	 * 7 for save game, 8 for load game, 11 for continue the game after tile 2048, 12 for getAi for MyAlgo, 
	 * 13 for getAi for Minimax, 14 for hint}.
	 */
	@Override
	public void doUserCommand(int num) {
		command = num;
		switch(num){
		case 0:
			initGame();
			break;
		case 1:
			if(moveUp()){
				setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
				setChanged();
				notifyObservers();
			}
			break;
		case 2:
			if(moveDown()){
				setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
				setChanged();
				notifyObservers();
			}
			break;
		case 3:
			if(moveRight()){
				setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
				setChanged();
				notifyObservers();
			}
			break;
		case 4:
			if(moveLeft()){
				setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
				setChanged();
				notifyObservers();
			}
			break;
		case 5:
			restartGame();
			break;
		case 6:
			undoMove();
			break;
		case 7:
			save();
			break;
		case 8:
			load();
			undoMove();
			break;
		case 11:
			// continue the game after hit 2048.
			succeed = false;
			check 	= false;
			break;
		case 12:
			// MyAlgo run
			getAI(host, port);
			break;
		case 13:
			// Minimax run
			getAI(host, port); 
			break;
		case 14:
			// hint
			break;
		default:
			break;
		}
	}
	/**
	 * @return if Stuck true otherwise false.
	 */
	@Override
	public boolean isStuck(){
		if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			stop = true;
		}
		return stuck;
	}
	/**
	 *@return if reach tile 2048 true, otherwise false.
	 */
	@Override
	public boolean isSucceed() {
		return succeed;
	}
	
	/**
	 * mergeStuck()
	 * @return check if we stuck.
	 */
	// check if there is any possible merge
	// getFreeSpotsNum supplement this method so we able to check if we stuck
	public boolean mergeStuck() {
		// run this way because of the boundaries
		for(int i=0 ; i<size-1 ; i++){
			for(int j=0 ; j<size-1 ; j++){
				if(board2048[i][j] == board2048[i][j+1] || board2048[i][j] == board2048[i+1][j]){
					return false;
				}
			}
		}
		for(int j=0 ; j < 3 ; j++){
			if(board2048[3][j] == board2048[3][j+1] || board2048[j][3] == board2048[j+1][3]){
				return false;
			}
		}
		return true;
	}
	/**
	 * @return file name.
	 */
	@Override
	public String getFileName() {
		return fileName;
	}
	/**
	 * @return  set file name.
	 */
	@Override
	public void setFileName(String s) {
		fileName = s;
	}


	
	/**
	 * @param host the ip of the server we connecting to.
	 * @param port the port we connecting on.
	 * @return this method connecting to a server a get the auto solution for this game.
	 */
	
	// this method connecting to a server a get the auto solution for this game
	//method need to get which host to connect and what port to use and which solver will do that 

	@Override
	public void getAI(String host, int port) {
		stop = false;
		switch (command) {
		case 12:
			MyAlgoRun(2048);			
			break;
		case 13:
			MinimaxRun();
			break;
		case 14:
			// Hint !!!
			break;
		default:
			break;
		}
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Sending to sever that we want answer with minimax algorithm.
	 */
	// minimax algo running
	private void MinimaxRun(){
		int goal = 2048;
		int hTile = calcHighTile(board2048);
		sol = new Minimax();
		while((hTile < goal && (!stop))){		
			try{  
					
				Socket s = new Socket(host,port);  
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				oos.writeObject(board2048);  
				oos.writeObject(new String("Model - data - 2048 sent from the client"));  
				oos.writeObject(sol);  
				oos.writeObject(new String("Solver - "+sol.getClass().toString()+" sent from the client"));  
				oos.writeObject(new String("exit"));
				Object obj = ois.readObject();
					
				if(obj != null){
					if(obj instanceof Integer){
						Integer x = (Integer) obj;
						doUserCommand(x);
						System.out.println(x);
					}
				}			
					
					
				oos.close();    
				ois.close();
				s.close();  
					
				}
			catch(Exception e){
					
				System.out.println(e.getCause());
				System.out.println("EXCEPTION, Nevermore4");
				System.out.println(e);
				}
			}
	}
	
	
	
	/**
	 *Sending to sever that we want answer with MyAlgo algorithm.
	 * @param goal the goal you want to reach.
	 */
	private void MyAlgoRun(int goal){
		
		int hTile = calcHighTile(board2048);
		sol = new MyAlgo(goal);
		
		while((hTile < goal && (!stop))){		
			try{  
		
				Socket s = new Socket(host,port);  
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				
				oos.writeObject(board2048);  
				oos.writeObject(new String("Model - data - 2048 sent from the client"));  
				oos.writeObject(sol);  
				oos.writeObject(new String("Solver - "+sol.getClass().toString()+" sent from the client"));  
				oos.writeObject(new String("exit"));
				Object obj = ois.readObject();
				if(obj != null){
					if(obj instanceof Integer){
						Integer x = (Integer) obj;
						doUserCommand(x);
						System.out.println(x);						
					}
				}
				
				
				oos.close();    
				ois.close();
				s.close(); 		
			}
			catch(Exception e){
				
				System.out.println(e.getCause());
				System.out.println("EXCEPTION, Nevermore4");
				System.out.println(e);
				}  
		}
		}
	
	/**
	 * 
	 * @param data the board.
	 * @return highest tile.
	 */
	private int calcHighTile(int[][] data) {
		int hscore = 2;
		for(int i = 0 ; i < data.length; i++){
			for(int j = 0 ; j < data[0].length; j++){
				if(hscore < data[i][j]){
					hscore = data[i][j];
				}
			}
		}
		return hscore;
	}
	
	
	/*
	 * 		int [][] last_board2048		= new int[size][size];
		
		int k  =0 ;
		while(true){
			
			for (int i = 0 ; i < size ; i++){
				for (int j = 0 ; j < size ; j++){
					last_board2048[i][j] = board2048[i][j];
				}	
			}
			doUserCommand(2);
			doUserCommand(3);	
			if(!boardChanged(board2048, last_board2048)){
				break;
			}
			System.out.println(k++);
		}*/
	 
}
