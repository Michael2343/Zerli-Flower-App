/*
 * 
 */
package controllers;

import javafx.scene.layout.AnchorPane;

/**
 * The Interface IContainable give to the class the ability to contain differers user GUI
 * And create a dynamic GUI approach.
 * The Class that implements IContainable interface responsible for the static environment GUI and the functions
 */
public interface IContainable {
	
	/**
	 * Gets the control container.
	 * Return the user control that existing in the frame
	 * @return the control container
	 */
	public AnchorPane getControlContainer();

}
