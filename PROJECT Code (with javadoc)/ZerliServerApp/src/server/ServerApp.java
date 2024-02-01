package server;

import java.io.IOException;
import controllers.StartSQLPassswordConfigController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerApp.
 */
public class ServerApp extends Application{ 
	
	/** The Constant DEFAULT_PORT. */
	final public static int DEFAULT_PORT = 5555;
	
	/** The main server that handles the connectivity (OCSF Layer). */
	public static Server server = new Server(DEFAULT_PORT) ;
	
	/**
	 * The main method.
	 *
	 * @param args - the arguments [no use here]
	 */
	public static void main(String[] args) { 
        try 
        {
        	server.listen();
        } 
        catch (Exception ex) 
        {
          System.out.println("Failed listen clients!");
        }
        
		launch(args);
	}
	
	/**
	 * Start of javafx application.
	 *
	 * @param stage the main stage
	 */
	@Override
	public void start(Stage stage){
		StartSQLPassswordConfigController SQLpasswordController;
		Parent root = null;
		try 
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/StartSQLPassswordConfig.fxml"));
			root =  loader.load(); 
			Utilities.GenericUtilties.SetWindowMovable(root, stage);
			SQLpasswordController = loader.getController();
			Scene scene = new Scene(root);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene); 
			SQLpasswordController.setStage(stage);
			stage.show();
		} 
		catch (IOException e) 
		{
			System.out.println("Failed load root");
			e.printStackTrace();
		}
	}
	

}
