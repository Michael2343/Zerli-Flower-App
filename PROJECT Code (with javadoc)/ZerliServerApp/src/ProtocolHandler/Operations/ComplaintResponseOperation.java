package ProtocolHandler.Operations;

import Entities.Complaint;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class ComplaintResponseOperation.
 */
public class ComplaintResponseOperation implements IOperation {

	/**
	 * Perform.
	 *
	 * @param requestee the requestee
	 * @param data the data
	 * @param params the params
	 * @return true, if successful
	 */
	@Override
	public boolean Perform(String requestee, Object data, Object params) {
		Complaint complaint = (Complaint)data;
		Server.SqlServerManager.ComplaintResponse(complaint);
		return true;
	}

	/**
	 * Perform.
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
