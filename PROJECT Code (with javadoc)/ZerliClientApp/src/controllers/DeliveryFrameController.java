/*
 * 
 */
package controllers;

import java.io.IOException;

import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Class DeliveryFrameController is the controller part of the customer static environment GUI
 * The GUI does not change and Contains Customer functions 
 * The Controller implements the IContainable interface that give to the controller the ability to add different user controls
 */
public class DeliveryFrameController implements IContainable {

    /** The control container. */
    @FXML
    private AnchorPane controlContainer;

    /** The user label. */
    @FXML
    private Label userLabel;

    /**
     * Bell button.
     * When the ball pressed go to the notifications window
     * @param event the event
     */
    @FXML
    void bellBtn(MouseEvent event) {

    }

    /**
     * Exit pressed.
     * When the exit pressed exit from the program 
     * @param event the event
     */
    @FXML
    void exitPressed(MouseEvent event) {
     	ClientApp.ProtocolHandler.Invoke(RequestType.SetLogOut,null,null,false);
    	try {
			ClientApp.ClientConnection.closeConnection();
			//System.out.println(ClientApp.ClientConnection.isConnected());
			System.exit(0);
		} catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Press logout.
     * When the logout pressed go to the insert user details window
     * @param event the event
     */
    @SuppressWarnings("static-access")
	@FXML
    void pressLogout(ActionEvent event) {
    	LoginController.windowControl.stage.close();
    	Stage newStage = new Stage();
		Parent root = null;
		try 
		{
			ClientApp.ProtocolHandler.Invoke(RequestType.SetLogOut,null,null,false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/mainframes/Login.fxml"));
			root =  loader.load(); 
			Utilities.GenericUtilties.SetWindowMovable(root, newStage);
			Scene scene = new Scene(root);
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setScene(scene); 
			newStage.show();
			LoginController.windowControl.stage = newStage;	
		}
		catch (Exception e) {e.printStackTrace();	}
    }
    
    /**
     * Initialize the DeliveryMan name and get the branches to show in the comboBox
     */
    public void init() {
		userLabel.setText(ClientApp.UserID.toString());
		ClientApp.ProtocolHandler.Invoke(RequestType.GetBranch, "Delivery", null, true);
		LoginController.windowControl.putPipe("Branch", (String)ClientApp.ProtocolHandler.GetResponse(RequestType.GetBranch));
		LoginController.windowControl.setUserControl("/gui/usercontrols/DeliveryOrderManager.fxml");
	}

	/**
	 * Gets the control container.
	 * Return the user control that existing in the frame 
	 * @return the control container
	 */
	@Override
	public AnchorPane getControlContainer() {
		// TODO Auto-generated method stub
		return controlContainer;
	}

}
