package Network2048;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import model.Game2048Model;
import model.Model;
import algorithms.Solver;

public class GameHandler implements ClientHandler {

	Model model 			= null;
	Solver solver 			= null;
	static	int counter 		= 0;

	@Override
	public void handleClient(ObjectInputStream input, ObjectOutputStream output) {
	
		try {
			while(true){
			   Object obj = input.readObject();
			   
			    if (obj instanceof String) {
			        if ((((String) obj)).equalsIgnoreCase("exit")) {
			            break;
			        }else{
			        	System.out.println("Current obj: "+(String)obj);
			        }
			        
			    }else if (obj instanceof int[][]){
			    	model = new Game2048Model();
			    	model.setData((int [][])obj);
			    	
			    }else if(obj instanceof Solver){
			    	solver = (Solver) obj;
			    }							    
			}
			
			System.out.println(solver.getClass());
			
			if (model != null && solver != null){
	
				Integer val	= new Integer(solver.calculator(model));
				output.writeObject(val);

				
			}
			
			input.close();
			output.close();
			
		}catch (ClassNotFoundException | IOException e) {
			System.out.println("CLASS OR IO, Nevermore8");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
