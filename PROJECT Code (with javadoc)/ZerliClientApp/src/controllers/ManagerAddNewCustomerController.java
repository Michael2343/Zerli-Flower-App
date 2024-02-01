/*
 * 
 */
package controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import Entities.NotificationInTable;
import Entities.PendingClientInfo;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *The Class ManagerAddNewCustomerController is the controller part of the Manager GUI.
 *The Class give the ability to the Manager to add new customer 
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ManagerAddNewCustomerController implements UserControl{

    /** The add error. */
    @FXML
    private Label addError;
    
	/** The Client info window. */
	@FXML
    private AnchorPane ClientInfoWindow;
	
    /** The search error label. */
    @FXML
    private Label searchErrorLBl;

    /** The ID search. */
    @FXML
    private TextField IDSearch;

    /** The ID text. */
    @FXML
    private TextField IDTxt;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The card number 1 text . */
    @FXML
    private TextField cardNumTxt1;

    /** The card number 2 text. */
    @FXML
    private TextField cardNumTxt2;

    /** The card number 3 text. */
    @FXML
    private TextField cardNumTxt3;

    /** The card number 4 text. */
    @FXML
    private TextField cardNumTxt4;

    /** The credit card dialog. */
    @FXML
    private AnchorPane creditCardDialog;

    /** The cvv text. */
    @FXML
    private TextField cvvText;

    /** The email text. */
    @FXML
    private TextField emailTxt;

    /** The expired month CB. */
    @FXML
    private ComboBox<String> expMonthCB;

    /** The first name text. */
    @FXML
    private TextField firstNameTxt;

    /** The last name text. */
    @FXML
    private TextField lastNameTxt;

    /** The password text. */
    @FXML
    private TextField passwordTxt;

    /** The phone text. */
    @FXML
    private TextField phoneTxt;

    /** The user name text. */
    @FXML
    private TextField userNameTxt;

    /** The year CB. */
    @FXML
    private ComboBox<String> yearCB;
    
    /** The pending info. */
    PendingClientInfo pendingInfo;
    
    @FXML
    void helpBtnPressed(ActionEvent event) {
    	Utilities.GenericUtilties.ShowHelp("", "help_account_adding.png");
    }
    
    /**
     * Search pressed.
     * When pressed search ,search the customer by his id and if he is exist show his data 
     * @param event the event
     */
    @FXML
    void SearchPressed(MouseEvent event) {
		ClientInfoWindow.setVisible(false);

    	String IDtxt = IDSearch.getText();
    	onExit();
    	if(IDtxt.length() == 0 ) {
    		searchErrorLBl.setText("Please Enter ID");
    		return;
    	}
    	else if(IDtxt.length()!= 9 ) {
    		searchErrorLBl.setText("Invalid ID");
    		return;
    	}
    	
    	searchErrorLBl.setText("");
    	ClientApp.ProtocolHandler.Invoke(RequestType.GetPendingClient, null, IDtxt, true);
    	pendingInfo = (PendingClientInfo)ClientApp.ProtocolHandler.GetResponse(RequestType.GetPendingClient);
    	if(pendingInfo == null) {
    		searchErrorLBl.setText("Customer already registered");
    		searchErrorLBl.setVisible(true);
    	}
    	else if(pendingInfo.getUserID().equals("-1"))
    	{
    		searchErrorLBl.setText("Customer does not exist");
    		searchErrorLBl.setVisible(true);
    	}
    	else { 
    		ClientInfoWindow.setVisible(true);
    		IDTxt.setText(pendingInfo.getID());
    		firstNameTxt.setText(pendingInfo.getFirstName());
    		lastNameTxt.setText(pendingInfo.getLastName());
    		phoneTxt.setText(pendingInfo.getPhone());
    		emailTxt.setText(pendingInfo.getEmail());
    		userNameTxt.setText(pendingInfo.getUserID());
    	}
    }
    
    /**
     * Adds the customer pressed.
     * When pressed add  , add the customer into the system and give the customer notification about his changing status
     * @param event the event
     */
    @FXML
    void addCustomerPressed(MouseEvent event) {
    	
    	if(!validateInput()) return;
    	else {
    		pendingInfo.setCreditPhrases(new String[] {cardNumTxt1.getText(),cardNumTxt2.getText(),cardNumTxt3.getText(),cardNumTxt4.getText()});
    		pendingInfo.setCVV(cvvText.getText());
    		pendingInfo.setExpirationMonth(expMonthCB.getValue());
    		pendingInfo.setExpirationYear(yearCB.getValue());
    		pendingInfo.setPassword(passwordTxt.getText());
    		ClientApp.ProtocolHandler.Invoke(RequestType.RegisterClient, pendingInfo, pendingInfo.getUserID(), false);
    		NotificationInTable notification = new NotificationInTable();
    		notification.setTo(pendingInfo.getUserID());
    		notification.setFrom("Manager");
    		notification.setContent("Welcome to Zerli online Shop\nPlease receive 20% off on first purchase coupon");
    		ClientApp.ProtocolHandler.Invoke(RequestType.SendNotification,notification,null,false);
    		Alert confirmAlert = new Alert(AlertType.NONE);
    		confirmAlert.setTitle("Add new customer Information");
    		confirmAlert.setContentText("Customer with ID " + pendingInfo.getID() + " added Successfully\n"
    				+ "Message had been send to customer");
    		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    		confirmAlert.getDialogPane().getButtonTypes().add(ok);
    		Optional<ButtonType> result = confirmAlert.showAndWait();
    		result.ifPresent(response -> { 
    			if(response == ok) {
    				onEnter();
    			}
    		});
    	}
    }

	/**
	 * Validate input.
	 *
	 * @return true, if successful
	 */
	private boolean validateInput() {
		StringBuilder error = new StringBuilder("");
    	boolean valid = true;
    	
    	if(passwordTxt.getText().equals("") )
    	{
    		error.append("Password field missing\n");
    		valid = false;
    	}
    	else if(passwordTxt.getText().length() < 4 ) {
    		error.append("Password need 4 characters minimum\n");
    		valid = false;
    	}
    	
    	var creds = new String[] {cardNumTxt1.getText(),cardNumTxt2.getText(),cardNumTxt3.getText(),cardNumTxt4.getText()};
    	for(String s : creds)
    	{
    		if(s.length() != 4)
    		{
    			error.append("Please enter valid credit card\n");
    			valid = false;
    			break;
    		}
    		try
    		{
    			Integer.parseInt(s);
    		}
    		catch(NumberFormatException e)
    		{
    			error.append("Please enter valid credit card\n");
    			valid = false;
    			break;
    		}
    	}
    
    	
    	if(yearCB.getValue() == null || expMonthCB.getValue() == null)
    	{
    		error.append("Please enter a valid expiration date\n");
    		valid = false;
    	}
    	else {
    		int year= Integer.parseInt(yearCB.getValue());
    		int month = Integer.parseInt(expMonthCB.getValue());
    		if(LocalDateTime.now().getYear() == year) {
    			if(LocalDateTime.now().getMonthValue() > month) {
    				error.append("Please enter a valid expiration date\n");
    				valid = false;
    			}
    		}
    	}
    	
    	if(cvvText.getText().equals(""))
    	{
    		error.append("CVV Cannot be empty\n");
    		valid = false;
    	}
    	else if(cvvText.getText().length() != 3 ) {
    		error.append("CVV must be 3 digits\n");
    		valid = false;
    	}
    	
        addError.setText(error.toString());
    	
    	return valid;
	}
	

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Manager Account Management)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
		LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerAccountManagment.fxml");
    }

	/**
	 * On enter.
	 * The first action to run - set the Instructions labels 
	 */
	@Override
	public void onEnter() {
		ClientInfoWindow.setVisible(false);
		searchErrorLBl.setText("");
		IDSearch.setText("");
		addError.setText("");
		
		var currentYear = LocalDateTime.now().toLocalDate().getYear();
		var years = new ArrayList<String>();
		var months = new ArrayList<String>();
		for(var i = currentYear;i < currentYear + 12;i++)
			years.add(i + "");
		for(var i = 1;i<=12;i++)
			months.add((i+"").length() == 1 ? ("0"+ i) : i + "");
		yearCB.setItems(FXCollections.observableArrayList(years));
		expMonthCB.setItems(FXCollections.observableArrayList(months));
	}

	/**
	 * On exit.
	 * The last action to run - reset all labels to ""
	 */
	@Override
	public void onExit() {
		searchErrorLBl.setText("");
		IDSearch.setText("");
		addError.setText("");
		passwordTxt.setText("");
		cardNumTxt1.setText("");
		cardNumTxt2.setText("");
		cardNumTxt3.setText("");
		cardNumTxt4.setText("");
		cvvText.setText("");
		expMonthCB.setValue(null);
		yearCB.setValue(null);
	}  
}
