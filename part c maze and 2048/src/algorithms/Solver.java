package algorithms;

import model.Model;

/**
 * Interfac Solver, which have the calculator mathod to get next move.
 * @author Eliel Dabush and Oleg Glizerin.
 * @return integer 1 for up, 2 for down, 3 for right ,4 for left.
 */

public interface Solver{
	
	public int calculator(Model model);

}
