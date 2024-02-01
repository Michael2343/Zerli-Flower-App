package ProtocolHandler.Operations;

import javax.swing.text.AbstractDocument.Content;

import Entities.NotificationInTable;
import Entities.Survey;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class SendNotificationOperation defines the operation
 * that is invoked upon an SendNotificationOperation Request.
 */
public class SendNotificationOperation implements IOperation {
	
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

		if(data instanceof NotificationInTable)
		{
			NotificationInTable notification = (NotificationInTable)data;
			Server.SqlServerManager.SendNotification(notification);
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
		return false;
	}
}
