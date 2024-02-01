package ProtocolHandler.Operations;

import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;
import Entities.*;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdateNewItemInCartOperation defines the operation
 * that is invoked upon an UpdateNewItemInCart Request.
 */
public class UpdateNewItemInCartOperation implements IOperation {

	/**
	 * The perform operation is the method that is being invoked when the matching request is made
	 * all database calls are managed here, along with some processing and input validation.
	 *
	 * @param requestee - the request sender
	 * @param data - the data that was sent
	 * @param params - the parameters constraints that were pinned to the data
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params) {
		if(data instanceof NewItem)
		{
			NewItem newItem= (NewItem)data;
			Server.SqlServerManager.UpdateNewItemInCart(requestee,newItem);
			return true;
		}
		return false;
	}

	/**
	 * This perform is not used since the operation is an Operate-Only request.
	 *
	 * @param requestee the requestee
	 * @param data the data
	 * @param params the params
	 * @param response the response
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params, ResponseWrapper response) {
		// TODO Auto-generated method stub
		return false;
	}

}
