package client;

import java.io.IOException;

import ocsf.client.AbstractClient;

public class Client extends AbstractClient {

	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */
	public Client(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg
	 *            The message from the server.
	 */
	@Override
	public void handleMessageFromServer(Object obj) {
		if (obj == null) {
			// add something
			return;
		}

	} 
	
}
