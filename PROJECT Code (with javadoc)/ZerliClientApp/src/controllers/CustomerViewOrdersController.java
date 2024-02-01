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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *The Class CustomerViewOrdersController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to watch his orders
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerViewOrdersController implements UserControl{
	
	/** The order list. */
	ObservableList<Order> orderList;
	
    /** The Order view table. */
    @FXML
    private TableView<Order> OrderViewTable;
    
    /** The Order date column. */
    @FXML
    private TableColumn<Order, String> OrderDateCol;

    /** The Order number column. */
    @FXML
    private TableColumn<Order, Integer> OrderNumberCol;

    /** The price column. */
    @FXML
    private TableColumn<Order, Integer> priceCol;

    /** The shipping date column. */
    @FXML
    private TableColumn<Order, String> shippingDateCol;

    /** The status column. */
    @FXML
    private TableColumn<Order, String> statusCol;

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Customer Home Page)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerHomePage.fxml");
    }
    
    /**
     * Click on item.
     * When the event happened go to Customer View Orders Specific
     * @param event the event
     * @return if the event is not a double click end the event
     */
    @FXML
    void clickOnItem(MouseEvent event) {
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		Order chosenOrder = orderList.get(OrderViewTable.getSelectionModel().getSelectedIndex());
    		LoginController.windowControl.putPipe("chosenOrder", chosenOrder);
    		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerViewOrdersSpecific.fxml");
    	}
    	else return;
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
	 * The first action to run -  get the customer orders from the Data base and initialize the table columns
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		ClientApp.ProtocolHandler.Invoke(RequestType.GetAllCustomerOrders, null, null, true);
		orderList = (ObservableList<Order>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetAllCustomerOrders);
		
		OrderNumberCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));	
		priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));	
	    OrderDateCol.setCellValueFactory(new PropertyValueFactory<>("order_date"));
	    shippingDateCol.setCellValueFactory(new PropertyValueFactory<>("shipping_date"));
	    statusCol.setCellValueFactory(new PropertyValueFactory<>("order_status"));
	    OrderViewTable.setItems(orderList);
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {}

}
