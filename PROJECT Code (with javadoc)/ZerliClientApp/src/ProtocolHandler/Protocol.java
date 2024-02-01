package ProtocolHandler;

import java.io.IOException;
import java.util.HashMap;

import client.ClientApp;
import controllers.LoginController;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// TODO: Auto-generated Javadoc
/**
 * The Class Protocol serves the main control of the protocol handler
 * from the client side.
 * The protocol handler is responsible for
 * -Sending commands (called invoking)
 * -Generating transaction
 * -Receiving responses.
 * -Handlers registration
 * -Handlers redirection
 * -Error handling
 * -Validating
 */
public class Protocol {
	
	/** The definition of pass. */
	final boolean PASS = true;
	
	/** The definition of failure. */
	final boolean FAIL = false;
	
	/** The max time to wait for a response timeout. */
	final long TIMEOUT = 200000;
	
	/**  This HashMap is used to map every requestType to the designated handler
	 *   when the Protocol-Handler has identified a response it will perform the mapped handler (if exists such). */
	static HashMap<RequestType,IHandler> Handlers = new HashMap<>();
	
	/**  This HashMap is used to map every requestType to the designated response
	 *   when the Protocol-Handler has identified a response it will load the response into the hashmap
	 *   this in turns enable multi-transactioning when multiple commands can be sent
	 *   and only then be read sequentally */
	static HashMap<RequestType,Object> Responses = new HashMap<>();
	
	/** A Mutex monitor object. */
	Object waiter = new Object();
	
	/** The on hold flag is used to determine whether a blocking command was sent previously
	 * and is yet to be answered. */
	boolean onHold = false;
	
	/**
	 * Register handler, registers a functional handler into a specific request type.
	 *
	 * @param requestType the request type for a handler to be mapped to
	 * @param handler the handler to be mapped to a request type
	 */
	public static void RegisterHandler(RequestType requestType, IHandler handler)
	{
		Handlers.put(requestType, handler);
	}
	
	/**
	 *  Gets the response of a certain request that was sent earlier
	 *  this is a blocking operation (for max of TIMEOUT milliseconds)
	 *
	 * @param requestType the request type we seek its response
	 * @return the response
	 */
	public synchronized Object GetResponse(RequestType requestType)
	{
			//OnLoading();
			int time = 0;
			
			while(onHold && time < TIMEOUT) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			onHold = false;
			if(time > TIMEOUT)
			{
				//System.out.println("timeOut");
				throw new TimeoutException("Command " + requestType.toString() + "did not respond!");
			}
			//EndLoading();
			return Responses.get(requestType);
	}
	
	/**
	 * Instantiates a new protocol handler instance.
	 * And register all handlers on creation
	 */
	public Protocol()
	{
		HandlersRegisterer.RegisterHandlers();
	}

	/**
	 * Invoke is used to initiate an actual command to the server
	 *
	 * @param requestType the request type, the actual command to be sent
	 * @param data the data that is being sent to the server
	 * @param params the params constraints that are being sent to the server
	 * @param expectingResponse true, if a response is awaited
	 * @return true, if successful
	 */
	public boolean Invoke(RequestType requestType,Object data,Object params,boolean expectingResponse)
	{
		//waiting for another response
		while(onHold);
		onHold = expectingResponse;
		Transaction transaction = new Transaction(requestType,ClientApp.UserID,data,params,expectingResponse);
		try {
			ClientApp.ClientConnection.sendToServer(transaction);
		} catch (IOException e) {
			e.printStackTrace();
			return FAIL;
		}
		
		return PASS;
	}

	/**
	 * Handles an incoming data from the server
	 * declines communication if a response for previous request has not been received
	 * or the transmission is not the type of transaction.
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	public synchronized boolean Handle(Object msg) {
		
		if(!onHold || !(msg instanceof Transaction)) return false;
		Transaction response = (Transaction)msg;
		if(Handlers.get(response.getRequestType()) != null)
			Responses.put(response.requestType, Handlers.get(response.getRequestType()).HandleResponse(response.getResponse()));
		else
			Responses.put(response.requestType, response.getResponse());
		onHold = false;
		notifyAll();
		return PASS;
		
	}
}
