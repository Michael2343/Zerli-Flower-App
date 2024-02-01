package client;

import java.io.IOException;
import Entities.Access;
import ProtocolHandler.Protocol;
import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * The Class ClientApp connects the client to the server.
 */
public class ClientApp extends Application { 
	
	/** The Constant DEFAULT_PORT. */
	final public static int DEFAULT_PORT = 5555;
	
	/** The Protocol handler. */
	public static Protocol ProtocolHandler  = new Protocol();
	
	/** The User ID. */
	public static String UserID = "NO-AUTH";
	
	/** The User status. */
	public static Access UserStatus = Access.noaut;
	
	/** The Client connection. */
	public static ClientConn ClientConnection;
	
	/**
	 * Start.
	 *
	 * @param stage the ip configuration page
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void start(Stage stage) throws IOException {	
		Parent root = null;
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/mainframes/StartIPconfig.fxml"));
			root =  loader.load(); 
			Utilities.GenericUtilties.SetWindowMovable(root, stage);
			Scene scene = new Scene(root);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene); 	
			stage.show();
			LoginController.windowControl.stage = stage;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
