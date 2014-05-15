package Network2048;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import model.Model;
import algorithms.Solver;

public class Server implements Runnable, Serializable {
	Model model;
	Solver solver;
	
	public Server(){
		
	}
	
	public void connection(){
		
		int port = 2002;  
		try {  
		ServerSocket ss = new ServerSocket(port); 
		ss.setSoTimeout(5000); // 5-sec just for test
		Socket someClient = ss.accept();  
		ObjectOutputStream output=new ObjectOutputStream(someClient.getOutputStream());
		ObjectInputStream input=new ObjectInputStream(someClient.getInputStream());

		model = (Model)input.readObject();  
		System.out.println((String)input.readObject());  
		solver = (Solver)input.readObject();
		System.out.println((String)input.readObject());
		
		if (model != null && solver != null){
			solver.calculator(model);
			//startSolv();
			System.out.println("current model score is : "+model.getScore());
		} 
		
		
		output.close();
		input.close();
		someClient.close();  
		ss.close();  
		}catch(Exception e){System.out.println(e);}  
	}    
	

	public String startSolv(){
		
		System.out.println("Solver in the server started.");
		return "output from Start Method";
	}

	@Override
	public void run() {
		connection();
		System.out.println("Connection with the server ended.");
	}

}
