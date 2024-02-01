/*
 * 
 */
package controllers;

import Entities.NotificationInTable;
import Entities.Order;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *The Class DeliveryViewOrderDetailsController is the controller part of the Delivery GUI.
 *The Class give the ability to the DeliveryMan to accept the order and submit it into the Data Base
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class DeliveryViewOrderDetailsController implements UserControl {

	 /** The active panel container. */
 	@FXML
	    private AnchorPane activePanelContainer;

	    /** The address label. */
    	@FXML
	    private Label addressLbl;

	    /** The city label. */
    	@FXML
	    private Label cityLbl;

	    /** The cost label. */
    	@FXML
	    private Label costLabel;

	    /** The full name label. */
    	@FXML
	    private Label fullnameLbl;

	    /** The order date label. */
    	@FXML
	    private Label orderDateLabel;

	    /** The order number label. */
    	@FXML
	    private Label orderNumberLabel;

	    /** The payment method label. */
    	@FXML
	    private Label paymentMethodLabel;

	    /** The phone label. */
    	@FXML
	    private Label phoneLbl;

	    /** The shipping date label. */
    	@FXML
	    private Label shippingDateLabel;


    /**
     * Back pressed.
     * When the back pressed go to the previous page (Delivery Order Manager)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/DeliveryOrderManager.fxml");
    }
 
    /**
     * Done pressed.
     * When pressed done the notification update , check if refund needed, 
     * Update that the order was send to the customer.
     * @param event the event
     */
    @FXML
    void donePressed(ActionEvent event) {
    	Order order = (Order) LoginController.windowControl.peekPipe("Order select");
    	ClientApp.ProtocolHandler.Invoke(RequestType.EndOrder, order, null, true);
    	int refund = (int) ClientApp.ProtocolHandler.GetResponse(RequestType.EndOrder);
    	
    	NotificationInTable notification = new NotificationInTable();
    	notification.setTo(order.getUserID());
    	notification.setFrom("Delivery");
    	
    	if(refund==0) {
    		notification.setContent("Order number " + order.getOrderID() + " has been completed"
    				+ "\nSee full info in \"My Orders\" tab");
    	}
    	else {
    		notification.setContent("Order number " + order.getOrderID() + " has arrived late"
    				+ "\nWe apologize! please receive " + refund + " Zerli coins as refund");
    	}
    	
    	ClientApp.ProtocolHandler.Invoke(RequestType.SendNotification, notification, null, false);
    	
    	LoginController.windowControl.setUserControl("/gui/usercontrols/DeliveryOrderManager.fxml");
    }

	/**
	 * On enter.
	 * The first action to run - set the order details 
	 */
	@Override
	public void onEnter() {
		Order order = (Order)LoginController.windowControl.peekPipe("Order select");
		orderDateLabel.setText(order.getOrder_date());
		shippingDateLabel.setText(order.getShipping_date());
		paymentMethodLabel.setText(order.getPayment_method());
		orderNumberLabel.setText(order.getOrderID());
		costLabel.setText(String.valueOf(order.getTotalPrice())+ " " + Utilities.Constants.SHEKEL);
		addressLbl.setText(order.getAddress());
		cityLbl.setText(order.getCity());
		fullnameLbl.setText(order.getFullname());
		phoneLbl.setText(order.getPhone());
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {}

}
