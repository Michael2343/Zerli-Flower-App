/*
 * 
 */
package controllers;

import java.util.Optional;

import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * The Class EditableItemController is the controller of the editable item GUI
 * the Class give the to the manger the ability to edit item 
 */
public class EditableItemController {

	/** The id. */
	private int id;
	
    /** The Edit. */
    @FXML
    private ImageView Edit;

    /** The Remove. */
    @FXML
    private ImageView Remove;

    /** The image rectangle. */
    @FXML
    private Rectangle imageRec;

    /** The name label. */
    @FXML
    private Label nameLabel;

    /**
     * Edits the pressed.
     *
     * @param event the event
     */
    @FXML
    void EditPressed(MouseEvent event) {
    	LoginController.windowControl.putPipe("currentEdited", id);
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ItemEditor.fxml");
    }

    /**
     * Removes the pressed.
     * When pressed remove the item will remove from the catalogs item Data Base
     * @param event the event
     */
    @FXML
    void RemovePressed(MouseEvent event) {
		Alert confirmAlert = new Alert(AlertType.NONE);
		confirmAlert.setTitle("Removing item");
		confirmAlert.setContentText("Are you sure you want to remove "
		+ nameLabel.getText() + " ?\n(This action is ireversible)");
		ButtonType yes = new ButtonType("Yes", ButtonData.YES);
		ButtonType no = new ButtonType("No",ButtonData.NO);
		confirmAlert.getDialogPane().getButtonTypes().add(yes);
		confirmAlert.getDialogPane().getButtonTypes().add(no);
		Optional<ButtonType> result = confirmAlert.showAndWait();
		result.ifPresent(response -> { 
			if(response == yes) 
			{
				ClientApp.ProtocolHandler.Invoke(RequestType.RemoveItemByID, null, id, false);
				LoginController.windowControl.Refresh();
			}
		});
    }
    
    /**
     * Initialize the component editable item to the item details
     *
     * @param id the id
     * @param name the name
     * @param image the image
     */
    public void init(int id,String name,Image image) 
    {
    	this.id=id;
		nameLabel.setText(name);
		imageRec.setFill(new ImagePattern(image));
    }
}
