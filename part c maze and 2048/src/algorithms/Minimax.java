package algorithms;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Game2048Model;
import model.Model;

public class Minimax implements Solver,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7715584375427671747L;
	private Integer cache_emptyCells;
	private String user = "USER";
	private String computer = "COMPUTER";
	
	public Minimax(){
		
	}
	
	@Override
	public int calculator(Model model) {
		
		System.out.println("calc");
		int depth = 7;
		
		Map<String,Integer> result = minimax(model, depth, user);
		
		
		int x = result.get("Direction");
		System.out.println(x);
		return x;
		
	}
	
	
	public int[][] copyArray(int[][] old){
		int[][] copiedArr = new int[old.length][old[0].length];
		for(int i = 0 ; i < old.length; i++){
			for(int j = 0 ; j < old[0].length; j++){
				copiedArr[i][j] = old[i][j];
			}
		}
		return copiedArr;
	}
	
	public Map<String,Integer> minimax(Model model, int depth, String player){
		Map<String,Integer> result = new HashMap<>();
		int bestDirection = -1;
		int bestScore = 0;
		
		
		
		if(depth == 0 || gameTerminated(model)){
			bestScore = heuristicScore(model.getScore(),getNumberOfEmptyCells(model.getData()),calculateClusteringScore(model.getData()));
		}
		
		
		//try
		/*
		if(gameTerminated(model)) {
            if(model.isSucceed()) {
                bestScore=Integer.MAX_VALUE; //highest possible score
            }
            else {
                bestScore=Math.min(model.getScore(), 1); //lowest possible score
            }
        }
        else if(depth==0) {
            bestScore=heuristicScore(model.getScore(),getNumberOfEmptyCells(model.getData()),calculateClusteringScore(model.getData()));
        }
        */
		else{
			if(player.equals("USER")){
				bestScore = Integer.MIN_VALUE;
				
				for(int i = 1; i < 4 ; i++){
					Model modelCopy = new Game2048Model((Game2048Model)model);
					modelCopy.doUserCommand(i);
					
					if(arrayEquals(modelCopy.getData(), model.getData())){
						continue;
					}
					
					Map<String, Integer> currentResult = minimax(modelCopy, depth - 1, computer);
					
					int currentScore = currentResult.get("Score");
                    if(currentScore > bestScore) { //maximize score
                        bestScore = currentScore;
                        bestDirection = i;
                    }
				}
			}
			else if(player.equals("USER")){
				bestScore = Integer.MAX_VALUE;
				
				List<Integer> moves = getEmptyCellIds(model.getData());
				
				if(moves.isEmpty()) {
                    bestScore = 0;
                }
                int[] possibleValues = {2, 4};
                
                int i,j;
                int[][] boardArray;
                
                for(Integer cellId : moves) {
                    i = cellId/model.getData().length;
                    j = cellId%model.getData()[0].length;

                    for(int value : possibleValues) {
                        Model modelCopy = new Game2048Model((Game2048Model)model);
                        setEmptyCell(modelCopy, i, j, value);

                        Map<String, Integer> currentResult = minimax(modelCopy, depth - 1, "USER");
                        int currentScore = currentResult.get("Score");
                        if(currentScore < bestScore) { //minimize best score
                            bestScore = currentScore;
                        }
                    }
                }
			}
		}
		System.out.println("Direction,Score" + "(" + bestDirection + "," +bestScore +")");
		result.put("Score", bestScore);
		result.put("Direction", bestDirection);
	    
	    return result;
		
	}
	
	public int heuristicScore(int actualScore, int numberOfEmptyCells, int clusteringScore) {
        int score = (int) (actualScore + Math.log(actualScore)*numberOfEmptyCells - clusteringScore);
        return Math.max(score, Math.min(actualScore, 1));
    }


	private int calculateClusteringScore(int[][] data) {
		int clusteringScore = 0;
        
        int[] neighbors = {-1,0,1};
        
        for(int i=0; i < data.length; ++i) {
            for(int j=0; j < data.length; ++j) {
                if(data[i][j] == 0) {
                    continue; //ignore empty cells
                }
                
                //clusteringScore-=boardArray[i][j];
                
                //for every pixel find the distance from each neightbors
                int numOfNeighbors = 0;
                int sum = 0;
                for(int k : neighbors) {
                    int x = i + k;
                    if(x < 0 || x >= data.length) {
                        continue;
                    }
                    for(int l : neighbors) {
                        int y = j + l;
                        if(y < 0 || y >= data.length) {
                            continue;
                        }
                        
                        if(data[x][y] > 0) {
                            ++numOfNeighbors;
                            sum += Math.abs(data[i][j] - data[x][y]);
                        }
                        
                    }
                }
                
                clusteringScore+=sum/numOfNeighbors;
            }
        }
        
        return clusteringScore;
	}


	public int getNumberOfEmptyCells(int[][] data) {
        if(cache_emptyCells==null) {
            cache_emptyCells = getEmptyCellIds(data).size();
        }
        return cache_emptyCells;
    }
		
		
	public void setEmptyCell(Model model, int i, int j, int value) {
	       if(model.getData()[i][j] == 0) {
	    	   model.getData()[i][j] = value;
	           cache_emptyCells = null;
	       }
	}

	private List<Integer> getEmptyCellIds(int[][] data) {
		List<Integer> cellList = new ArrayList<>();
        
        for(int i=0; i < data.length; ++i) {
            for(int j=0; j < data[0].length; ++j) {
                if(data[i][j] == 0) {
                    cellList.add(data.length * i + j);
                }
            }
        }
        
        return cellList;
	}


	private boolean gameTerminated(Model model) {
		
		boolean stuckUp = false;
		boolean stuckDown = false;
		boolean stuckLeft = false;
		boolean stuckRight = false;
		
		int[][] temp = copyArray(model.getData());
		Model modelCopy1 = new Game2048Model((Game2048Model)model);
		int[][] copyArrayModel1 = copyArray(modelCopy1.getData());
		Model modelCopy2 = new Game2048Model((Game2048Model)model);
		int[][] copyArrayModel2 = copyArray(modelCopy1.getData());
		Model modelCopy3 = new Game2048Model((Game2048Model)model);
		int[][] copyArrayModel3 = copyArray(modelCopy1.getData());
		Model modelCopy4 = new Game2048Model((Game2048Model)model);
		int[][] copyArrayModel4 = copyArray(modelCopy1.getData());
		
		//check if win or if there empty cell
		for(int i = 0; i < temp.length; i++){
			for(int j = 0; j < temp[0].length; j++){
				if(temp[i][j] == 2048){
					return true;
				}
				if(temp[i][j] == 0){
					return false;
				}
			}
		}
		
		//check if stuck for all sides (up,down,Right,Left)
		modelCopy1.doUserCommand(1);
		if(arrayEquals(modelCopy1.getData(), copyArrayModel1)){
			stuckUp = true;
		}
		modelCopy2.doUserCommand(2);
		if(arrayEquals(modelCopy2.getData(), copyArrayModel2)){
			stuckDown = true;
		}
		modelCopy3.doUserCommand(3);
		if(arrayEquals(modelCopy3.getData(), copyArrayModel3)){
			stuckRight = true;
		}
		modelCopy4.doUserCommand(4);
		if(arrayEquals(modelCopy4.getData(), copyArrayModel4)){
			stuckLeft = true;
		}
		
		return (stuckUp && stuckDown && stuckRight && stuckLeft);
	}
	
	public boolean arrayEquals(int[][] arr1, int[][] arr2){
		for(int i = 0 ; i < arr1.length; i++){
			for(int j = 0; j < arr1[0].length; j++){
				if(arr1[i][j] != arr2[i][j]){
					return false;
				}
			}
		}
		return true;
	}
}


