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

import algorithms.AlphaBeta;
import algorithms.Solver;

/**
 * This Class, is a Model of the Game 2048.
 * @author Eliel Dabush and Oleg Glizerin 
 *
 */
 
public class Game2048Model extends Observable implements Model, Serializable {
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 8774547151582184720L;
	
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
	 
	private final int size;
	private int score					= 0;
	private int [][] board2048;
	private Stack <Integer> old_score 	= new Stack <Integer>();
	private Stack <int[][]> old_moves 	= new Stack <int[][]>();
	private boolean succeed				= false;
	private boolean stuck 				= false;
	private boolean check				= true;
	private boolean stop 				= false;
	private int tempScore				= 0;
	private String fileName;
	private String host					= "localhost";
	private int port					= 2032;
	private Solver sol;
	@SuppressWarnings("unused")
	private int command;
	private int hint					= 0;
	private int hintsNum				= Integer.MAX_VALUE;
	private int depth					= 7;
	
	// for the main script:
	private int sqr1val;
	private int sqr2val;
	private int sqr1plc;
	private int sqr2plc;
	
	
	/**
	 * Default C'tor
	 * 
	 */
	public Game2048Model(){
		this.size 	= 4;
		board2048 	= new int[size][size];
		this.sqr1val = squareVal();
		this.sqr2val = squareVal();
		this.sqr1plc = squarePlace(getFreeSpotsNum());
		this.sqr2plc = squarePlace(getFreeSpotsNum());
		initGame();	
	}
	/**
	 * c'tor for the tests main (algorithms compare)
	 * @param size
	 * @param sqr1val
	 * @param sqr1plc
	 * @param sqr2val
	 * @param sqr2plc
	 */
	public Game2048Model(int size, int sqr1val, int sqr1plc,int sqr2val, int sqr2plc) {
		this.size 	= size;
		board2048 	= new int[size][size];
		this.sqr1val = sqr1val;
		this.sqr2val = sqr2val;
		this.sqr1plc = sqr1plc;
		this.sqr2plc = sqr2plc;
		initGame();	
	}
	
	/**
	 * copy c'tor
	 * @param gm
	 */
	public Game2048Model(Game2048Model gm) {
		
		this.size = gm.size;
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
		board2048 	= new int[size][size];
		for(int i=0 ; i<size ; i++){
			for(int j=0 ; j<size ; j++){
				this.board2048[i][j] = gm.board2048[i][j];
			}
		}
	}

