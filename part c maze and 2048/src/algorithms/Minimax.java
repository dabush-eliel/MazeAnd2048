package algorithms;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import model.Game2048Model;
import model.Model;

public class Minimax implements Solver,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7715584375427671747L;
	private String user = "USER";
	private String computer = "COMPUTER";
	int numOfSolutionsLeft = 0;
	Node node;
	int depth;
	
	public Minimax(int depth){
		this.depth = depth;
	}
	
	@Override
	public int calculator(Model model) {
		
		node = new Node(model,user,4);
		System.out.println("Minimax calculator started.");
		
		
		
		
		
		int x = minimax(depth, user, node);

		System.out.println("**FINISHED CALCULATOR.**");
		return numOfSolutionsLeft;
		
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
	
	public int minimax(int depth, String player, Node node){
		if(depth == 0 || gameTerminated(node.getGameModel())){
			
		}
		
		if(player.equals(user)){
			node.setChildren(new Node[node.getNumOfChildren()]);
			for(int i = 1 ; i < 5 ; i++){
				Model copyModel = new Game2048Model((Game2048Model)node.getGameModel());
				if(i == 1){
					copyModel.moveUp();
				}
				else if(i == 2){
					copyModel.moveDown();
				}
				else if(i == 3){
					copyModel.moveRight();
				}
				else if(i == 4){
					copyModel.moveLeft();
				}
				node.getChildren()[i - 1] = new Node(copyModel,computer, getEmptyCellIds(copyModel.getData()).size());
				minimax(depth - 1, computer, node.getChildren()[i - 1]);
			}
		}
		else if(player.equals(computer)){
			Model modelCopy = new Game2048Model((Game2048Model)node.getGameModel());
			List<Integer> moves = getEmptyCellIds(modelCopy.getData());
            int[] possibleValues = {2, 4};
            int i,j;
            for(Integer cellId : moves) {
                i = cellId/modelCopy.getData().length;
                j = cellId%modelCopy.getData()[0].length;
                for(int value : possibleValues) {
                    setEmptyCell(modelCopy, i, j, value);
                }
            }
		}
		
		
		return 0;
	}
	
	
	
	
	
	// old minimax (not working)
	/*
	public Map<String,Integer> minimax(Model model, int depth, String player, Map<String,Integer> result){
		System.out.println(depth);
		if(depth == 0 || gameTerminated(model)){
			return result;
		}
		else{
			if(player.equals("USER")){
				Model modelCopy = new Game2048Model((Game2048Model)model);
				for(int i = 1; i < 4 ; i++){
					String theHintAndNumOfEmptyCellsThatWillBe = huristicksCalculator(modelCopy);
					String[] toSplit = theHintAndNumOfEmptyCellsThatWillBe.split(",");
					String bestMoveIs = toSplit[0];
					int willBeEmptyCells = Integer.parseInt(toSplit[1]);
					modelCopy.doUserCommand(i);
					String string = "USER"+ "," + i + "," + depth + "," + bestMoveIs;
                    result.put(string, willBeEmptyCells);
				}
				minimax(modelCopy, depth - 1, computer, result);
			}
			else if(player.equals("COMPUTER")){
				Model modelCopy = new Game2048Model((Game2048Model)model);
				List<Integer> moves = getEmptyCellIds(model.getData());
                int[] possibleValues = {2, 4};
                int i,j;
                for(Integer cellId : moves) {
                    i = cellId/model.getData().length;
                    j = cellId%model.getData()[0].length;
                    for(int value : possibleValues) {
                        setEmptyCell(modelCopy, i, j, value);
                        String string = "COMPUTER" + "," + i + "," + j + "," + depth;   
                        result.put(string, value);                        
                    }
                }
                minimax(modelCopy, depth - 1, user, result);
			}
		}
	    
	    return result;
		
	}
	
	*/
	
	/**
	 * 
	 * 
	 * for received board check where to move for get more empty cells
	 * 
	 */
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


	
		
		
	public void setEmptyCell(Model model, int i, int j, int value) {
	       if(model.getData()[i][j] == 0) {
	    	   model.getData()[i][j] = value;
	           
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


