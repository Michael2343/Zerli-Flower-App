package ProtocolHandler;

/**
 * The Interface IHandler is used when creating a custom handler
 * A Handler is a final command processing layer that is invoked on Operate&Respond commands
 * when a response is received in the client protocol handler the protocol handler will seek a handler
 * when a handler is located the handler is provided with the response data
 * and therefore is able to perform processing logic on it, and store it as the new data
 * The purpose of the handler is to provide another hidden protocol logic layer
 * to hide "ugly" processing from the user
 */
public interface IHandler {
	
	/**
	 * Handles a response, and returns the newly processed object.
	 *
	 * @param response the response received from the server
	 * @return the newly processed object
	 */
	public Object HandleResponse(Object response);
}
