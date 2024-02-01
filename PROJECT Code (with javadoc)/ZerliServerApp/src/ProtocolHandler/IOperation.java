/*
 * 
 */
package ProtocolHandler;

// TODO: Auto-generated Javadoc
/**
 * The Interface IOperation is used when designating a new command
 * it has two methods used differently by the Protocol Handler.
 */
public interface IOperation {
	
	/**
	 * This method is used to Perform an Operation without a response back to the server.
	 *
	 * @param requestee - holds a unique ID of the requester client
	 * @param data - holds data (in an agreed data type and method) to be used in the Perform
	 * @param params - holds any parameters (in an agreed data type and method) to be used in the Perform
	 * @return success - true for PASS and false for FAIL
	 */
	public boolean Perform(String requestee,Object data, Object params);
	
	/**
	 * This method is used to Perform an Operation without a response back to the server.
	 *
	 * @param requestee - holds a unique ID of the requester client
	 * @param data - holds data (in an agreed data type and method) to be used in the Perform
	 * @param params - holds any parameters (in an agreed data type and method) to be used in the Perform
	 * @param response - use response.SetResponse to set any (agreed) object to return to the client.
	 * @return success - true for PASS and false for FAIL
	 */
	public boolean Perform(String requestee,Object data,Object params,ResponseWrapper response);
	
	/* Example of agreement and coding convention
	 * REQUIREMENT: Clients Sends to Server a command to set the price value of a specific order
	 * AGREEMENT:
	 * 		- data is of type Integer and holds the price
	 * 		- params is of type string and holds orderID
	 */
}
