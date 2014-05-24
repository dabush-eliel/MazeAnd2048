package algorithms;

import java.io.Serializable;

import minimax.AIsolver;
import minimax.Board;
import minimax.Direction;
import model.Model;

public class AlphaBeta implements Solver, Serializable {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public int calculator(Model model) {
		try {
			
			int hintDepth 	= model.getDepth();
			Board newGame 	= new Board(model.getData(), model.getScore());	
			Direction hint 	= AIsolver.findBestMove(newGame, hintDepth);

			if(hint==Direction.UP) {
				return 1;
		    }
		    else if(hint==Direction.DOWN) {				    
		    	return 2;
		    }
		    else if(hint==Direction.RIGHT) {
		    	return 3;
		    }else{
		    	return 4;
		    }
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
