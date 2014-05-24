package presenter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import minimax.AIsolver;
import model.Game2048Model;
import model.Model;
import algorithms.AlphaBeta;

/**
 * Main that runs a memory test for Minimax algorithm and AlpaBeta pruning algorithm
 * compare between them by test 10 different board states and the both algorithms on them
 * takes very long time to get result.
 * 
 */
public class MainScript {
	
//	Model model2048;
//	View view2048;
//	Presenter presenter;
	static int [][] states = { {2,2,2,6} ,{2,6,2,15} , {2,11,2,14} , {2,1,2,13} , {2,3,4,9} , {4,1,2,5} , {4,1,2,16} , {4,11,4,13} , {4,3,4,4} , {4,6,4,11}};
		

	public static void main(String[] args) throws IOException {
		
			
		for(int i = 0 ; i<1 ; i++){
			
			String filename = "TestScriptOutcom/"+"MinimaxBoardSize"+4*(i+1)+".csv"; 
			FileWriter file = new FileWriter(filename);
			PrintWriter pw = new PrintWriter(file);
			
			for(int j = 0 ; j < 10 ; j++){
				long startTime = System.currentTimeMillis();
				String result = startGame(4*(1+i),states[j][0],states[j][1],states[j][2],states[j][3]);

				float elapsedTime;
				float elapsedTimeMilis = System.currentTimeMillis() - startTime;
				float elapsedTimeSec = elapsedTimeMilis/1000F;

				if(elapsedTimeSec > 60){
					elapsedTime = elapsedTimeSec/60;
					result += ",TimeToSolution#"+elapsedTime+"min ";
					
				}else{
					elapsedTime = elapsedTimeSec;
					result += "TimeToSolution#"+elapsedTime+"sec ";
				}
				pw.println("Game#"+j+",BoardSize#"+4*(1+i)+",FirstTileVal#"+states[j][0]+",FirstTilePosition#"+states[j][1]+",SecondTileVal#"+states[j][2]+",SecondTilePosition#"+states[j][3]+","+result);
			}
			pw.close();	
		}	
	}
	
	
	public static String startGame(int size, int sqr1val, int sqr1plc,int sqr2val, int sqr2plc){

		long counter 	= 0;
		AlphaBeta ab 	= new AlphaBeta();
		Model model2048	= new Game2048Model( size,  sqr1val,  sqr1plc, sqr2val,  sqr2plc);	
		int [][] lastB = new int [model2048.getData().length][model2048.getData()[0].length];
		
		while(!model2048.isStuck() && !model2048.isSucceed()){
			// save last board data
			for(int i = 0 ; i < size ; i++){
				for(int j = 0 ; j < size ; j++){
					lastB[i][j] = model2048.getData()[i][j];
				}
			}
			// perform a user command
			int hint = ab.calculator(model2048);
			model2048.doUserCommand(hint);
			while(!model2048.isChanged(model2048.getData(), lastB)){
				Random rand = new Random();
				model2048.doUserCommand(rand.nextInt(4));
			}
			counter +=  AIsolver.statesCounter;	
			System.out.println("score: "+model2048.getScore());
		}
			
		String str;
		int score = model2048.getScore();
		
		if(score >=18432){
			str = "won";
		}else{
			str = "lost";
		}
		return str+","+"GameScore#"+score+","+"StatesDeveloped#"+counter;	
	}
	
}

