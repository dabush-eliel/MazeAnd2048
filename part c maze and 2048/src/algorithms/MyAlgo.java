package algorithms;
import java.io.Serializable;
import java.util.List;

import model.Game2048Model;
import model.Model;

public class MyAlgo implements Solver, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8168102632311639749L;
	private int upVal;
	private int downVal;
	private int rightVal;
	private int leftVal;				
	private final int UP 		= 1;
	private final int DOWN		= 2;
	private final int RIGHT 	= 3;
	private final int LEFT 		= 4;
	private int bestMove 		= 0;
	private final int GOAL;
	boolean stop = false;
	
	// this algo avoid  moving up
	// get the goal value 
	public MyAlgo(int goal){
		this.GOAL = goal;
	}
	
	@Override
	public List<Object> calculator(Model model) {
		
		System.out.println("MyAlgo Calculator");
		int [][]data = copyArray(model.getData());
		int hTile = calcHighScore(data);
		
		// we set goal value in the c'tor. usually 2048. 
		while((hTile < Math.pow(2, GOAL)) && (!stop)){
			System.out.println("in while");
			downVal 	= 3;
			rightVal 	= 2;
			leftVal 	= 1;
			
			if(newBoard(data) && model.getScore() == 0){
				int i = 0;
				while(true){
					data = copyArray(model.getData());
					model.doUserCommand(DOWN);
					model.doUserCommand(RIGHT);	
					if(!boardChanged(data, model.getData())){
						break;
					}
					System.out.println(i++);
				}
				System.out.println("before break");
				break;
			}
			stop = true;
			
			// Shouldn't been printed
			System.out.println("after break");
			if(model.isStuck()){
				stop = true;
				break;
			}

			
			// do it for 3 possible moves : down - right - left --> (at this order) - we avoid move up
			for(int i=2 ; i<=4 ; i++){
				Model modelCopy = new Game2048Model((Game2048Model)model);
				modelCopy.doUserCommand(i);
				
				if(!boardChanged(modelCopy.getData(), model.getData())){
					continue;
				}
				
				int score = (modelCopy.getScore() - model.getScore());
				
				if(avoidedMovePoss(modelCopy.getData())){
					continue;
				}else{
					switch(i){
					case 2:
						downVal += 50;
						break;
					case 3:
						rightVal += 50;
						break;
					case 4:
						leftVal += 50;
						break;
					}
				}
				
				
			}
			
			int val = Math.max(downVal, Math.max(rightVal,leftVal));
			model.doUserCommand(val);			
		}
		
		
		return null;
	}
	
	
	// check if new tile can block our move - block means we can move only UP - our avoided move
	private boolean avoidedMovePoss(int [][]data) {
		
		return false;
	}

	// there are only 2 tiles with the 2or4 value on them 
	private boolean newBoard(int[][] data) {
		int counter = 0;
		for(int i=0 ; i<data.length ; i++ ){
			for(int j=0 ; j<data[0].length ; j++){
				if(data[i][j] != 0 && data[i][j] != 2 && data[i][j] != 4){
					return false;
				}else if(data[i][j] != 0){
					counter++;
				}
			}
		}
		if(counter != 2){
			return false;
		}
		return true;
	}

	private int calcHighScore(int[][] data) {
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

	private int[][] copyArray (int[][] arr){
		int[][] copiedArr = new int[arr.length][arr[0].length];
		for(int i = 0 ; i < arr.length; i++){
			for(int j = 0 ; j < arr[0].length; j++){
				copiedArr[i][j] = arr[i][j];
			}
		}
		return copiedArr;
	}
	
	private boolean boardChanged(int [][]src, int [][]dst) {	
		int size = Math.min(src.length, src[0].length);		
		for (int i = 0 ; i < size ; i++){
			for (int j = 0 ; j < size ; j++){
				if(src[i][j] != dst[i][j]){
					return true;
				}
			}
		}	
		return false;
	}
	
}
