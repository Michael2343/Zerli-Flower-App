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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * The Class CartNewItemListHboxController s the controller part of the item that aper in the cart GUI.
 * The Class extends the Hbox class to be able to be implement into the Hbox GUI component
 */
public class CartNewItemListHboxController extends HBox{
	
	/** The id. */
	private int id;
	
	
	/** The quantity. */
	private int quntity;
	
	/** The Vbox. */
	private VBox Vbox;
    
    /** The catalog type label. */
    @FXML
    private Label catalogTypeLabel;

    /** The delete button. */
    @FXML
    private Button deleteBtn;

    /** The hbox root. */
    @FXML
    private HBox hboxRoot;

    /** The circle. */
    @FXML
    private Circle circle;

    /** The item name label. */
    @FXML
    private Label itemNameLabel;

    /** The item type label. */
    @FXML
    private Label itemTypeLabel;

    /** The quantity label. */
    @FXML
    private Label quntityLabel;

    /**
     * Inits the.
     *
     * @param Vbox the vbox
     * @param image1 the image 1
     * @param id the id
     * @param name the name
     * @param itemType the item type
     * @param catalogType the catalog type
     * @param quntity the quantity
     * @param price the price
     */
    public void init ( VBox Vbox ,Image image1 ,int id,String name,
    		ItemType itemType,CatalogType catalogType,int quntity,int price)
    {
    	this.Vbox=Vbox;
    	this.id=id;
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
    	NewItem newItem =(NewItem) LoginController.windowControl.peekPipe("newItemList");
    	ItemInList tmpInList = new ItemInList(id, quntity);
		newItem.deleteItem(tmpInList);
		LoginController.windowControl.putPipe("newItemList",newItem);
    	Vbox.getChildren().remove(hboxRoot);
    	ClientApp.ProtocolHandler.Invoke(RequestType.DeleteItemFromNewItemList, newItem.getItem_id(), id, true);
    
    	if((boolean)ClientApp.ProtocolHandler.GetResponse(RequestType.DeleteItemFromNewItemList))
    	{
    	

         	if(newItem.getAssemble().size()==0) {
         		RedNotificationCircle cartLabelAndImage=(RedNotificationCircle) LoginController.windowControl.getPipe("cartLabel");
            	int cartNotificationsNumber=cartLabelAndImage.getCartNotificationsNumber();
            	cartNotificationsNumber=cartNotificationsNumber-newItem.getQuantity();
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
                ClientApp.ProtocolHandler.Invoke(RequestType.DeleteNewItemFromCart, newItem.getItem_id(), null, false);
                LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCart.fxml");
         	}
    	}
    		
    	
    }

}
