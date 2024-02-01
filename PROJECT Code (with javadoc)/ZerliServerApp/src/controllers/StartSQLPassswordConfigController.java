package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import reports.PDFGenerator;
import server.*;

import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class StartSQLPassswordConfigController serves as
 * the controller part of the enter sql password GUI
 * it requests the user DB password and progresses to the server panel after
 * adequate validation.
 */
public class StartSQLPassswordConfigController {

    /** The connect button. */
    @FXML
    private Button connectBtn;

    /** The error label. */
    @FXML
    private Label errrorLabel;

    /** The exit button. */
    @FXML
    private ImageView exitBtn;

    /** The SQL Password text field. */
    @FXML
    private TextField SqlTextField;

    /** The enter SQL prompt label. */
    @FXML
    private Label enterSQLLabel;
    
    /**  No practical use. */
    @FXML
    private AnchorPane AncorePane;

	/** The stage. */
	private Stage stage;

    /**
     * This event invokes when the server operator attempts to
     * start the server.
     * In order to progress to the server panel the operator must
     * enter a valid mysql-db password
     * upon success the Server Panel window will open
     *
     * @param event the event
     */
    @FXML
    void connectPressed(ActionEvent event) {
    	String ip = SqlTextField.getText();
    	if(ip.equals("")) {
    		errrorLabel.setVisible(true);
    	}
    	else {
    		if(ServerConnSQL.startConn(SqlTextField.getText()))
    		{
				stage.close();
				Stage newStage = new Stage();
    			Parent root = null;
    			try 
    			{
    				FXMLLoader loader = new FXMLLoader();
    				loader.setLocation(getClass().getResource("/gui/ServerPanel.fxml"));
    				root =  loader.load(); 
    				Utilities.GenericUtilties.SetWindowMovable(root, newStage);
    				Scene scene = new Scene(root);
    				newStage.initStyle(StageStyle.UNDECORATED);
    				newStage.setScene(scene); 
    				newStage.show();
    			} 
    			catch (IOException e) 
    			{
    				Server.Log("Server", "Failed load root");
    				e.printStackTrace();
    			}
    		}
    		else {
    			SqlTextField.setText("");
        		errrorLabel.setVisible(true);	
    		} 
    		
    	}
    }

    /**
     * This event invokes when the server operator shuts down
     * the server.
     *
     * @param event the event
     */
    @FXML
    void exitPressed(MouseEvent event) {
    	System.exit(0);
    }
    
  
	/**
	 * Standard setter:
	 * Sets the stage.
	 *
	 * @param stage the new stage
	 */
	public void setStage(Stage stage) {
		this.stage= stage;
		
	}
    
    /**
     * An alternative way to perform enter using
     * only the ENTER key in the keyboard.
     *
     * @param event the event
     */
    @FXML
    void EnterPressed(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		connectPressed(null);
    }
}
