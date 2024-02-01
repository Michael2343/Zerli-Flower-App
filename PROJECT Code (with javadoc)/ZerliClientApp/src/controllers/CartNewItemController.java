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
 * The Class CartNewItemController s the controller part of the item that aper in the cart GUI.
 * The Class extends the Hbox class to be able to be implement into the Hbox GUI component
 */
public class CartNewItemController extends HBox {
	
	/** The id. */
	private int id;
	
	/** The price. */
	private int price;
	
	/** The quantity. */
	private int quntity;
	
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

	/** The quantity label. */
	@FXML
	private Label quntityLabel;

	/** The total cost. */
	private Label totalCost;

	/** The Vbox. */
	private VBox Vbox;
	
	/** The new item. */
	private NewItem newItem;

	/**
	 * View new item components.
	 * When pressed go to window that show newIte specification
	 * @param event the event
	 */
	@FXML
	void ViewNewItemComponents(ActionEvent event) {
		LoginController.windowControl.putPipe("newItemList", newItem);
		LoginController.windowControl.setUserControl("/gui/usercontrols/NewItemList.fxml");

	}

	/**
	 * Inits the.
	 *
	 * @param totalCost the total cost
	 * @param Vbox the vbox
	 * @param image1 the image 1
	 * @param id the id
	 * @param name the name
	 * @param quntity the quantity
	 * @param price the price
	 * @param newItem the new item
	 */
	public void init(Label totalCost, VBox Vbox, Image image1, int id, String name, int quntity, int price,
			NewItem newItem) {
		this.totalCost = totalCost;
		this.Vbox = Vbox;
		this.id = id;
		this.price = price;
		this.quntity = quntity;
		circle.setFill(new ImagePattern(image1));
		itemNameLabel.setText(newItem.getItemName());
		quntityLabel.setText(String.valueOf(quntity));
		this.newItem = newItem;
	}

	/**
	 * Delete button pressed.
	 * When the delete pressed the Newitem is deleted from the newItem cart list and update the amount label
	 * @param event the event
	 */
	@FXML
	void DeleteBtnPressed(ActionEvent event) {
		Vbox.getChildren().remove(hboxRoot);
		ClientApp.ProtocolHandler.Invoke(RequestType.DeleteAllnewItemFromCart, id, null, true);
		totalCost.setText(
				String.valueOf((Integer) LoginController.windowControl.peekPipe("totalCost") - price * quntity));
		if ((boolean) ClientApp.ProtocolHandler.GetResponse(RequestType.DeleteAllnewItemFromCart)) {
			RedNotificationCircle cartLabelAndImage = (RedNotificationCircle) LoginController.windowControl
					.getPipe("cartLabel");
			int cartNotificationsNumber = cartLabelAndImage.getCartNotificationsNumber();
			cartNotificationsNumber = cartNotificationsNumber - quntity;
			cartLabelAndImage.setCartNotificationsNumber(cartNotificationsNumber);
			Label label;
			ImageView image = new ImageView();
			label = cartLabelAndImage.getLabel();
			image = cartLabelAndImage.getImage();
			if (cartNotificationsNumber == 0) {
				label.setVisible(false);
				image.setVisible(false);
			}
			label.setText(String.valueOf(cartNotificationsNumber));
			LoginController.windowControl.putPipe("cartLabel", cartLabelAndImage);
		}
		LoginController.windowControl.putPipe("totalCost", Integer.parseInt(totalCost.getText()));

	}
}
