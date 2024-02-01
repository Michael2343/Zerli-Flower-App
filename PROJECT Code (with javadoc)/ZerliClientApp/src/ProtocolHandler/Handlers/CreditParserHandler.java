package ProtocolHandler.Handlers;

import Entities.paymentInfo;
import ProtocolHandler.IHandler;

/**
 * The Class CreditParserHandler is used to handle a response
 * of credit information data in an unorganized fashion and order
 * into a specific entity.
 */
public class CreditParserHandler implements IHandler {

	/**
	 * Handle response, gets the information in form of string array and
	 * reformats it into a paymentInfo entity
	 *
	 * @param response the response as string array
	 * @return the paymentInfo entity
	 */
	@Override
	public Object HandleResponse(Object response) {
		if(response instanceof String[])
		{
			String[] data = (String[])response;
			return new paymentInfo(data[0],data[1],data[2],data[3], data[4], data[5]);
		}
		return null;
	}

}
