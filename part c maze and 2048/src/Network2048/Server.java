package Network2048;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5251997575233371250L;
	ServerSocket ss 		= null;
	ClientHandler handler 	= null;
	boolean stop 			= false;
	int port;
	int noc 				= 1;
	
	public Server(int port, int noc, ClientHandler ch){
		this.port = port;
		this.handler = ch;
		this.noc = noc;
	}
	
	public void connection(){		  
		try {  
		
			ss = new ServerSocket(port); 
			ss.setSoTimeout(160000); 
			
			ExecutorService tp = Executors.newFixedThreadPool(noc);
			
			while(!stop){
				try {
					final Socket someClient = ss.accept();  
					System.out.println("client connected");
					
					final ObjectInputStream input=new ObjectInputStream(someClient.getInputStream());
					final ObjectOutputStream output=new ObjectOutputStream(someClient.getOutputStream());
					
					tp.execute(new Runnable() {					
						@Override
						public void run() {
							handler.handleClient(input,output);
							try{
								input.close();
								output.close();
								someClient.close();
					//			stop = true; 	 
							}catch(Exception e){
								System.out.println("EXCEPTION , Nevermore11");
							}
						}
					});
				}catch(SocketTimeoutException e){
					System.out.println("SocketTimeOUT EXCEPTION, Nevermore9");
					stop = true;	
					System.out.println(e);						
				}	
			} // end of loop
			
			ss.close();
			System.out.println("serverSocket closed");
			
		} catch (IOException e) {
			System.out.println("io exception occured, Nevermore10");
			e.printStackTrace();
		}	
	}    

	@Override
	public void run() {
		connection();
		System.out.println("Connection with the server ended.");
		System.exit(0);
	}
	
	public void close(){
		stop = true;
	}

}