	/**
	 *  Initialize a new game with 2 random tiles.
	 */
	public void initGame(){
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				this.board2048[i][j]	= 	0;
			}
		}
		// for test
		//board2048[1][2] = 1024;
		//board2048[2][3] = 1024;
		
		// initialize 2 random Squares with the value: 2 OR 4 --> 90% for 2 and 10% for 4
		setSquare(sqr1val,sqr1plc);
		setSquare(sqr2val,sqr2plc);	
		score = 0;
		old_moves.clear();
		old_score.clear();
		succeed 	= false;
		stuck 		= false;
		check 		= true;
		hint		= 0;
		hintsNum	= Integer.MAX_VALUE;
		depth		= 7;
	}

	/**
	 * 
	 * @return int - value 2 with 90% of success and value 4 with 10% of success.
	 */
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
	private int squarePlace(int num){		// get number of free possible squares
		if(num == 0){
			return 0;
		}
		Random rand = new Random();
		return (rand.nextInt(num)+1);
	}


	/**
	 * Sets new tile in the board 
	 * @param sqrVal - value to use (2or4)
	 * @param sqrPlc - on which cell 
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
	 * Perform a move Up for the board, if movement succeed - it returns true 
	 * @return Move Up.
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
	 * Perform a move Down for the board, if movement succeed - it returns true 
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
	 * Perform a move Right for the board, if movement succeed - it returns true 
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
	 * Perform a move Left for the board, if movement succeed - it returns true 
	 * @return Move Left.
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
	
	/**
	* Method to skip the '0' when we perform a board movement.
	**/
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
	
	/**
	* Method to merge val when we perform a board movement.
	**/
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
	 * Check if two boards have the same values.
	 * @param src
	 * @param dst
	 * @return true\false
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
	*
	*	Methods we need to override because implements the Model interface
	*
	**/
	@Override
	public boolean moveUpRight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveUpLeft() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveDownRight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveDownLeft() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * @return game score
	 */
	@Override
	public int getScore(){
		return this.score;
	}
	
	/**
	 * @return game board data
	*/
	@Override
	public int[][] getData() {
		return board2048;
	}

	/**
	 * @return size of the board
	 */

	public int getSize() {
		return size;
	}

	/**
	 * @return stack with all of our scores.
	 */
	public Stack<Integer> getOld_score() {
		return old_score;
	}

	/**
	 * @return stack with all of our old moves (board states).
	 */
	public Stack<int[][]> getOld_moves() {
		return old_moves;
	}

	/**
	* 'check', for keep playing normal after won (without a popup window after every move)
	* @return true if user continue after receive tile 2048.
	*/
	public boolean isCheck() {
		return check;
	}

	public int getTempScore() {
		return tempScore;
	}

	/**
	* @return a number that represent a movement
	*/
	public int getHint() {
		return hint;
	}

	/**
	 * initialize a new game from begining and notify the presenter.
	 */	
	@Override
	public void restartGame() {
		initGame();
		setChanged();
		notifyObservers();
	}

	/**
	 * @return make undo to the last move the user did (by using the stacks) - unlimited function
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
	 * save the current state of the game to text file.
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
			e.printStackTrace();
		}
	}
	
	/**
	 *  loading a saved game state from a text file.
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
	 * We receive a integer that represent a command from the 'Presenter' after the user did an action.
	 * @param  num 
	 * method receive number that represent - { 0 for init game, 1 for up,2 for down,3 for right,4 for left, 5 for restart game, 6 for undo move, 
	 * 7 for save game, 8 for load game, 11 for continue the game after tile 2048, 12 stop AI running, 
	 * 13 getAI with Minimax-Alphabeta }.
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
			stop = true;
			break;
		case 13:
			// minimax - alpha beta -  run
			getAI(host, port); 
			break;
		default:
			break;
		}
	}

	/**
	 * @return true if we stuck (we lost) otherwise false.
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
	*	@return true if we reach to 2048, otherwise return false.
	*/
	@Override
	public boolean isSucceed() {
		return succeed;
	}

	/**
	*	
	* check if there is any possible merge
	* getFreeSpotsNum supplement this method so we able to check if we stuck
	* @return true if we can't perform any merge on the board.
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
	 * set file name.
	 */
	@Override
	public void setFileName(String s) {
		fileName = s;
	}

	/**
	 * this method connecting to a server a get the auto solution for this game
	 * method need to get which host to connect and what port to use and which solver will do that 
	 * @param host the ip of the server we connecting to.
	 * @param port the port we connecting on.
	 */
	@Override
	public void getAI(final String host, final int port) {
		stop = false;

		Thread solverT = new Thread(new Runnable() {
			
			@Override
			public void run() {
				AlphaBetaRun(host,port);
				//MinimaxRun();
			}
		});
		solverT.start();
			
		setChanged();
		notifyObservers();
	}
	
	
	/**
	* set how many hints the user wants
	*/
	@Override
	public void setHintsNum(int num) {
		this.hintsNum = num;
	}

	/**
	* set how deep the user wants the hints to be
	*/
	@Override
	public void setDepth(int num) {
		this.depth = num;
	}
	
	@Override
	public int getDepth() {
		return depth;
	}



	/**
	 *  Create connection with a remote server and asks for a solution for the 2048 game by AlphaBeta pruning algorithm.
	 *  connection is open until the algorithm done (win \ stuck \ user stopped).
	 */
	private void AlphaBetaRun(String host, int port) {
		
		sol = new AlphaBeta();

		try{
			Socket s = new Socket(host,port);  
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			oos.writeObject(new String("Solver-Alpha-Beta "+sol.getClass().toString()+" sent from the client"));
			oos.writeObject(sol); 
		
		while(!isSucceed() && (!stop) && hintsNum>0) {
		
			oos.writeObject(new String("Model-2048 sent from the client"));
			oos.writeObject(new Game2048Model(this));
			
			Object obj = ois.readObject();
			
			if(obj != null){
				if(obj instanceof Integer){
					Integer x  = (Integer) obj;
					hint = x.intValue();
					System.out.println("Hint: "+hint);
					doUserCommand(hint);
				}	
			}
			--hintsNum;
		}
			
		oos.writeObject(new String("exit"));
		
			oos.close();    
			ois.close();
			s.close(); 
			
		}catch (Exception e) {
			System.out.println("AB "+ e.getMessage());
		}
			
		hintsNum 	= Integer.MAX_VALUE;
		depth 		= 7;
		System.out.println("done AlphaBeta.");
	}
		

