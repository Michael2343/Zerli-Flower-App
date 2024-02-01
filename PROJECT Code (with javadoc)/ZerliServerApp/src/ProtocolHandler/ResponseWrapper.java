/*
 * 
 */
package ProtocolHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseWrapper is used to return a response
 * from an IOperationd.
 */
public class ResponseWrapper {
	
	/** The response to be returned. */
	Object response;
	
	/**
	 * Instantiates a new response wrapper.
	 *
	 * @param response the response
	 */
	public ResponseWrapper(Object response)
	{
		this.response = response;
	}
	
	/**
	 * Instantiates a new response wrapper.
	 */
	public ResponseWrapper()
	{
		response = null;
	}
	
	/**
	 * Sets the response.
	 *
	 * @param Response the response
	 */
	public void SetResponse(Object Response)
	{
		response = Response;
	}
	
	/**
	 * Gets the response.
	 *
	 * @return The Response
	 */
	public Object getResponse()
	{
		return response;
	}
}
