/*
 * 
 */
package controllers;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import Entities.Complaint;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/** 
 *The Class CustomerServicePostComplaintController is the controller part of the service GUI.
 *The Class give the ability to the service to post customer complaints into the system
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerServicePostComplaintController implements UserControl {

    /** The feed back text. */
    @FXML
    private TextArea feedBackText;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The branch field. */
    @FXML
    private ComboBox<String> branchField;

    /** The error label. */
    @FXML
    private Label errorLabel;

    /** The user ID field. */
    @FXML
    private TextField userIDField;

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Customer Service View Complaints)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServiceViewComplaints.fxml");
    }
    @FXML
    void helpBtnPressed(ActionEvent event) {
    	Utilities.GenericUtilties.ShowHelp("Posting a Complaint", "help_complaint.png");
    }
    /**
     * Post press.
     * When pressed post the complain get checked and update in to the system 
     * @param event the event
     */
    @FXML
    void postPress(ActionEvent event) {
    	//check if combo box value selected
    	try {
    			branchField.getValue().equals("");
    		}catch(NullPointerException e)
    		{
    			errorLabel.setVisible(true);
    			errorLabel.setText("Please select branch");
    			return;
    		}
    	//
    	Complaint complaint = new Complaint();
    	complaint.setIDnumber(userIDField.getText());
    	complaint.setBranch(branchField.getValue());
    	complaint.setComplain_text(feedBackText.getText());
    	complaint.setComplain_time(Timestamp.valueOf(LocalDateTime.now()));
    	ClientApp.ProtocolHandler.Invoke(RequestType.MakeComplaint,complaint,null,true);
    	if(((String)ClientApp.ProtocolHandler.GetResponse(RequestType.MakeComplaint)).equals("done"))
    	{
    		ClientApp.ProtocolHandler.Invoke(RequestType.GetComplaints,  null, null, true);
    		CustomerServiceFrameController mfc= (CustomerServiceFrameController)LoginController.windowControl.peekPipe("service frame controller");
			mfc.editCircle(1);
        	LoginController.windowControl.putPipe("Complaints", ClientApp.ProtocolHandler.GetResponse(RequestType.GetComplaints));
        	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServiceViewComplaints.fxml");
    	}
    	else 
    	{
    		errorLabel.setVisible(true);
    		errorLabel.setText((String)ClientApp.ProtocolHandler.GetResponse(RequestType.MakeComplaint));
    	}
    	
    }

	/**
	 * On enter.
	 * The first action to run - get the branches to show in the comboBox
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		errorLabel.setVisible(false);
		ClientApp.ProtocolHandler.Invoke(RequestType.GetBranches,  null, null, true);
		branchField.setItems((ObservableList<String>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetBranches));
	}

	/**
	 * On exit.
	 * The last action to run- reset the filled in complaint details 
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		feedBackText.setText("");
		userIDField.setText("");
		branchField.getItems().clear();
	}

}
