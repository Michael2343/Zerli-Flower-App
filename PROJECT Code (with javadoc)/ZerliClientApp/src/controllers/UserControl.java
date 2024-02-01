package controllers;

/**
 * The Interface UserControl - every sub window that we load to main screen is implements of UserControl Interface
 */
public interface UserControl {
	
	/**
	 * Commands that the Controller do the screen will do while uploading window
	 */
	void onEnter();
	
	/**
	 * Commands that Controller do the screen will do while existing window
	 */
	void onExit();

}
