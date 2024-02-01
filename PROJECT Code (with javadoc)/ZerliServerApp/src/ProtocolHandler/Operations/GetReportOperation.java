package ProtocolHandler.Operations;

import java.time.LocalDate;

import Entities.ReportType;
import ProtocolHandler.IOperation;
import ProtocolHandler.ResponseWrapper;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class GetReportOperation defines the operation
 * that is invoked upon an GetReport Request.
 */
public class GetReportOperation implements IOperation {

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
		if(params instanceof Object[])
		{
			Object[] par = (Object[])params;
			response.SetResponse(Server.SqlServerManager.GetReport((ReportType)(par[0]), (boolean)par[1], requestee, 
					java.sql.Date.valueOf(((LocalDate)par[2]))));
			return true;
		}
		return false;
	}

}
