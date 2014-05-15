package Network2048;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jws.WebParam.Mode;

import model.Model;
import algorithms.Solver;

public class GameHandler implements ClientHandler {

	Model model 			= null;
	Solver solver 			= null;

	@Override
	public void handleClient(ObjectInputStream input, ObjectOutputStream output) {
	
		try {
			while(true){
				   Object obj = input.readObject();

				    if (obj instanceof String) {
				        if ((((String) obj)).equalsIgnoreCase("exit")) {
				            break;
				        }else{
				        	System.out.println((String) obj);
				        }
				    }
				    if(obj instanceof Model){
				    	model = (Model) obj;
				    }
				    if(obj instanceof Solver){
				    	solver = (Solver) obj;
				    }
				
				   
					if (model != null && solver != null){
						System.out.println("current model score is : "+model.getScore());
						System.out.println(startSolv());	
					}				    
			}
			
			input.close();
			output.close();
			
		}catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private String startSolv(){
		
		System.out.println("Solver in the clientHandler started.");
		return "output from Start Method - gameHandler";
	}

}
