package ProtocolHandler;

import java.io.Serializable;

/**
 * The Class Transaction is the fundamental communication element
 * that is passed between Server<->Client, it holds information regarding
 * the request, sender, parameters,data and more.
 */
public class Transaction implements Serializable{
	
	/** The sender of the transaction in terms of username. */
	//Identifier
	String requestee;
	
	/**  Is this request is Operate&Respond or Operate Only. */
	//Protocol Header
	boolean _shouldRespond;
	
	/** The RequestType 
	 * (see RequestType.java for more information regarding the available request) */
	//Data
	RequestType requestType;
	
	/**  The received data object from the client. */
	Object received;
	
	/** The parameter constraints sent by the client pinned to the data. */
	Object params;
	
	/**  The response to be returned to the client. */
	//Response
	Object response;
	
	/**
	 * Instantiates a new transaction.
	 *
	 * @param requestType - (see RequestType.java)
	 * @param requestee - the Username of the requester
	 * @param data - the data object [can be null]
	 * @param params - the parameter constraints on the data [can be null]
	 * @param returns - should the receiver respond [true - yes | false - no]
	 */
	public Transaction(RequestType requestType,String requestee,Object data,Object params,boolean returns)
	{
		this.requestType = requestType;
		this.requestee = requestee;
		this.received = data;
		this.params = params;
		this._shouldRespond = returns;
	}
	
	/**
	 * Gets the requester.
	 *
	 * @return the requester
	 */
	//Getters
	public String getRequester()
	{
		return requestee;
	}
	
	/**
	 * Gets the request type.
	 *
	 * @return the request type
	 */
	public RequestType getRequestType()
	{
		return requestType;
	}
	
	/**
	 * Should respond.
	 *
	 * @return - is the sender expecting a response
	 */
	public boolean shouldRespond()
	{
		return _shouldRespond;
	}
	
	/**
	 * Gets the received data.
	 *
	 * @return the received data
	 */
	public Object getReceivedData()
	{
		return received;
	}
	
	/**
	 * Gets the received parameters.
	 *
	 * @return the received parameters
	 */
	public Object getReceivedParameters()
	{
		return params;
	}
	
	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public Object getResponse()
	{
		return response;
	}
	
	/**
	 * Sets the response.
	 *
	 * @param res the new response
	 */
	public void setResponse(Object res)
	{
		response = res;
	}
}
