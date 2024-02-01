package ProtocolHandler.Operations;

import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class SetComplaintAlertedOperation defines the operation
 * that is invoked upon an SetComplaintAlertedOperation Request.
 */
public class SetComplaintAlertedOperation implements IOperation {

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
		Server.SqlServerManager.SetComplaintAlerted((Integer)params);
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
		// TODO Auto-generated method stub
		return false;
	}

}
