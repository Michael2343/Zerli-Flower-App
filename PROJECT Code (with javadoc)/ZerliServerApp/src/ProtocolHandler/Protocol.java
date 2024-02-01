package ProtocolHandler;

import java.io.IOException;
import java.util.HashMap;

import ocsf.server.ConnectionToClient;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class Protocol serves the main control of the protocol handler
 * from the server side.
 * The protocol handler is responsible for
 * -Receiving commands
 * -Parsing commands
 * -Performing operations/requests
 * -Generating response
 * -Sending Responses
 * -Error handling
 * -Validating
 */
public class Protocol {
	
	/** The definition of pass. */
	final boolean PASS = true;
	
	/** The definition of failure. */
	final boolean FAIL = false;
	
	/**  This HashMap is used to map every requestType to the designated Operation
	 *   when the Protocol-Handler has identified a request it will perform the mapped operation. */
	static HashMap<RequestType,IOperation> Operations = new HashMap<>();
	
	/**
	 * This method is used to register a new command reaction
	 * This method is used to encapsulate the Operation map from the programmer.
	 *
	 * @param type the type
	 * @param op the op
	 */
	public static void RegisterOperation(RequestType type, IOperation op)
	{
		Operations.put(type, op);
	}
	
	/**
	 * When a protocol is instantiated it will register
	 * all the RegisterOperation calls.
	 */
	public Protocol()
	{
		RequestRegisterer.RegisterProtocol();
	}
	
	/**
	 * The Handle function links directly to the OCSF Incoming message layer
	 * when a system is using the Protocol-Handler the transmission between the client server
	 * is only allowed via a Transaction object, any other object moving along the sockets
	 * will be disregarded
	 * The Protocol-Handler upon receiving an Transaction will peel off the layers
	 * Parsing the request and information on the way, eventually invoking the operation
	 * and responding accordingly.
	 *
	 * @param transmission the transmission
	 * @param sender the sender
	 * @return true - pass, false - error
	 * @throws Exception the exception
	 */
	public boolean Handle(Object transmission, ConnectionToClient sender) throws Exception
	{
		Server.Log("Server", "Message received from: " + sender.getInetAddress().getHostAddress());
		ResponseWrapper response = new ResponseWrapper();
		if(!(transmission instanceof Transaction))
		{
			Server.Log("Protocol", "Unconventional data received, ignoring...");
			throw new Exception("Unconventional Transimission");
		}
		
		Transaction transaction = (Transaction)transmission;
		boolean status;
		Server.Log("Protocol",String.format("Incoming transmission from %s (%s)\n-request:%s\nresponse? %s",transaction.requestee,sender.getInetAddress().getHostAddress(),transaction.requestType.toString(),transaction.shouldRespond() ? "Yes":"No"));
		if(transaction.shouldRespond())
			status = Operations.get(transaction.getRequestType()).Perform(transaction.requestee, transaction.getReceivedData(), transaction.getReceivedParameters(),response);
		else
			status = Operations.get(transaction.getRequestType()).Perform(transaction.requestee, transaction.getReceivedData(), transaction.getReceivedParameters());
		
		if(FAIL == status)
		{
			Server.Log("Protocol", "Operation of request " + transaction.requestType + " for " + transaction.getRequester() + "has failed");
			return FAIL;
		}
		if(transaction.shouldRespond())
		{
			Server.Log("Protocol", "Building response transaction for " + transaction.requestType + " for " + transaction.getRequester());
			transaction.setResponse(response.getResponse());
			Server.Log("Server", "Sending response to client: " + transaction.getRequester());
			send(sender,transaction);
		}
		
		return PASS;
	}
	
	/**
	 * The send method is used to respond to the sending client
	 * After the request has been done
	 * the send method is logged by the server logger.
	 *
	 * @param client is the original command requester
	 * @param trans the trans
	 * @throws Exception the exception
	 */
	private void send(ConnectionToClient client,Transaction trans) throws Exception
	{
	    try {
			client.sendToClient(trans);
		} catch (IOException e) {
			Server.Log("Server", "Failed connecting to client: " + trans.getRequester());
			throw new Exception("Cannot reply to client : " + e.getMessage() +e.getStackTrace().toString());
		}
	}
}
