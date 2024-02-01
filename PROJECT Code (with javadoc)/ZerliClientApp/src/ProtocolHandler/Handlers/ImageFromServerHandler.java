package ProtocolHandler.Handlers;

import java.util.ArrayList;

import ProtocolHandler.IHandler;

/**
 * The Class ImageFromServerHandler perform an generic to non-generic cast
 * by taking an arraylist of generic type and converting it to a non generic type
 */
public class ImageFromServerHandler implements IHandler {

	/**
	 * Handle the generic type.
	 *
	 * @param response the arraylist in generic type
	 * @return the arraylist in non generic format
	 */
	@Override
	public Object HandleResponse(Object response) {
		if(response instanceof ArrayList<?> )
		{
		@SuppressWarnings("unchecked")
		ArrayList<byte[]> listOfImages= (ArrayList<byte[]>)response;
		 return listOfImages;
		}
		return null;
	}

}
