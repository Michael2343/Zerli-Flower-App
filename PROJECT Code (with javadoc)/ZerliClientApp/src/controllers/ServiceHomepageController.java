package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * The Class ServiceHomepageController - implements UserControl for switching sub windows.
 * Responsible : shows the Service user Home page image "Welcome to zerli".
 */
public class ServiceHomepageController implements UserControl{

	/** The active panel container - AnchorPane that contains all the FXML element */
    @FXML
    private AnchorPane activePanelContainer;

	/**
	 * Commands that the Controller do the screen will do while uploading window	 */
	@Override
	public void onEnter() {}

	/**
	 * Commands that Controller do the screen will do while existing window
	 */
	@Override
	public void onExit() {}

}
