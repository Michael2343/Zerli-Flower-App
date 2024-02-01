/*
 * 
 */
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

import Entities.*;

/**
 *The Class NewItemListController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to see his new item items in the cart
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class NewItemListController implements UserControl {

    /** The Back button. */
    @FXML
    private Button BackBtn;

    /** The Vbox. */
    @FXML
    private VBox Vbox;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;
    
    /**
     * Back button pressed.
     * When the back pressed go to the previous page (Customer Cart)
     * @param event the event
     */
    @FXML
    void BackBtnPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCart.fxml");
    }
	
	/**
	 * On enter.
	 * The first action to run - set the new item items into the GUI list 
	 */
	@Override
	public void onEnter() {
		Vbox.getChildren().clear();
		NewItem newItem;
		newItem=(NewItem) LoginController.windowControl.peekPipe("newItemList");
		
		for (ItemInList itemInlist : newItem.getAssemble()) {
			FXMLLoader loader=new FXMLLoader();
			HBox root;
			loader.setLocation(getClass().getResource("/gui/components/CartNewItemListHbox.fxml"));
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			CartNewItemListHboxController cartNewItemListHboxController=loader.getController();
			cartNewItemListHboxController.init(Vbox,itemInlist.getJXImage(),itemInlist.getItem_id(),
					itemInlist.getItemName(),itemInlist.getItemType(),itemInlist.getCatalogType(),itemInlist.getQuantity(),itemInlist.getPrice());
			Vbox.getChildren().add(root);
		}
		
		}
	
	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
	}

}
