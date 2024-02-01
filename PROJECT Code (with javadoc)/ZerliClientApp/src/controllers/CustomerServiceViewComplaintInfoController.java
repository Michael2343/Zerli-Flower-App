/*
 * 
 */
package controllers;

import java.time.format.DateTimeFormatter;
import Entities.Complaint;
import ProtocolHandler.RequestType;
import Utilities.GenericUtilties;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *The Class CustomerServiceViewComplaintInfoController is the controller part of the service GUI.
 *The Class give the ability to the service to watch customer complaints details
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerServiceViewComplaintInfoController implements UserControl{

	/** The complaint. */
	private Complaint complaint;
	
    /** The Answer text. */
    @FXML
    private TextArea AnswerText;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The branch label. */
    @FXML
    private Label branchLabel;

    /** The complaint text. */
    @FXML
    private Label complaintText;

    /** The date label. */
    @FXML
    private Label dateLabel;


    /** The refund field. */
    @FXML
    private TextField refundField;

    /** The user Id label. */
    @FXML
    private Label userIDlLabel;
    
    /** The error label. */
    @FXML
    private Label errorLabel;

    /**
     * End pressed.
     * When pressed End the service employee done to handle the complain and update the result into the system
     * @param event the event
     */
    @FXML
    void EndPressed(ActionEvent event) {
    	int refund;
    	try {
    		if(refundField.getText().equals(""))
    		{
    			refund =0;
    		}
    		else
    		{
    			refund = Integer.parseInt(refundField.getText());
    		}
    		if(refund < 0)
    		{
    			errorLabel.setVisible(true);
        		errorLabel.setText("refund must be positive");
    		}
    		else
    		{
    			complaint.setAnswer_text(AnswerText.getText());
    			complaint.setRefund(refund);
    			ClientApp.ProtocolHandler.Invoke(RequestType.ComplaintResponse,complaint , null, false);
    			ClientApp.ProtocolHandler.Invoke(RequestType.GetComplaints,  null, null, true);
    			CustomerServiceFrameController mfc= (CustomerServiceFrameController)LoginController.windowControl.peekPipe("service frame controller");
				mfc.editCircle(-1);
    			LoginController.windowControl.putPipe("Complaints", ClientApp.ProtocolHandler.GetResponse(RequestType.GetComplaints));
        		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServiceViewComplaints.fxml");
    		}
    	}catch (NumberFormatException e)
    	{
    		errorLabel.setVisible(true);
    		errorLabel.setText("Please enter a Number");
    	}
    		
    
    }

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Customer Service View Complaints)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServiceViewComplaints.fxml");
    }

	/**
	 * On enter.
	 * The first action to run - get the complaint details and show them 
	 */
	@Override
	public void onEnter() {
		
		errorLabel.setVisible(false);
		complaint = (Complaint)LoginController.windowControl.peekPipe("Complaint select");
		complaintText.setText(complaint.getComplain_text());
		userIDlLabel.setText(complaint.getUser_id());
		dateLabel.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(GenericUtilties.Convert_LocalDate_To_SQLDate(complaint.getComplain_time())));
		branchLabel.setText(complaint.getBranch());
		
	}

	/**
	 * On exit.
	 * The last action to run- reset the filled in details
	 */
	@Override
	public void onExit() {
		AnswerText.setText("");
		refundField.setText("");
		
	}

}
