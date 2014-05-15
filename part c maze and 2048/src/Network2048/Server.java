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
			ss.setSoTimeout(10000); 
			
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
								// need to find another place to set -stop = true- 
								// 	--> because now its only for one client!
					//			stop = true; 	 
							}catch(Exception e){
							}
						}
					});
				}catch(SocketTimeoutException e){
					stop = true;	
					System.out.println(e);						
				}	
			} // end of loop
			
			ss.close();
			System.out.println("serverSocket closed");
			
		} catch (IOException e) {
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
