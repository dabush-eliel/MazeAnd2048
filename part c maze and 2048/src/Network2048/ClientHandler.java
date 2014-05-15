package Network2048;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface ClientHandler {
	
		public void handleClient(ObjectInputStream input, ObjectOutputStream output);
		
}
