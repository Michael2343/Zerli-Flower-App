/*
 * 
 */
package controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import Entities.CatalogType;
import Entities.ItemInList;
import Entities.ItemType;
import Entities.NewItem;
import Entities.NotificationInTable;
import Entities.Order;
import Entities.OrderStatus;
import Entities.ShippingMethods;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *The Class CustomerViewOrdersSpecificController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to watch the order details and information
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerViewOrdersSpecificController implements UserControl{
	
	/** The item list view. */
	ObservableList<ItemInList> itemListView =
			FXCollections.observableArrayList(new ArrayList<ItemInList>());
	    
	/** The order. */
	private Order order;
	
    /** The Item table. */
    @FXML
    private TableView<ItemInList> ItemTable;
	
    /** The catalog item. */
    @FXML
    private TableColumn<ItemInList, CatalogType> catalogItem;

    /** The name item. */
    @FXML
    private TableColumn<ItemInList, String> nameItem;
    
    /** The price item. */
    @FXML
    private TableColumn<ItemInList, Integer> priceItem;

    /** The quantity item. */
    @FXML
    private TableColumn<ItemInList, Integer> quantityItem;
    
    /** The type item. */
    @FXML
    private TableColumn<ItemInList, ItemType> typeItem;

	
    /** The branch name. */
    @FXML
    private Label brachName;

    /** The cancel order. */
    @FXML
    private Button cancelOrder;

    /** The full address. */
    @FXML
    private Label fullAddress;

    /** The greeting card text. */
    @FXML
    private Label greetingCardText;

    /** The order date. */
    @FXML
    private Label orderDate;

    /** The order number. */
    @FXML
    private Label orderNumber;

    /** The payment method. */
    @FXML
    private Label paymentMethod;

    /** The price. */
    @FXML
    private Label price;

    /** The ship method. */
    @FXML
    private Label shipMethod;

    /** The shipping date. */
    @FXML
    private Label shippingDate;

    /** The status order. */
    @FXML
    private Label statusOrder;
    
    /** The item list label. */
    @FXML
    private Label itemListLbl;
    
    /** The full list button. */
    @FXML
    private Button fullListBtn;
    
    @FXML
    private Button pickupBtn;
    
    /**
     * Click on new item.
     * When the event happened show the NewItem components 
     * @param event the event
     * @return if the event is not a double click end the event
     */
    @FXML
    void clickOnNewItem(MouseEvent event) {
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		if(ItemTable.getSelectionModel().getSelectedItem() instanceof NewItem) {
    			
    			NewItem chosenItem = (NewItem) ItemTable.getSelectionModel().getSelectedItem();
    			itemListView = FXCollections.observableArrayList(chosenItem.getAssemble());
    			
        		ItemTable.setItems(itemListView);
        		fullListBtn.setVisible(true);
        		itemListLbl.setText("New Item\nList:");
    		}	
    		else return;
    	}
    	else return;
    }
    

    /**
     * Show full specs.
     * When the event happened show the all order information again
     * @param event the event
     */
    @FXML
    void showFullSpecs(ActionEvent event) {
    	fullListBtn.setVisible(false);
		itemListLbl.setText("Items\nList:");
		
		order = (Order) LoginController.windowControl.peekPipe("chosenOrder");
		itemListView = FXCollections.observableArrayList((ArrayList<ItemInList>)order.getItems());
		
		catalogItem.setCellValueFactory(new PropertyValueFactory<>("catalog_Type"));
		nameItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		priceItem.setCellValueFactory(new PropertyValueFactory<>("price"));
		quantityItem.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		typeItem.setCellValueFactory(new PropertyValueFactory<>("item_type"));
		
		ItemTable.setItems(itemListView);
    }
    
    /**
     * Back pressed.
     * When the back pressed go to the previous page (Customer View Orders)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerViewOrders.fxml");
    }
    
    @FXML
    void pickupConfirm(ActionEvent event) {
    	ClientApp.ProtocolHandler.Invoke(RequestType.EndOrder, order, null, true);
    	int refund = (int) ClientApp.ProtocolHandler.GetResponse(RequestType.EndOrder);
    	NotificationInTable notification = new NotificationInTable();
    	notification.setTo(order.getUserID());
    	notification.setFrom("Pickup");
		notification.setContent("Order number " + order.getOrderID() + " has been picked up"
    				+ "\nSee full info in \"My Orders\" tab");
    	ClientApp.ProtocolHandler.Invoke(RequestType.SendNotification, notification, null, false);
    	pickupBtn.setVisible(false);
		cancelOrder.setVisible(false);
		statusOrder.setText("Completed");
    }
    
    /**
     * Cancel order pressed.
     * When pressed cancel order the customer cancel his order , the order deleted from the Data Base 
     * And the customer get refund according to the time that left 
     * @param event the event
     */
    @FXML
    void cancelOrderPressed(ActionEvent event) {
    	Alert confirmAlert = new Alert(AlertType.NONE);
		confirmAlert.setTitle("Cancel Order!");
		confirmAlert.setContentText("Are you sure you want cancel the order?");
		ButtonType yes = new ButtonType("Yes", ButtonData.YES);
		ButtonType no = new ButtonType("No",ButtonData.NO);
		confirmAlert.getDialogPane().getButtonTypes().add(yes);
		confirmAlert.getDialogPane().getButtonTypes().add(no);
		Optional<ButtonType> result = confirmAlert.showAndWait();
		result.ifPresent(response -> { 
			if(response == yes) {
				LocalDateTime cancelTime = LocalDateTime.now();
				LocalDateTime shippingTime = Utilities.GenericUtilties.
						Convert_LocalDate_To_SQLDate(order.getShippingDate());
				
				long diff = Math.abs(ChronoUnit.MINUTES.between(cancelTime,shippingTime));
				int refundZerli=0;
				int	orderPrice = order.getTotalPrice();
				if(diff/60 >= 3 && diff%60 >0) {
					refundZerli=orderPrice;
				}
				else if(diff/60 >= 1 && diff/60 <= 3 && diff%60 >0 ) {
					refundZerli=(int)(0.5*orderPrice);
				}
				else {
					refundZerli=0;
				}
				
				ClientApp.ProtocolHandler.Invoke(RequestType.CancelOrder, refundZerli ,Integer.parseInt(order.getOrderID()), false);
				statusOrder.setText("Pending cancel");
				confirmAlert.close();
				cancelOrder.setVisible(false);
			}
			confirmAlert.close();
		});
    }

	/**
	 * On enter.
	 * The first action to run - initialize the table columns and set the data 
	 */
	@Override
	public void onEnter() {
		fullListBtn.setVisible(false);
		itemListLbl.setText("Items\nList:");
		pickupBtn.setVisible(false);
		order = (Order) LoginController.windowControl.peekPipe("chosenOrder");
		order.setUserID(ClientApp.UserID);
		if(order.getShippingMethod() == ShippingMethods.pickup && order.getStatus() == OrderStatus.confirmed)
			pickupBtn.setVisible(true);
		
		itemListView = FXCollections.observableArrayList((ArrayList<ItemInList>)order.getItems());
		
		catalogItem.setCellValueFactory(new PropertyValueFactory<>("catalog_Type"));
		nameItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		priceItem.setCellValueFactory(new PropertyValueFactory<>("price"));
		quantityItem.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		typeItem.setCellValueFactory(new PropertyValueFactory<>("item_type"));
		
		ItemTable.setItems(itemListView);
	    
		orderNumber.setText(order.getOrderID());
		orderDate.setText(order.getOrder_date());
		brachName.setText(order.getBranchName());
		if(order.getShippingMethod()== ShippingMethods.shipping) {
			shipMethod.setText("Shipping to");
			fullAddress.setText(order.getAddress()+" "+order.getCity());
		}
		else {
			shipMethod.setText("Pickup at");
			fullAddress.setText(order.getBranchName()+ " branch");
		}
		shippingDate.setText("at " + order.getShipping_date());
		paymentMethod.setText(order.getPayment_method());
		greetingCardText.setText(order.getGreetingCard()==null? "No greeting card" : order.getGreetingCard());
		statusOrder.setText(order.getOrder_status());
		price.setText(String.valueOf(order.getTotalPrice())+ " " + Utilities.Constants.SHEKEL);
		
		if(order.getStatus() == OrderStatus.completed || 
				order.getStatus() == OrderStatus.canceled ||
				order.getStatus() == OrderStatus.pending_cancel)
			cancelOrder.setVisible(false);
		else 
			cancelOrder.setVisible(true);
		
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {}

}
