/*
 * 
 */
package controllers;

import Entities.CatalogType;
import Entities.ItemInList;
import Entities.ItemType;
import Entities.NewItem;
import Entities.NotificationInTable;
import Entities.Order;
import Entities.OrderStatus;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *The Class ManagerViewOrderDetailsController is the controller part of the Manager GUI.
 *The Class give the ability to the Manager to view the orders details 
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ManagerViewOrderDetailsController implements UserControl{

	/** The order. */
	private Order order;
	
	/** The future status. */
	private String futureStatus;
	
	/** The order items. */
	private ObservableList<ItemInList> orderItems;
	
	/** The observable list. */
	private ObservableList<ItemInList> observableList;
	
    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;


    /** The item table. */
    @FXML
    private TableView<ItemInList> itemTable;
    
    /** The catalog column. */
    @FXML
    private TableColumn<ItemInList, CatalogType> catalogColumn;


    /** The cost label. */
    @FXML
    private Label costLabel;

    /** The item ID column. */
    @FXML
    private TableColumn<ItemInList, Integer> itemIDColumn;

    /** The item name column. */
    @FXML
    private TableColumn<ItemInList, String> itemNameColumn;

    /** The order date label. */
    @FXML
    private Label orderDateLabel;

    /** The order number label. */
    @FXML
    private Label orderNumberLabel;

    /** The payment method label. */
    @FXML
    private Label paymentMethodLabel;

    /** The price column. */
    @FXML
    private TableColumn<ItemInList, Integer> priceColumn;

    /** The quantity column. */
    @FXML
    private TableColumn<ItemInList, Integer> quantityColumn;

    /** The shipping date label. */
    @FXML
    private Label shippingDateLabel;

    /** The shipping method label. */
    @FXML
    private Label shippingMethodLabel;

    /** The status label. */
    @FXML
    private Label statusLabel;

    /** The type column. */
    @FXML
    private TableColumn<ItemInList, ItemType> typeColumn;
    

    /** The full list button. */
    @FXML
    private Button fullListBtn;
    
    /** The item list label. */
    @FXML
    private Label itemListLbl;
	
   /** The approve button. */
   @FXML
    private Button approveBtn;
    
    
    /**
     * New item click.
     * When pressed new item , show the all order details
     * @param event the event
     */
    @FXML
    void newItemClick(MouseEvent event) {
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		if(itemTable.getSelectionModel().getSelectedItem() instanceof NewItem) {
    			
    			NewItem chosenItem = (NewItem) itemTable.getSelectionModel().getSelectedItem();
    			observableList = FXCollections.observableArrayList(chosenItem.getAssemble());
        		
    			itemTable.setItems(observableList);
        		fullListBtn.setVisible(true);
        		itemListLbl.setText("New Item\nList:");
    		}	
    		else return;
    	}
    	else return;
    }
    

    /**
     * Show full specks.
     *
     * @param event the event
     */
    @FXML
    void showFullSpecs(ActionEvent event) {
    	fullListBtn.setVisible(false);
		itemListLbl.setText("Items\nList:");
		
    	observableList = orderItems;
    	itemTable.setItems(observableList);
    }
    

    /**
     * Approve pressed.
     * When pressed approve the customer pending order approved and notification will send to the customer
     * @param event the event
     */
    @FXML
    void approvePressed(ActionEvent event) {
    	Order order = (Order) LoginController.windowControl.peekPipe("Order select");
    	
    	ClientApp.ProtocolHandler.Invoke(RequestType.ConfirmOrder, order, futureStatus, true);
    	int refund = (int) ClientApp.ProtocolHandler.GetResponse(RequestType.ConfirmOrder);
    	NotificationInTable notification = new NotificationInTable();
    	notification.setTo(order.getUserID());
    	notification.setFrom("Manager");
    	
    	if(futureStatus.equals("confirmed")) {
    		notification.setContent("Order number " + order.getOrderID() + " has been confirmed"
    				+ "\nSee full info in \"My Orders\" tab");
    	}
    	else {
    		notification.setContent("Order number " + order.getOrderID() + " has been canceled"
    				+ "\nPlease receive " + refund + " Zerli coins as refund");
    	}
    	
    	ClientApp.ProtocolHandler.Invoke(RequestType.SendNotification, notification, null, false);
    	
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerOrderManager.fxml");
    }

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Manager Order Manager)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerOrderManager.fxml");
    }

	/**
	 * On enter.
	 * The first action to run - Initialize the table columns and set the order details data into the table
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		fullListBtn.setVisible(false);
		itemListLbl.setText("Items\nList:");
		
		order = (Order)LoginController.windowControl.peekPipe("Order select");
		catalogColumn.setCellValueFactory(new PropertyValueFactory<>("catalog_Type"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("item_id"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("item_type"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		orderDateLabel.setText(order.getOrder_date());
		shippingDateLabel.setText(order.getShipping_date());
		paymentMethodLabel.setText(order.getPayment_method());
		orderNumberLabel.setText(order.getOrderID());
		costLabel.setText(String.valueOf(order.getTotalPrice())+ " " + Utilities.Constants.SHEKEL);
		statusLabel.setText(order.getOrder_status());
		shippingMethodLabel.setText(order.getShipping_method());
		
		if(order.getStatus() == OrderStatus.pending_cancel ) {
			futureStatus = "canceled";
			approveBtn.setText("Cancel order");
		}
		else if(order.getStatus() == OrderStatus.pending_confirm ) {
			futureStatus = "confirmed";
			approveBtn.setText("Approve order");

		}
		
		ClientApp.ProtocolHandler.Invoke(RequestType.GetItemsOfOrder, LoginController.windowControl.peekPipe("Order select"), null, true);
		orderItems = (ObservableList<ItemInList>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetItemsOfOrder);
		observableList = orderItems;
		itemTable.setItems(observableList);
		
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
	}

}
