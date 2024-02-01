package ProtocolHandler.Operations;

import java.util.ArrayList;

import Entities.Order;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class GetOrdersByBranchOperation defines the operation
 * that is invoked upon an GetOrderByBranch Request.
 */
public class GetOrdersByBranchOperation implements IOperation {

	/**
	 * This perform is not used since the operation is an Operate&Respond request.
	 *
	 * @param requestee the requestee
	 * @param data the data
	 * @param params the params
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params) {
		return false;
	}

	// data - branch
	// params manager/delivery
	
	/**
	 * The perform operation is the method that is being invoked when the matching request is made
	 * all database calls are managed here, along with some processing and input validation.
	 *
	 * @param requestee - the request sender
	 * @param data - the data that was sent
	 * @param params - the parameters constraints that were pinned to the data
	 * @param response the response
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params, ResponseWrapper response) {
		if(data instanceof String && params instanceof String) {
			ArrayList<Order> Orders = new ArrayList<Order>();
		    Server.SqlServerManager.GetOrdersByBranch(Orders, (String)data, (String)params);
		    response.SetResponse(Orders);
			return true;
		}
		return false;
	}

}
