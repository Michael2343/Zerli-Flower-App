/*
 * 
 */
package controllers;

import Entities.CustomerInfo;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * he Class CustomerAccountInfoController is the controller part of the Customer GUI.
 * The Class give the ability to the Customer to watch his personal details.
 * The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerAccountInfoController implements UserControl{

    /** The id. */
    @FXML
    private TextField ID;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The credit card. */
    @FXML
    private TextField creditCard;

    /** The cvv. */
    @FXML
    private TextField cvv;

    /** The email. */
    @FXML
    private TextField email;

    /** The first name. */
    @FXML
    private TextField firstName;

    /** The last name. */
    @FXML
    private TextField lastName;

    /** The month exp. */
    @FXML
    private TextField monthExp;

    /** The new customer label. */
    @FXML
    private Label newCustomerLabel;

    /** The phone. */
    @FXML
    private TextField phone;

    /** The year exp. */
    @FXML
    private TextField yearExp;

    /** The zerli coins. */
    @FXML
    private TextField zerliCoins;

    /**
     * Back pressed.
     * When the back pressed go to the previous page ( Customer Home Page) and if its needed save the typed data 
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerHomePage.fxml");
    }

	/**
	 * On enter.
	 * The first action to run - gets the customer detail to show
	 */
	@Override
	public void onEnter() {
		ClientApp.ProtocolHandler.Invoke(RequestType.GetCustomerInfo, null,null, true);
		CustomerInfo customer =(CustomerInfo) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCustomerInfo);
		firstName.setText(customer.getFirstName());
		lastName.setText(customer.getLastName());
		ID.setText(customer.getID());
		email.setText(customer.getEmail());
		phone.setText(customer.getPhone());
		creditCard.setText(customer.getCreditCard());
		cvv.setText(customer.getCvv());
		monthExp.setText(customer.getExpMonth());
		yearExp.setText(customer.getExpYear());
		zerliCoins.setText(customer.getZerliCoins());
		newCustomerLabel.setVisible(customer.isNewCustomer());
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
	}

}
