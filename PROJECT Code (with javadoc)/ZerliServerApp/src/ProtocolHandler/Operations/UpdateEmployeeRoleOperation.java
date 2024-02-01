package ProtocolHandler.Operations;

import Entities.*;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdateEmployeeRoleOperation.
 */
public class UpdateEmployeeRoleOperation implements IOperation{

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
		if(data instanceof Roles && params instanceof String) {
			String role = ((Roles)data).toString();
			String userID = (String)params;
		    Server.SqlServerManager.UpdateEmployeeRole(userID,role);
			return true;
		}
		return false;
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
