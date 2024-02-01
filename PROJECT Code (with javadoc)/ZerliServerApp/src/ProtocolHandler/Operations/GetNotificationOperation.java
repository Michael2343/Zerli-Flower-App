package ProtocolHandler.Operations;

import java.util.ArrayList;

import Entities.NotificationInTable;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class GetNotificationOperation defines the operation
 * that is invoked upon an GetNotification Request.
 */
public class GetNotificationOperation implements IOperation {

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
		// TODO Auto-generated method stub
		return false;
	}

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
		ArrayList<NotificationInTable> notificationList=new ArrayList<NotificationInTable>();
		server.Server.SqlServerManager.getNotification(requestee,notificationList);
		response.SetResponse(notificationList);
		return true;
	}

}
