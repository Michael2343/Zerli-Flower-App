package ProtocolHandler.Handlers;

import java.util.ArrayList;

import Entities.Order;
import ProtocolHandler.IHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Class ArrayListToObservableHandler is a handler which receives an array-list
 * of any type and casts it into an observable collection.
 */
public class ArrayListToObservableHandler implements IHandler{

	/**
	 * Handle response.
	 *
	 * @param response the response
	 * @return the object
	 */
	@Override
	public Object HandleResponse(Object response) {
		if(response instanceof ArrayList<?>) {
			return FXCollections.observableArrayList((ArrayList<?>)response);
		}
		return null;
	}

}
