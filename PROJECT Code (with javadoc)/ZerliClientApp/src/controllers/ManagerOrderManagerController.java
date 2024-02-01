/*
 * 
 */
package controllers;

import Entities.Order;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *The Class ManagerOrderManagerController is the controller part of the Manager GUI.
 *The Class give the ability to the Manager to manage the orders (approve)
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ManagerOrderManagerController implements UserControl {

	/** The observable list. */
	private ObservableList<Order> observableList;
	
	   /** The branch name. */
   	@FXML
	    private Label branchName;

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

	    /** The shipping method column. */
    	@FXML
	    private TableColumn<Order, String> shippingMethodColumn;

	    /** The status column. */
    	@FXML
	    private TableColumn<Order, String> statusColumn;


	    /**
    	 * Back pressed.
    	 * When the back pressed go to the previous page (Manager Home Page)
    	 * @param event the event
    	 */
    	@FXML
	    void backPressed(ActionEvent event) 
	    {
	    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerHomePage.fxml");
	    }

	    /**
    	 * Gets the order info.
    	 * @param event the event
    	 * @return the order info
    	 */
    	@FXML
	    void getOrderInfo(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			Order OrderChoose = ordersTable.getSelectionModel().getSelectedItem();
			LoginController.windowControl.putPipe("Order select", OrderChoose );
			LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerViewOrderDetails.fxml");
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
	 * The first action to run - Initialize the table columns and set the branch order data into the table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		branchName.setText((String) LoginController.windowControl.peekPipe("Branch"));
		orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
		orderIDcolumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		expectedDateColumn.setCellValueFactory(new PropertyValueFactory<>("shipping_date"));
		shippingMethodColumn.setCellValueFactory(new PropertyValueFactory<>("shipping_method"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("Order_status"));
		
		ClientApp.ProtocolHandler.Invoke(RequestType.GetOrdersByBranch,  LoginController.windowControl.peekPipe("Branch"), "manager", true);
		observableList = (ObservableList<Order>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetOrdersByBranch);
		
		ordersTable.setItems(observableList);
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
	}



}
