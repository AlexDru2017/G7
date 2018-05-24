package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import resources.Message;
import resources.Question;

public class Server extends AbstractServer {

	// region Constants

	final public static int DEFAULT_PORT = 5555;

	private final Connection connection;

	// end region -> Constants

	// region Constructors

	public Server(int port) {
		super(port);
		connection = SqlUtilities.connection();
	}

	// end region -> Constructors

	// region Protected Methods

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg == null) {
			// add error screen
			return;
		}
		if (msg instanceof ArrayList<?>) {

			// if it's from the questions table
			if (((ArrayList<?>) msg).get(0) instanceof Question) {
				try {
					SqlUtilities.editTable((ArrayList<Question>) msg);
					client.sendToClient(Message.SaveTable);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (msg instanceof String) {
			String str = (String) msg;
			String[] strArray = str.split(" ");
			try {
				switch (strArray[0]) {
				case "#login":
					PreparedStatement login = connection.prepareStatement(SqlUtilities.Login_SELECT_UserID_From_Users);
					login.setString(1, strArray[1]);
					login.setString(2, strArray[2]);
					ResultSet rs;
					rs = login.executeQuery();
					rs.next();
					if (rs.getString(1).equals(strArray[1])) {
						login = connection.prepareStatement(SqlUtilities.Login_getlog_Status);
						login.setString(1, strArray[1]);
						login.setString(2, strArray[2]);
						rs = login.executeQuery();
						rs.next();
						if (rs.getInt(1) == 0) {
							client.sendToClient("#Teacher");
							login = connection.prepareStatement(SqlUtilities.Login_UpdateUser_logStatus);
							login.setString(1, strArray[1]);
							login.setString(2, strArray[2]);
							login.executeUpdate();
							return;
						} else {
							client.sendToClient("#UserAlreadyConnected");
							System.out.println("UserAlreadyConnected");
							return;
						}
					} else {
						client.sendToClient("No such user");
						return;
					}
				case "#EditorRemovePressed":
					client.sendToClient(SqlUtilities.getQuestions());
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	@Override
	protected void serverStarted() {
		System.out.println("Server Started");
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// end region -> Protected Methods

	public static void main(String[] args) {
		int port = 0; // Port to listen on
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		Server server = new Server(port);
		try {
			server.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}