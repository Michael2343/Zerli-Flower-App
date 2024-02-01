package ProtocolHandler.Handlers;

import ProtocolHandler.IHandler;

/**
 * The Class PingHandler is a demonstration of an echo
 * handler which simply forwards the handler
 */

public class PingHandler implements IHandler {

	/**
	 * Handle response.
	 *
	 * @param response the data received from the server
	 * @return the same data.
	 */
	@Override
	public Object HandleResponse(Object response) {
		return response;
	}

}
