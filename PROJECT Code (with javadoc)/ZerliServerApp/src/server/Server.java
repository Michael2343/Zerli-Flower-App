package server;

import ProtocolHandler.Protocol;
import controllers.ServerPanelController;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
// TODO: Auto-generated Javadoc

/**
 * The Class Server.
 */
public class Server extends AbstractServer{
	
	/** The Constant DEFAULT_PORT. */
	final public static int DEFAULT_PORT = 5555;

	/** The Constant VERSION. */
	public static final String VERSION = "0.0.4 Alpha";
	
	/** The Sql server manager. */
	public static ServerConnSQL SqlServerManager = new ServerConnSQL();
	
	/** The Protocol handler. */
	private static Protocol ProtocolHandler = new Protocol();
	
	/** The Console used for logging. */
	public static TextArea Console;
	
	/**
	 * Instantiates a new server.
	 *
	 * @param port the port used to be binded to
	 */
	public Server(int port) {
		super(port);
	}

	/**
	 * Handles message from client - OCSF Layer
	 * The method will redirect all incoming messages to the Protocol Handler infrastructure
	 *
	 * @param msg the msg that is being received
	 * @param client the client that has sent the message
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		
		try {
			ProtocolHandler.Handle(msg, client);
		} catch (Exception e1) {e1.printStackTrace();}  
	}
	
	/**
	 * Log - is used to log an operation from a source to the console in the server UI panel
	 *
	 * @param source - the module that is being logged
	 * @param content - the log content
	 */
	public static void Log(String source,String content)
	{
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				var formatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm");
				Console.appendText(String.format("%s >> [%s]: %s\n",formatter.format(LocalDateTime.now()),source,content));
			}

		});
	}

}
