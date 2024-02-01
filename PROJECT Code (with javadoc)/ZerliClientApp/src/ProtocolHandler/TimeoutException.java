package ProtocolHandler;

/**
 * The Class TimeoutException is used by the protocol to
 * determine a state of a time out when waiting for a server response.
 */
public class TimeoutException extends RuntimeException {
	
	public TimeoutException(String string)
	{
		super(string);
	}
}
