package client;

import java.io.IOException;
import ocsf.client.*;


/**
 * The Class ClientConn handles the connection between a client and a host.
 */
public class ClientConn extends AbstractClient{
	
	/**
	 * Instantiates a new client conn.
	 *
	 * @param host the host of the connection
	 * @param port the port of the connection
	 */
	public ClientConn(String host, int port) {
		super(host, port);
		try {
			openConnection();
		} catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * Handle message from server.
	 *
	 * @param msg the message
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		ClientApp.ProtocolHandler.Handle(msg);
	}

}
