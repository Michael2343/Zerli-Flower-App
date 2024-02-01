package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * The Class WindowControl - incharge of window change between screens
 * and save information that shared for several windows 
 */
public class WindowControl {

	/** The stage - saves the stage for all the windows*/
	public static Stage stage;
	
	/** The frameController - saves the controller of the main window */
	public IContainable frameController;
	
	/** The currentControl - saves the controller of the sub windows */
	private UserControl currentControl;

	/** The map - singleton map for all loader.load() of FXMLs */
	private Map<String,Parent> map = new HashMap<>();
	
	/** The uc_map - singleton map for all sub windows controllers */
	private Map<String,UserControl> uc_map = new HashMap<String, UserControl>();
	
	/** The pipe - save information that shared for several windows */
	private Map<String, Object> pipe = new HashMap<>();
	
	/**
	 * Instantiates a new IContainable main screen controller.
	 *
	 * @param frameController -  the IContainable controller
	 */
	public WindowControl(IContainable frameController) {
		this.frameController = frameController;
	}
	
	/**
	 * Insert information to pipe map.
	 *
	 * @param tag - Key is a string
	 * @param data - value is an object
	 */
	public void putPipe(String tag,Object data) {
		pipe.put(tag, data);
		
	}
	
	/**
	 * Remove information from pipe and send the removed object
	 *
	 * @param tag - Key is a string
	 * @return data - value is an object
	 */
	public Object getPipe(String tag) {
		Object object = pipe.get(tag);
		pipe.remove(tag);
		return object;
	}
	
	/**
	 * Send the value object that matched to key.
	 *
	 * @param tag - Key is a string
	 * @return data - value is an object
	 */
	public Object peekPipe(String tag) {
		return pipe.get(tag);
	}
	
	/**
	 * setUserControl - open the next FXML sub window (FXML and Controller).
	 *
	 * @param path - path of the FXML location and name
	 */
	public void setUserControl(String path)
	{
		if(currentControl != null) {
			currentControl.onExit();
		}
	
		if(map.get(path) == null ) {
			FXMLLoader loader;
			Parent root;
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(path));
			root = null;
			try {root = loader.load();	} 
			catch (IOException e) {e.printStackTrace();} 
			map.put(path, root);
			currentControl = loader.getController();
			uc_map.put(path, currentControl);
			
		}
		else {
			currentControl = uc_map.get(path);
		}
		frameController.getControlContainer().getChildren().clear();
		frameController.getControlContainer().getChildren().add(map.get(path));
		currentControl.onEnter();
	}

	/**
	 * Refresh window and execute onEnter of the controller of the sub windows.
	 */
	public void Refresh() {
		currentControl.onEnter();
	}
}
