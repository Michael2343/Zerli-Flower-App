package controllers;

import client.ClientConn;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Class StartIPconfigController - insertion of the IP address in order to connect the server.
 */
public class StartIPconfigController {

    /** The connectBtn - Button for connection . */
    @FXML
    private Button connectBtn;

    /** The enterIPLabel - constant Label main header "Enter IP". */
    @FXML
    private Label enterIPLabel;

    /** The errrorLabel -  Label for errors. */
    @FXML
    private Label errrorLabel;

    /** The ipTextField- TextField for entering the IP address text . */
    @FXML
    private TextField ipTextField;

    /**
     * Button for connection pressed - execute connection to server.
  	 * @param event - ActionEvent of button pressed
     */
    @FXML
    void connectPressed(ActionEvent event) {
    	startConnaction();
    }

    /**
     * "ENTER" is pressed when cursor in label IP.
  	 * @param event - Enter pressed
     */
    @FXML
    void Enter(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		startConnaction();
    }
    
    /**
     * "X" window image is pressed - exit from the entire program
  	 * @param event - ActionEvent of button pressed
     */
    @FXML
    void exitPressed(MouseEvent event) {
    	System.exit(0);
    }
    
    /**
     * Connection to server with the IP address from label.
     * When IP address label is empty - Default is localhost.
     */
    private void startConnaction() 
    {
    	errrorLabel.setVisible(false);
    	String ip = ipTextField.getText();
    	if(ip.equals("")) {
    		ip = "localhost";
    	}
    	ClientApp.ClientConnection = new ClientConn(ip, ClientApp.DEFAULT_PORT);
    	if(!ClientApp.ClientConnection.isConnected()) {
    		errrorLabel.setVisible(true);
    	}
    	else {
    		LoginController.windowControl.stage.close();
    		Stage newstage = new Stage();
    		Parent root = null;
    		try 
    		{
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/gui/mainframes/Login.fxml"));
    			root =  loader.load(); 
    			Utilities.GenericUtilties.SetWindowMovable(root, newstage);
    			Scene scene = new Scene(root);
    			newstage.initStyle(StageStyle.UNDECORATED);
    			newstage.setScene(scene); 
    			newstage.show();
    			LoginController.windowControl.stage = newstage;
    			
    		}
    		catch (Exception e) {e.printStackTrace();	}
    	}
    }
}
