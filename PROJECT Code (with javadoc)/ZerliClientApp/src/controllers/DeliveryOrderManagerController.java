/*
 * 
 */
package controllers;

import Entities.Order;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *The Class AddNewSurveyController is the controller part of the Delivery GUI.
 *The Class give the ability to the DeliveryMan to
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class DeliveryOrderManagerController implements UserControl{

	/** The observable list. */
	private ObservableList<Order> observableList;
	
	    /** The address column. */
    	@FXML
	    private TableColumn<Order, String> adressCol;
	
	    /** The branch name. */
    	@FXML
	    private Label branchName;
	
	    /** The city column. */
    	@FXML
	    private TableColumn<Order, String> cityCol;

	    /** The expected date column. */
    	@FXML
	    private TableColumn<Order, String> expectedDateColumn;

	    /** The order date column. */
    	@FXML
	    private TableColumn<Order, String> orderDateColumn;

	    /** The order Id column. */
    	@FXML
	    private TableColumn<Order, String> orderIDcolumn;

	    /** The orders table. */
    	@FXML
	    private TableView<Order> ordersTable;


	    /** The price column. */
    	@FXML
	    private TableColumn<Order, Integer> priceColumn;

	    /**
    	 * Gets the order info.
    	 * When the event happened go to Delivery View Order Details
    	 * If the event is not a double click end the event
    	 * @param event the event
    	 */
    	@FXML
	    void getOrderInfo(MouseEvent event) {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				Order OrderChoose = ordersTable.getSelectionModel().getSelectedItem();
				LoginController.windowControl.putPipe("Order select", OrderChoose );
				LoginController.windowControl.setUserControl("/gui/usercontrols/DeliveryViewOrderDetails.fxml");
			}
	    }
	    
	    /**
    	 * Refresh pressed.
    	 * When pressed refresh the table set the last update data
    	 * @param event the event
    	 */
    	@FXML
	    void refreshPressed(MouseEvent event) {
	    	onEnter();
	    }
		
		/**
		 * On enter.
		 * The first action to run - initialize the table columns and get the branches names
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onEnter() {
			branchName.setText((String) LoginController.windowControl.peekPipe("Branch"));
			orderIDcolumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
			orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
			expectedDateColumn.setCellValueFactory(new PropertyValueFactory<>("shipping_date"));
			adressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
			cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
			
			ClientApp.ProtocolHandler.Invoke(RequestType.GetOrdersByBranch,  LoginController.windowControl.peekPipe("Branch"), "delivery", true);
			observableList = (ObservableList<Order>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetOrdersByBranch);
			ordersTable.setItems(observableList);
		}

		/**
		 * On exit.
		 */
		@Override
		public void onExit() {}

}
