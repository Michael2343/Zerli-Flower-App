/*
 * 
 */
package controllers;

import Entities.*;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

/**
 * The Class CartItemHboxController s the controller part of the item that aper in the cart GUI.
 * The Class extends the Hbox class to be able to be implement into the Hbox GUI component
 */
public class CartItemHboxController extends HBox{
	
	/** The total cost. */
	private Label totalCost;
	
	/** The Vbox. */
	private VBox Vbox;
    
    /** The catalog type label. */
    @FXML
    private Label catalogTypeLabel;
    
    /** The circle. */
    @FXML
    private Circle circle;
    
    /** The delete button. */
    @FXML
    private Button deleteBtn;

    /** The item name label. */
    @FXML
    private Label itemNameLabel;

    /** The item type label. */
    @FXML
    private Label itemTypeLabel;

    /** The quantity label. */
    @FXML
    private Label quntityLabel;
    
    /** The Hbox root. */
    @FXML
    private HBox hboxRoot;
    
    /** The id. */
    private int id;
	
	/** The price. */
	private int price;
	
	/** The quantity. */
	private int quntity;
    
    /**
     * Instantiates a new cart item hbox controller.
     */
    public CartItemHboxController() {
	super();	
    }
    
    /**
     * Inits the.
     *
     * @param totalCost the total cost
     * @param Vbox the vbox
     * @param image1 the image 1
     * @param id the id
     * @param name the name
     * @param itemType the item type
     * @param catalogType the catalog type
     * @param quantity the quantity
     * @param price the price
     */
    public void init (Label totalCost, VBox Vbox ,Image image1 ,int id,String name,
    		ItemType itemType,CatalogType catalogType,int quntity,int price)
    {
    	this.totalCost=totalCost;
    	this.Vbox=Vbox;
    	this.id=id;
    	this.price=price;
    	this.quntity=quntity;
    	circle.setFill(new ImagePattern(image1));
    	itemNameLabel.setText(name);
    	itemTypeLabel.setText(Utilities.GenericUtilties.ItemTypeToString(itemType));
    	catalogTypeLabel.setText(Utilities.GenericUtilties.CatalogTypeToString(catalogType));
    	quntityLabel.setText(String.valueOf(quntity));
    }
    
    /**
     * Delete button pressed.
     * When the delete pressed the item is deleted from the item cart list and update the amount label
     * @param event the event
     */
    @FXML
    void DeleteBtnPressed(ActionEvent event) {
    	Vbox.getChildren().remove(hboxRoot);
    	ClientApp.ProtocolHandler.Invoke(RequestType.DeleteItemFromCart, id, null, true);
    	totalCost.setText(String.valueOf((int)LoginController.windowControl.getPipe("totalCost")-(price*quntity)));
    	if((boolean)ClientApp.ProtocolHandler.GetResponse(RequestType.DeleteItemFromCart))
    	{
    		RedNotificationCircle cartLabelAndImage=(RedNotificationCircle) LoginController.windowControl.getPipe("cartLabel");
        	int cartNotificationsNumber=cartLabelAndImage.getCartNotificationsNumber();
        	cartNotificationsNumber=cartNotificationsNumber-quntity;
        	cartLabelAndImage.setCartNotificationsNumber(cartNotificationsNumber);
        	Label label;
        	ImageView image=new ImageView();
        	label=cartLabelAndImage.getLabel();
        	image=cartLabelAndImage.getImage();
        	if(cartNotificationsNumber==0) 
        	{
        		label.setVisible(false);
            	image.setVisible(false);
        	}
        	label.setText(String.valueOf(cartNotificationsNumber));
        	LoginController.windowControl.putPipe("cartLabel", cartLabelAndImage);
    	}
    	LoginController.windowControl.putPipe("totalCost", Integer.parseInt(totalCost.getText()));
    	
    }

}
