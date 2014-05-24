package presenter;

import java.util.Observable;

import model.Game2048Model;
import model.Model;
import view.View;
import view.game2048.Game2048View;
import Network2048.GameHandler;
import Network2048.Server;

/**
 * Main that runs a memory test for Minimax algorithm and AlpaBeta pruning algorithm
 * compare between them by test 10 different board states and the both algorithms on them
 * 
 */
public class MainScript {
	
//	Model model2048;
//	View view2048;
//	Presenter presenter;
	static int [][] states = { {2,2,2,6} ,{2,6,2,15} , {2,11,2,14} , {2,1,2,13} , {2,3,4,9} , {4,1,2,5} , {4,1,2,16} , {4,11,4,13} , {4,3,4,4} , {4,6,4,11}};
		

	public static void main(String[] args) {
		Server server = new Server(2022, 1, new GameHandler());		
		Thread sThread = new Thread(server);
		sThread.start();	
		
		for(int i = 0 ; i<3 ; i++){
			for(int j = 0 ; j < 10 ; j++){
				
				startGame(4*(i+1),states[j][0],states[j][1],states[j][2],states[j][3]);
			}
		}
		
		try {
			Thread.sleep(480000);
		} catch (InterruptedException e) {
			System.out.println("INTERUPTED EXCEPTION, Nevermore13");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
		server.close();	
		
		
	}
	
	
	public static void startGame(int size, int sqr1val, int sqr1plc,int sqr2val, int sqr2plc){
		Model model2048				= new Game2048Model( size,  sqr1val,  sqr1plc, sqr2val,  sqr2plc);	
		View view2048				= new Game2048View(model2048.getData());
		System.out.println("game2048 model&view initialized!");
		Presenter presenter 		= new Presenter(model2048, view2048);		
		((Observable) view2048).addObserver(presenter);
		((Observable) model2048).addObserver(presenter);
		System.out.println("observers initialized");

		Thread viewThread = new Thread((Game2048View) view2048);
		viewThread.start();
	}

}
