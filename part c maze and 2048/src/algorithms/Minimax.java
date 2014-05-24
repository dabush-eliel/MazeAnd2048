package algorithms;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Game2048Model;
import model.Model;

public class Minimax implements Solver,Serializable{
	
	
	private static final long serialVersionUID = -7715584375427671747L;
	
	private String user = "USER";
	private String computer = "COMPUTER";
	
	private Integer cache_emptyCells = null;
	
	
	//the problem is in score need to fix, and sync exception 2 bugs tomorow..
	
	
	public Minimax(){
		
	}
	
	@Override
	public int calculator(Model model) {
		
		System.out.println("Minimax calculator started.");
		int depth = 7;
		
		Map<String, Integer> result ;// = new HashMap<>();
		result = alphabeta(model, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, user);

		
		Integer hint = (Integer) result.get("Direction");	
		System.out.println("get from minimax hint = " + hint);
		System.out.println("**FINISHED CALCULATOR.**");
		return hint;
		
	}
	
	
	public Map<String, Integer> alphabeta(Model model, int depth, int alpha, int beta, String player){
		
        Map<String, Integer> result = new HashMap<>();
        
        Integer bestDirection = null;
        int bestScore;
        
        if(gameTerminated(model)) {
            if(model.isSucceed()) {
                bestScore = Integer.MAX_VALUE; //highest possible score
            }
            else {
                bestScore = Math.min(model.getScore(), 1); //lowest possible score
            }
        }
        else if(depth == 0) {
            bestScore = heuristicScore(model.getScore(), numOfemptyCellsFoo(model), calculateClusteringScore(model.getData()));
        }
        else {
            if(player.equals(user)) {
                for(int i = 1; i < 5; i++) {
                    Model copyModel = new Game2048Model((Game2048Model)model);
                    int pointsBeforeMove = model.getScore();
                    int points = 0;
                    
                    if(i == 1){
                    	if(copyModel.moveUp()){
	                    	int pointsAfterMove = copyModel.getScore();
	                    	points = pointsAfterMove - pointsBeforeMove;
                    	}
                    }
                    else if(i == 2){
                    	if(copyModel.moveDown()){
	                    	int pointsAfterMove = copyModel.getScore();
	                    	points = pointsAfterMove - pointsBeforeMove;
                    	}
                    }
                    else if(i == 3){
                    	if(copyModel.moveRight()){
	                    	int pointsAfterMove = copyModel.getScore();
	                    	points = pointsAfterMove - pointsBeforeMove;
                    	}
                    }
                    else if(i == 4){
                    	if(copyModel.moveLeft()){
	                    	int pointsAfterMove = copyModel.getScore();
	                    	points = pointsAfterMove - pointsBeforeMove;
                    	}
                    }
                    
                    if(points == 0 && isEqual(model.getData(), copyModel.getData())) {
                    	continue;
                    }
                    
                    Map<String, Integer> currentResult = alphabeta(copyModel, depth - 1, alpha, beta, computer);
                    int currentScore = ((Number)currentResult.get("Score")).intValue();
                                        
                    if(currentScore > alpha) { //maximize score
                        alpha = currentScore;
                        bestDirection = i;
                    }
                    
                    if(beta <= alpha) {
                        break; //beta cutoff
                    }
                }
                
                bestScore = alpha;
            }
            else {
                List<Integer> moves = getEmptyCellIds(model.getData());
                int[] possibleValues = {2, 4};

                int i,j;
                abloop: for(Integer cellId : moves) {
                    i = cellId/model.getData().length;
                    j = cellId%((model.getData()[0]).length);

                    for(int value : possibleValues) {
                        Model copyModel = new Game2048Model((Game2048Model)model);
                     	setEmptyCell(copyModel, i, j, value);

                        Map<String, Integer> currentResult = alphabeta(copyModel, depth - 1, alpha, beta, user);
                        int currentScore = ((Number)currentResult.get("Score")).intValue();
                        if(currentScore < beta) { //minimize best score
                            beta = currentScore;
                        }
                        
                        if(beta <= alpha) {
                            break abloop; //alpha cutoff
                        }
                    }
                }
                
                bestScore = beta;
                
                if(moves.isEmpty()) {
                    bestScore = 0;
                }
            }
        }
        
        //System.out.println("Score" + bestScore + "," + "Direction" + "," + bestDirection);
        result.put("Score", bestScore);
        result.put("Direction", bestDirection);
        return result;
    }
	
	
	public void printArray(int [][] arr){
		for(int i = 0; i < arr.length ;i++){
			for(int j = 0; j < arr[0].length ; j++){
				System.out.print(arr[i][j]);
				System.out.print("_");
			}
			System.out.println();
		}
		System.out.println("________");
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
	
	
	private String huristicksCalculator(Model model) {
	
	
		
		
		String theHintAndNumOfEmptyCellsThatWillBe = "";
		
		Model modelCopy1 = new Game2048Model((Game2048Model)model);
		Model modelCopy2 = new Game2048Model((Game2048Model)model);
		Model modelCopy3 = new Game2048Model((Game2048Model)model);
		Model modelCopy4 = new Game2048Model((Game2048Model)model);
		
		
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
		int max = numOfemptyCellsFoo(model);
		
		
		int numOfEmptyCells1 = numOfemptyCellsFoo(modelCopy1);
		int numOfEmptyCells2 = numOfemptyCellsFoo(modelCopy2);
		int numOfEmptyCells3 = numOfemptyCellsFoo(modelCopy3);
		int numOfEmptyCells4 = numOfemptyCellsFoo(modelCopy4);
		
		if(numOfEmptyCells1 >= max){
			max = numOfEmptyCells1;
			theHintAndNumOfEmptyCellsThatWillBe = "UP" + "," + max;
		}
		if(numOfEmptyCells3 >= numOfEmptyCells2){
			max = numOfEmptyCells3;
			theHintAndNumOfEmptyCellsThatWillBe = "RIGHT" + "," + max;
		}
		if(numOfEmptyCells4 >= numOfEmptyCells3){
			max = numOfEmptyCells4;
			theHintAndNumOfEmptyCellsThatWillBe = "LEFT" + "," + max;
		}
		if(numOfEmptyCells2 >= numOfEmptyCells1){
			max = numOfEmptyCells2;
			theHintAndNumOfEmptyCellsThatWillBe = "DOWN" + "," + max;
		}
		if(numOfEmptyCells4 >= numOfEmptyCells3){
			max = numOfEmptyCells4;
			theHintAndNumOfEmptyCellsThatWillBe = "LEFT" + "," + max;
		}
	
		
		return theHintAndNumOfEmptyCellsThatWillBe;
	
		
		
		/**
		 * 
		 * random huristicks
		 */
		/*
		String[] theHints = {"UP","DOWN","RIGHT","LEFT"};
		Random rand = new Random();
		int index = rand.nextInt(4)+1;
		switch (index) {
		case 1:
			return "UP";
			
		case 2:
			return "DOWN";
			
		case 3:
			return "RIGHT";
			
		case 4:
			return "LEFT";
			

		default:
			break;
		}
		return "";
		*/
		
	}
	
	
	private int numOfemptyCellsFoo(Model model){
		
		if(cache_emptyCells==null) {
	          cache_emptyCells = (getEmptyCellIds(model.getData())).size();
	      }
	    return cache_emptyCells;
	}

	public int[][] copyTheBoard(int[][] data) {
		int[][] copyBoard = new int[data.length][data[0].length];
		for(int i = 0; i < data.length ; i++){
			for(int j = 0; j < data[0].length;j++){
				copyBoard[i][j] = data[i][j];
			}
		}
		return copyBoard;
	}

	private static int heuristicScore(int actualScore, int numberOfEmptyCells, int clusteringScore) {
		int score = (int) (actualScore + Math.log(actualScore)*numberOfEmptyCells - clusteringScore);
	    return Math.max(score, Math.min(actualScore, 1));
    }


	private static int calculateClusteringScore(int[][] boardArray) {
		 int clusteringScore=0;
	        
	        int[] neighbors = {-1,0,1};
	        
	        for(int i=0;i<boardArray.length;++i) {
	            for(int j=0;j<boardArray.length;++j) {
	                if(boardArray[i][j] == 0) {
	                    continue; //ignore empty cells
	                }
	                
	                //clusteringScore-=boardArray[i][j];
	                
	                //for every pixel find the distance from each neightbors
	                int numOfNeighbors = 0;
	                int sum = 0;
	                for(int k : neighbors) {
	                    int x = i + k;
	                    if(x < 0 || x >= boardArray.length) {
	                        continue;
	                    }
	                    for(int l : neighbors) {
	                        int y = j + l;
	                        if(y < 0 || y >= boardArray.length) {
	                            continue;
	                        }
	                        
	                        if(boardArray[x][y] > 0) {
	                            ++numOfNeighbors;
	                            sum += Math.abs(boardArray[i][j] - boardArray[x][y]);
	                        }
	                        
	                    }
	                }
	                
	                clusteringScore += (sum/numOfNeighbors);
	            }
	        }
	        
	        return clusteringScore;
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
		if(isEqual(modelCopy1.getData(), copyArrayModel1)){
			stuckUp = true;
		}
		modelCopy2.doUserCommand(2);
		if(isEqual(modelCopy2.getData(), copyArrayModel2)){
			stuckDown = true;
		}
		modelCopy3.doUserCommand(3);
		if(isEqual(modelCopy3.getData(), copyArrayModel3)){
			stuckRight = true;
		}
		modelCopy4.doUserCommand(4);
		if(isEqual(modelCopy4.getData(), copyArrayModel4)){
			stuckLeft = true;
		}
		
		return (stuckUp && stuckDown && stuckRight && stuckLeft);
	}
	
    public boolean isEqual(int[][] currBoardArray, int[][] newBoardArray) {

    	boolean equal = true;
        
        for(int i=0;i<currBoardArray.length;i++) {
            for(int j=0;j<currBoardArray.length;j++) {
                if(currBoardArray[i][j]!= newBoardArray[i][j]) {
                    equal = false; //The two boards are not same.
                    return equal;
                }
            }
        }
        
        return equal;
    }
}


