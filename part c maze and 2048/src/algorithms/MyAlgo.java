package algorithms;
import java.io.Serializable;

import model.Model;

public class MyAlgo implements Solver, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8168102632311639749L;

	
	public MyAlgo(){
		
	}
	
	@Override
	public int calculator(Model model) {
		
		int [][]data = copyArray(model.getData());
		
		
		return 0;
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
	
}
