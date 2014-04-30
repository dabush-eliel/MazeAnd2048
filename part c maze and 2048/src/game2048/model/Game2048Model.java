package game2048.model;

import java.util.Observable;
import java.util.Random;
import java.util.Stack;



public class Game2048Model extends Observable implements Model {
	private final int size 				= 4;				//	size of row / column (same size for us)
	private int score					= 0;
	private int [][] board2048 			= new int[size][size];
	private Stack <Integer> old_score 	= new Stack <Integer>();
	private Stack <int[][]> old_moves 	= new Stack <int[][]>();
	private boolean succeed				= false;
	private boolean stuck 				= false;
	private boolean check				= true;
	
	public Game2048Model() {
		initGame();	
	}
	
	public void initGame(){
		for(int i = 0 ; i < size ; i++){
			for(int j = 0 ; j < size ; j++){
				this.board2048[i][j]	= 	0;
			}
		}
	//	board2048[1][2] = 1024;
	//	board2048[2][3] = 1024;
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

	// get random value - 2 or 4
	private int squareVal(){
		Random rand = new Random();
	/*	int probs[] = new int[10];
		for (int i 	= 0 ; i < 9 ; i++){
			probs[i] = 2;
		}
		for (int i 	= 9 ; i<10 ; i++){
			probs[i] = 4;
		}*/
		int x = rand.nextInt(10);
		if(x < 9){
			return 2;
		}else{
			return 4;
		}
	//	return probs[rand.nextInt(10)];
	}
	// get where to put the new square 
	private int squarePlace(int num){		// get number of free possible squares
		if(num == 0){
			return 0;
		}
		Random rand = new Random();
		return (rand.nextInt(num)+1);
	}

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
	
	@Override
	public void moveUp() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
		
		old_score.push(score);
		
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
			old_moves.push(last_board2048);
			setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
			setChanged();
			notifyObservers();
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			setChanged();
			notifyObservers();
		}
	}
	

	@Override
	public void moveDown() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		old_score.push(score);
		
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
			old_moves.push(last_board2048);
			setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
			setChanged();
			notifyObservers();
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void moveRight() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		old_score.push(score);
		
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
			old_moves.push(last_board2048);
			setSquare(squareVal(),squarePlace(getFreeSpotsNum()));
			setChanged();
			notifyObservers();
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void moveLeft() {
		int []vals = new int[size];
		int [][] last_board2048		= new int[size][size];
		
		// saving the current state - to compare if the action illegal and to save the state before the last move .
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				last_board2048[i][j] = board2048[i][j];
			}	
		}
	
		old_score.push(score);
		
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
			old_moves.push(last_board2048);
			setSquare(squareVal(),squarePlace(getFreeSpotsNum()));	
			setChanged();
			notifyObservers();
		}else if(getFreeSpotsNum() == 0 && mergeStuck()){
			stuck = true;
			setChanged();
			notifyObservers();
		}
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
	
	@Override
	public int getScore(){
		return this.score;
	}
	
	@Override
	public int[][] getBoard() {
		return getBoard2048();
	}
	
	private int[][] getBoard2048() {
		return board2048;
	}

	@Override
	public void restartGame() {
		initGame();
		setChanged();
		notifyObservers();
	}

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

	@Override
	public void doUserCommand(int num) {
		switch(num){
		case 0:
			initGame();
			break;
		case 1:
			moveUp();
			break;
		case 2:
			moveDown();
			break;
		case 3:
			moveRight();
			break;
		case 4:
			moveLeft();
			break;
		case 5:
			restartGame();
			break;
		case 6:
			undoMove();
			break;
		case 11:
			succeed = false;
			check 	= false;
			break;
		default:
			break;
			
//		case 7:		save
//		case 8:		load			
//		case 9:		exit
		}
	}

	@Override
	public boolean isStuck(){
		return stuck;
	}
	
	@Override
	public boolean isSucceed() {
		return succeed;
	}

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

}
