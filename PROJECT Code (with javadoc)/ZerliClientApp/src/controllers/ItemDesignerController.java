/*
 * 
 */
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import Entities.CatalogType;
import Entities.Color;
import Entities.Item;
import Entities.ItemType;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 *The Class ItemDesignerController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to..
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ItemDesignerController implements UserControl {
	
	/** The colors. */
	ArrayList<String> colors = new ArrayList<String>();
	
	/** The item types. */
	ArrayList<String> item_types = new ArrayList<String>();
	
	/** The image data. */
	FileInputStream imagedata = null;
	
	/** The new item. */
	private Item newItem;
	
	/** The error label. */
	@FXML
	private Label errorLabel;
	
    /** The Item image. */
    @FXML
    private ImageView ItemImage;
  
    /** The currency. */
    @FXML
    private Label currency;
   
    /** The Item RB. */
    @FXML
    private RadioButton ItemRB;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The add button. */
    @FXML
    private Button addBtn;

    /** The cancel button. */
    @FXML
    private Button cancelBtn;

    /** The catalog types. */
    @FXML
    private ToggleGroup catalogTypes;

    /** The item color. */
    @FXML
    private ComboBox<String> itemColor;

    /** The item type. */
    @FXML
    private ComboBox<String> itemType;

    /** The name. */
    @FXML
    private TextField name;

    /** The on sale CB. */
    @FXML
    private CheckBox onSaleCB;

    /** The price. */
    @FXML
    private TextField price;

    /** The product RB. */
    @FXML
    private RadioButton productRB;

    /** The sale price. */
    @FXML
    private TextField salePrice;

    /** The select image. */
    @FXML
    private Button selectImage;

    /**
     * Adds the button click.
     * When pressed add the new item will insert into the Data Base with all his details
     * @param event the event
     */
    @FXML
    void addBtn_Click(ActionEvent event) {
    	byte[] buf = null;
    	if(!inputValid()) return;
    	if(ItemImage.getImage() != null)
    	{
    		try {
				buf = imagedata.readAllBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else
    		buf = null;
    	CatalogType ctype;
    	if(catalogTypes.getSelectedToggle().equals(ItemRB))
    	{
    		ctype = CatalogType.custom;
    	}
    	else
    	{
    		ctype = CatalogType.pre_define;
    	}
    	newItem = new Item(0, name.getText(), Integer.parseInt(price.getText()), ctype, Utilities.GenericUtilties.StringToItemType(itemType.getValue()),Utilities.GenericUtilties.StringToColor(itemColor.getValue()), buf,onSaleCB.isSelected(),  Integer.parseInt(salePrice.getText()));
    	ClientApp.ProtocolHandler.Invoke(RequestType.AddItem, newItem, null, false);
    	cancelBtn_press(null);
    }

    /**
     * Input validation.
     * @return true, if successful
     */
    private boolean inputValid() {
    	StringBuilder error = new StringBuilder("");
    	boolean valid = true;
    	if(name.getText().equals(""))
    	{
    		error.append("Name cannot be empty!\n");
    		valid = false;
    	}
    	try {
			Integer.parseInt(price.getText());
		} catch (Exception e) {
    		error.append("Price is invalid!\n");
    		valid = false;
		}
    	if(catalogTypes.getSelectedToggle() == null)
    	{
    		error.append("Please select Catalog Type!\n");
    		valid = false;
    	}
    	if(itemType.getValue() == null)
    	{
    		error.append("Please select Item Type!\n");
    		valid = false;
    	}
    	if(itemColor.getValue() == null)
    	{
    		error.append("Please select Item Type!\n");
    		valid = false;
    	}
    	try {
			Integer.parseInt(salePrice.getText());
		} catch (Exception e) {
    		error.append("Sale Price is invalid!\n");
    		valid = false;
		}
    	errorLabel.setText(error.toString());
		return valid;
	}

	/**
	 * Cancel button press.
	 * When pressed cancel go to Marketing Catalog Editor
	 * @param event the event
	 */
	@FXML
    void cancelBtn_press(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/MarketingCatalogEditor.fxml");
    }
    
    /**
     * Select image click.
     * When pressed select the system import a image file into the item iamge 
     * @param event the event
     */
    @FXML
    void selectimage_cllick(ActionEvent event) {
    	FileChooser imageChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
    	imageChooser.getExtensionFilters().add(extFilterPNG);
        //Show open file dialog
        File file = imageChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ItemImage.setImage(image);
            try {
				imagedata = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
    }
	
	/**
	 * On enter.
	 * The first action to run - set the 
	 */
	@Override
	public void onEnter() {
		currency.setText(Utilities.Constants.SHEKEL);
		 //salePrice.disableProperty().bind(onSaleCB.selectedProperty().not());
		 for(Color c : Color.values())
			 colors.add(Utilities.GenericUtilties.ColorToString(c));
		 for(ItemType it : ItemType.values())
			 item_types.add(Utilities.GenericUtilties.ItemTypeToString(it));
		 itemType.setItems(FXCollections.observableArrayList(item_types));
		 itemColor.setItems(FXCollections.observableArrayList(colors));
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {}

}
