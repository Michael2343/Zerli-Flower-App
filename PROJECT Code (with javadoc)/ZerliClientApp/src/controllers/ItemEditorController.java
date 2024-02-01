/*
 * 
 */
package controllers;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *The Class ItemEditorController is the controller part of the marketing GUI.
 *The Class give the ability to the marketing to change and add some items
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ItemEditorController implements UserControl {
	
	/** The colors. */
	ArrayList<String> colors = new ArrayList<String>();
	
	/** The item types. */
	ArrayList<String> item_types = new ArrayList<String>();
	
	/** The current edited. */
	private Item currentEdited;
	
	/** The error label. */
	@FXML
	private Label errorLabel;
	
    /** The Item image. */
    @FXML
    private ImageView ItemImage;

    /** The Item RB. */
    @FXML
    private RadioButton ItemRB;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The currency. */
    @FXML
    private Label currency;
    
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

    /** The update btn. */
    @FXML
    private Button updateBtn;

    /**
     * Cancel button press.
     *	When pressed cancel go to Marketing Catalog Editor
     * @param event the event
     */
    @FXML
    void cancelBtn_press(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/MarketingCatalogEditor.fxml");
    }
    
    /**
     * Input valid.
     *
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
    	if(onSaleCB.isSelected())
    	{
        	try {
    			Integer.parseInt(salePrice.getText());
    		} catch (Exception e) {
        		error.append("Sale Price is invalid!\n");
        		valid = false;
    		}
    	}
    	errorLabel.setText(error.toString());
		return valid;
	}
    
    /**
     * Update button click.
     *When pressed  update take the Marketing employee data and insert it into the Data Base
     * @param event the event
     */
    @FXML
    void updateBtn_Click(ActionEvent event) {
    	if(!inputValid()) return;
    	CatalogType ctype;
    	if(catalogTypes.getSelectedToggle().equals(ItemRB))
    	{
    		ctype = CatalogType.custom;
    	}
    	else
    	{
    		ctype = CatalogType.pre_define;
    	}
    	currentEdited.setName(name.getText());
    	currentEdited.setPrice(Integer.parseInt(price.getText()));
    	currentEdited.setItemType(Utilities.GenericUtilties.StringToItemType(itemType.getValue()));
    	currentEdited.setCatalogType(ctype);
    	currentEdited.setColor(Utilities.GenericUtilties.StringToColor(itemColor.getValue()));
    	currentEdited.setOnSale(onSaleCB.isSelected());
    	currentEdited.setSalePrice(Integer.parseInt(salePrice.getText()));
    	ClientApp.ProtocolHandler.Invoke(RequestType.UpdateItem, currentEdited, null, false);
		 cancelBtn_press(null);
    }
    @FXML
    void helpBtnPressed(ActionEvent event) {
    	Utilities.GenericUtilties.ShowHelp("Item Editor", "help_marketing_editor.png");
    }
	/**
	 * On enter.
	 * The first action to run - initialize the table columns and get Item data from the Data Base
	 */
	@Override
	public void onEnter() {
		currency.setText(Utilities.Constants.SHEKEL);
		 int id = (int) LoginController.windowControl.getPipe("currentEdited");
		 ClientApp.ProtocolHandler.Invoke(RequestType.GetItemByID, null, id, true);
		 currentEdited = (Item)ClientApp.ProtocolHandler.GetResponse(RequestType.GetItemByID);
		 salePrice.disableProperty().bind(onSaleCB.selectedProperty().not());
		 ItemImage.setImage(currentEdited.getJXImage());
		 for(Color c : Color.values())
			 colors.add(Utilities.GenericUtilties.ColorToString(c));
		 for(ItemType it : ItemType.values())
			 item_types.add(Utilities.GenericUtilties.ItemTypeToString(it));
		 itemType.setItems(FXCollections.observableArrayList(item_types));
		 itemColor.setItems(FXCollections.observableArrayList(colors));
		 itemColor.setValue(Utilities.GenericUtilties.ColorToString(currentEdited.getColor()));
		 itemType.setValue(Utilities.GenericUtilties.ItemTypeToString(currentEdited.getItemType()));
		 name.setText(String.valueOf(currentEdited.getName()));
		 price.setText(String.valueOf(currentEdited.getPrice()));
		 if(currentEdited.getCatalogType() == CatalogType.custom)
			 ItemRB.selectedProperty().set(true);
		 else
			 productRB.selectedProperty().set(true);
		 onSaleCB.setSelected(currentEdited.isOnSale());
		 salePrice.setText(String.valueOf(currentEdited.getSalePrice()));
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		
	}

}
