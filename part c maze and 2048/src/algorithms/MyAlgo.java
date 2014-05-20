package algorithms;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private int [][] cellsVals;
	private final int UP 		= 1;
	private final int DOWN		= 2;
	private final int RIGHT 	= 3;
	private final int LEFT 		= 4;
	private int bestMove 		= 0;
	private int depth 			= 8;
	private String player		= "USER";
	private final int GOAL;
	boolean stop = false;
	
	
	// this algo avoid  moving up
	// get the goal value 
	public MyAlgo(int goal){
		this.GOAL = goal;
	}
	
	@Override
	public int calculator(Model model) {
		
		System.out.println("MyAlgo Calculator");
		
		int [][]data = copyArray(model.getData());
		int hTile = calcHighTile(data);		
		
		for(int i = 1 ; i < 5 ; i++){
			Model modelCopy = new Game2048Model( (Game2048Model) model);
			if(i == 1){
				if(modelCopy.moveUp()){
					upVal = heuristicCalc(modelCopy);
				}else{
					upVal = Integer.MAX_VALUE;
				}
				
			}else if (i == 2){
				if(modelCopy.moveDown()){
					downVal = heuristicCalc(modelCopy);
				}else{
					downVal = Integer.MAX_VALUE;
				}
				
			}else if(i == 3){
				if(modelCopy.moveRight()){
					rightVal = heuristicCalc(modelCopy);
				}else{
					rightVal = Integer.MAX_VALUE;
				}
			}else if(i == 4){
				if(modelCopy.moveLeft()){
					leftVal = heuristicCalc(modelCopy);
				}else{
					leftVal = Integer.MAX_VALUE;
				}
			}
		}
	
		int min = upVal;
		int command = UP;
		
		if(min > rightVal){
			min = rightVal;
			command = RIGHT;
		}
		if(min > downVal){
			min = downVal;
			command = DOWN;
		}
		if(min > leftVal){
			min = leftVal;
			command = LEFT;
		}
			
		
		//Math.min(upVal, Math.min(downVal, Math.min(rightVal, leftVal)));
		return command; 
	}

	
	private int heuristicCalc(Model m) {
		int val = 0;
		
		val += cellsWeightHeuristic(m);
		
		
		return val;
	}
	
	
	
	private int cellsWeightHeuristic(Model m) {
		
		int weight = 0;
		int val = 1000;
		int [][]data = copyArray(m.getData());
		
		cellsVals = new int[m.getData().length][(m.getData())[0].length];
		for(int i = 0; i<cellsVals.length; i++){
			for(int j = 0; j<cellsVals[0].length; j++){
				cellsVals[j][i] = val*(j+1);  
			}
			val = val/10;
		}
		
		for(int i = 0; i<cellsVals.length; i++){
			for(int j = 0; j<cellsVals[0].length; j++){
				weight += cellsVals[i][j] * data[i][j];  
			}
		}

		return weight;
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