// 	minimax algo running
//	private void MinimaxRun(){
//		
//		sol = new Minimax();
//		
//		try{  
//			
//			Socket s = new Socket(host,port);  
//			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
//			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//			
//			System.out.println(1);
//	
//			oos.writeObject(new Game2048Model(this));  
//			oos.writeObject(new String("Model - 2048 sent from the client"));  
//			oos.writeObject(sol);  
//			oos.writeObject(new String("Solver - "+sol.getClass().toString()+" sent from the client"));  
//			oos.writeObject(new String("exit"));
//			Object obj = ois.readObject();
//			
//			if(obj != null){
//				if(obj instanceof List<?>){
//					List<Object> modelsAndHints = (List<Object>) obj;
//					String[] hints = (String[]) modelsAndHints.get(0);
//					System.out.println(hints.length);
//					for(int i = 0 ; i < hints.length ; i++){
//						if(hints[i] != null){
//							System.out.println(hints[i]);
//							
//							//try
//							Model modelCopy1 = new Game2048Model(this);
//							modelCopy1.doUserCommand(1);
//							Model modelCopy2 = new Game2048Model(this);
//							modelCopy1.doUserCommand(1);
//							Model modelCopy3 = new Game2048Model(this);
//							modelCopy1.doUserCommand(1);
//							Model modelCopy4 = new Game2048Model(this);
//							modelCopy1.doUserCommand(1);
//							
//							switch (hints[i]) {
//							case "UP":
//								if(boardChanged(modelCopy1.getData(), board2048)){
//									doUserCommand(1);
//								}
//								Random rand1 = new Random();
//								int x1 = rand1.nextInt(3)+1;
//								doUserCommand(x1);
//								break;
//							case "DOWN":
//								if(boardChanged(modelCopy2.getData(), board2048)){
//									doUserCommand(2);
//								}
//								Random rand2 = new Random();
//								int x2 = rand2.nextInt(3)+1;
//								doUserCommand(x2);
//								break;
//							case "RIGHT":
//								if(boardChanged(modelCopy3.getData(), board2048)){
//									doUserCommand(3);
//								}
//								Random rand3 = new Random();
//								int x3 = rand3.nextInt(3)+1;
//								doUserCommand(x3);
//								break;
//							case "LEFT":
//								if(boardChanged(modelCopy4.getData(), board2048)){
//									doUserCommand(4);
//								}
//								Random rand4 = new Random();
//								int x4 = rand4.nextInt(3)+1;
//								doUserCommand(x4);
//								break;
//							case "COMPUTER":
//								Random rand5 = new Random();
//								int x5 = rand5.nextInt(3)+1;
//								doUserCommand(x5);
//								break;
//							}
//						}
//					}
//				}
//			}			
//			
//			oos.close();    
//			ois.close();
//			s.close();  
//			
//			}
//		catch(Exception e){
//			
//			System.out.println(e.getCause());
//			System.out.println("EXCEPTION, Nevermore4");
//			System.out.println(e);
//			}
//	}
	
	
	// for testing heuristics in MyAlgo 
//	private void MyAlgoRun(int goal){
//		
//		int hTile = calcHighTile(board2048);
//		sol = new MyAlgo(goal);
//		
//		while((hTile < goal) && (!stop) && (hintsNum > 0)){		
//			try{  		
//				Socket s = new Socket(host,port);  
//				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
//				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//				
//				oos.writeObject(new Game2048Model(this));  
//				oos.writeObject(new String("Model - 2048 sent from the client"));  
//				oos.writeObject(sol);  
//				oos.writeObject(new String("Solver - "+sol.getClass().toString()+" sent from the client"));  
//				oos.writeObject(new String("exit"));
//				Object obj = ois.readObject();
//				if(obj != null){
//					if(obj instanceof Integer){
//						Integer x = (Integer) obj;
//						System.out.println(x);
//						doUserCommand(x.intValue());						
//					}
//				}	
//				
//				hTile = calcHighTile(board2048);
//				
//				oos.close();    
//				ois.close();
//				s.close(); 		
//			}
//			catch(Exception e){
//				
//				System.out.println(e.getCause());
//				System.out.println("EXCEPTION, Nevermore4");
//				System.out.println(e);
//				}  
//			--hintsNum;
//		}
//		depth = 7;
//		hintsNum = Integer.MAX_VALUE;
//	}
//	
//	
//	private int calcHighTile(int[][] data) {
//		int hscore = 2;
//		for(int i = 0 ; i < data.length; i++){
//			for(int j = 0 ; j < data[0].length; j++){
//				if(hscore < data[i][j]){
//					hscore = data[i][j];
//				}
//			}
//		}
//		return hscore;
//	}
//
		
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
