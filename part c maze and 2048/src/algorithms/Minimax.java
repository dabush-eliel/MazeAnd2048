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
	public List<Object> calculator(Model model) {
		
		System.out.println("calc");
		int depth = 2;
		
		List<Object> modelsAndHints = new ArrayList<>();
		
		Model[] resultArray = new Model[depth];
		String[] hints = new String[depth];
		Map<String,Model> result = new HashMap<>();
		result = minimax(model, depth, user, result);
		String currentPlayer = "USER";
		
		
		
		
		
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0 ; i < depth; i++){
			if(currentPlayer.equals("USER")){
				for(int j = 1 ; j < depth ; j++){
					Model copyModel1 = new Game2048Model((Game2048Model)model);
					int[][] copyArr1 = new int[copyModel1.getData().length][copyModel1.getData()[0].length];
					copyArr1 = copyModel1.getData();
					String string1 = currentPlayer + "," + j + "," + (depth - i) + "," + "UP";
					//System.out.println(string1 + ", " + result.containsKey(string1));
					if(result.containsKey(string1)){
						resultArray[i] = result.get(string1);
						
						hints[i] = "UP";
						
						copyModel1.doUserCommand(1);
						boolean bool = arrayEquals(copyModel1.getData(), copyArr1);
						System.out.println("should be DOWN" + ",current Bool is:" + bool);
						if(arrayEquals(copyModel1.getData(), copyArr1)){
							hints[i] = "DOWN";
						}
					}
				}
				for(int j = 1 ; j < depth ; j++){
					Model copyModel2 = new Game2048Model((Game2048Model)model);
					int[][] copyArr2 = new int[copyModel2.getData().length][copyModel2.getData()[0].length];
					copyArr2 = copyModel2.getData();
					String string2 = currentPlayer + "," + j + "," + (depth - i) + "," + "DOWN";
					//System.out.println(string2 + ", " + result.containsKey(string2));
					if(result.containsKey(string2)){
						resultArray[i] = result.get(string2);
						
						hints[i] = "DOWN";
						copyModel2.doUserCommand(2);
						boolean bool = arrayEquals(copyModel2.getData(), copyArr2);
						System.out.println("should be UP" + ",current Bool is:" + bool);
						if(arrayEquals(copyModel2.getData(), copyArr2)){
							hints[i] = "UP";
						}
					}
				}
				for(int j = 1 ; j < depth ; j++){
					Model copyModel3 = new Game2048Model((Game2048Model)model);
					int[][] copyArr3 = new int[copyModel3.getData().length][copyModel3.getData()[0].length];
					copyArr3 = copyModel3.getData();
					String string3 = currentPlayer + "," + j + "," + (depth - i) + "," + "RIGHT";
					//System.out.println(string3 + ", " + result.containsKey(string3));
					if(result.containsKey(string3)){
						resultArray[i] = result.get(string3);
						
						hints[i] = "RIGHT";
						copyModel3.doUserCommand(3);
						boolean bool = arrayEquals(copyModel3.getData(), copyArr3);
						System.out.println("should be LEFT" + ",current Bool is:" + bool);
						if(arrayEquals(copyModel3.getData(), copyArr3)){
							hints[i] = "LEFT";
						}
					}
				}
				for(int j = 1 ; j < depth ; j++){
					Model copyModel4 = new Game2048Model((Game2048Model)model);
					int[][] copyArr4 = new int[copyModel4.getData().length][copyModel4.getData()[0].length];
					copyArr4 = copyModel4.getData();
					String string4 = currentPlayer + "," + j + "," + (depth - i) + "," + "LEFT";
					//System.out.println(string4 + ", "+ result.containsKey(string4));
					if(result.containsKey(string4)){
						resultArray[i] = result.get(string4);
						
						hints[i] = "LEFT";
						copyModel4.doUserCommand(4);
						boolean bool = arrayEquals(copyModel4.getData(), copyArr4);
						System.out.println("should be RIGHT" + ",current Bool is:" + bool);
						if(arrayEquals(copyModel4.getData(), copyArr4)){
							hints[i] = "RIGHT";
						}
					}
				}
				currentPlayer = "COMPUTER";
			}
			else if(currentPlayer.equals("COMPUTER")){
				resultArray[i] = result.get(currentPlayer + "," + i + "," + (depth - i));
				hints[i] = "COMPUTER";
				currentPlayer = "USER";
			}
			
		}
		
		
		modelsAndHints.add(resultArray);
		modelsAndHints.add(hints);
		return modelsAndHints;
		
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
	
	public Map<String,Model> minimax(Model model, int depth, String player, Map<String,Model> result){
		if(depth == 0 || gameTerminated(model)){
			return result;
		}
		else{
			if(player.equals("USER")){
				for(int i = 1; i < 4 ; i++){
					Model modelCopy = new Game2048Model((Game2048Model)model);
					String bestMoveIs = huristicksCalculator(modelCopy);
					modelCopy.doUserCommand(i);
					if(arrayEquals(modelCopy.getData(), model.getData())){
						continue;
					}
					minimax(modelCopy, depth - 1, computer, result);
					String string = "USER"+ "," + i + "," + depth + "," + bestMoveIs;
				
                    result.put(string, modelCopy);
                   // System.out.println("added:" + string + "," + result.containsKey(string));
                   
				}
			}
			else if(player.equals("COMPUTER")){
				List<Integer> moves = getEmptyCellIds(model.getData());
                int[] possibleValues = {2, 4};
                int i,j;
                for(Integer cellId : moves) {
                    i = cellId/model.getData().length;
                    j = cellId%model.getData()[0].length;
                    for(int value : possibleValues) {
                        Model modelCopy = new Game2048Model((Game2048Model)model);
                        setEmptyCell(modelCopy, i, j, value);
                        minimax(modelCopy, depth - 1, "USER", result);
                        String string = "COMPUTER" + "," + i + "," + depth;
                        
                        result.put(string, modelCopy);
                       // System.out.println("added:" + string + "," + result.containsKey(string));
                        
                    }
                }
			}
		}
	    
	    return result;
		
	}
	
	/**
	 * 
	 * 
	 * for received board check where to move for get more empty cells
	 * 
	 */
	private String huristicksCalculator(Model model) {
		String theHint = "";
		
		Model modelCopy1 = new Game2048Model((Game2048Model)model);
		Model modelCopy2 = new Game2048Model((Game2048Model)model);
		Model modelCopy3 = new Game2048Model((Game2048Model)model);
		Model modelCopy4 = new Game2048Model((Game2048Model)model);
		
		int max = 0;
		
		//4 directions
		
		//UP
		modelCopy1.doUserCommand(1);
		//DOWN
		modelCopy2.doUserCommand(2);
		//RIGHT
		modelCopy3.doUserCommand(3);
		//LEFT
		modelCopy4.doUserCommand(4);
		
		
		//get the best move
		int numOfEmptyCells = numOfemptyCellsFoo(modelCopy1);
		if(numOfEmptyCells > max){
			max = numOfEmptyCells;
			theHint = "UP";
		}
		numOfEmptyCells = numOfemptyCellsFoo(modelCopy2);
		if(numOfEmptyCells > max){
			max = numOfEmptyCells;
			theHint = "DOWN";
		}
		numOfEmptyCells = numOfemptyCellsFoo(modelCopy3);
		if(numOfEmptyCells > max){
			max = numOfEmptyCells;
			theHint = "RIGHT";
		}
		numOfEmptyCells = numOfemptyCellsFoo(modelCopy4);
		if(numOfEmptyCells > max){
			max = numOfEmptyCells;
			theHint = "LEFT";
		}
		
		
		return theHint;
	}
	
	
	private int numOfemptyCellsFoo(Model model){
		int numOfEmptyCells = 0;
		for(int i = 0; i < model.getData().length ; i++){
			for(int j = 0; j < model.getData().length;j++){
				if(model.getData()[i][j] == 0){
					++numOfEmptyCells;
				}
			}
		}
		return numOfEmptyCells;
	}

	private int[][] copyTheBoard(int[][] data) {
		int[][] copyBoard = new int[data.length][data[0].length];
		for(int i = 0; i < data.length ; i++){
			for(int j = 0; j < data[0].length;j++){
				copyBoard[i][j] = data[i][j];
			}
		}
		return copyBoard;
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
					System.out.println("its arr1 value:" + arr1[i][j] + "," + "its arr2 value:" + arr2[i][j]);
					return false;
				}
			}
		}
		return true;
	}
}


