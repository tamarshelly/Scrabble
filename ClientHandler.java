package test;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {

	/*the conversion method */
	void handleClient(InputStream inFromclient, OutputStream outToClient);
	void close(); //close the stream
}
