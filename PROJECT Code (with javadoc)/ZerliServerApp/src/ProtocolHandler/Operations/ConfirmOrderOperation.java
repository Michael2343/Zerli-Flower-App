package ProtocolHandler.Operations;

import Entities.Order;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfirmOrderOperation defines the operation
 * that is invoked upon a ConfirmOrder Request.
 */
public class ConfirmOrderOperation implements IOperation {

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
		if(data instanceof Order ) {
			Order order = (Order)data;
			int num = Server.SqlServerManager.ConfirmOrder(Integer.valueOf(order.getOrderID()),(String)params);
			response.SetResponse(num);
			return true;
		}
		return false;
	}

}
