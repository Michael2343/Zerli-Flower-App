/*
 * 
 */
package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Entities.CatalogType;
import Entities.Item;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *The Class AddNewSurveyController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to..
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class MarketingEditorController implements UserControl {
	
	/** The item list. */
	private ObservableList<Item> itemList = FXCollections.observableArrayList();
	
	/** The catalog pages. */
	private Map< Integer ,ArrayList<Item>> catalogPages;
    
    /** The catalog type. */
    private CatalogType catalogType;
    
    /** The Grid pane. */
    @FXML
    private GridPane GridPane;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The catalog name label. */
    @FXML
    private Label catalogNameLbl;

    /** The current page. */
    @FXML
    private Label currentPage;

    /** The items checked. */
    @FXML
    private CheckBox itemsChecked;

    /** The max pages. */
    @FXML
    private Label maxPages;

    /** The products checked. */
    @FXML
    private CheckBox productsChecked;

    /**
     * Back pressed.
     *
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {

    }
    
    /**
     * Adds the new item pressed.
     * When the Adds pressed go to Item Adder
     * @param event the event
     */
    @FXML
    void addNewItemPressed(MouseEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ItemAdder.fxml");
    }

    /**
     * Browse left.
     * When Browse left pressed go back to the previous page in the catalog 
     * @param event the event
     */
    @FXML
    void browseLeft(ActionEvent event) {
    	if(currentPage.getText().equals("1")) return;
    	else {
     		ShowPage(Integer.parseInt(currentPage.getText())-1);
   		 currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())-1));
    	}
    }
    
    /**
     * Browse right.
     * When Browse left pressed move to the next page in the catalog
     * @param event the event
     */
    @FXML
    void browseRight(ActionEvent event) {
    	if(currentPage.getText().equals(maxPages.getText())) return;
    	else {
    		ShowPage(Integer.parseInt(currentPage.getText())+1);
    	    currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())+1));

    	}
    }
    
    /**
     * Update catalog by the item types
     */
    @SuppressWarnings("unchecked")
	private void updateCatalog()
    {
    	itemList.clear();
    	if(productsChecked.isSelected())
    	{
			catalogType = (CatalogType.pre_define);
			ClientApp.ProtocolHandler.Invoke(RequestType.GetCatalog, null,catalogType, true);
			itemList.addAll((ObservableList<Item>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCatalog));
    	}
    	if(itemsChecked.isSelected())
    	{
    		catalogType = (CatalogType.custom);
    		ClientApp.ProtocolHandler.Invoke(RequestType.GetCatalog, null,catalogType, true);
    		itemList.addAll((ObservableList<Item>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCatalog));
    	}
		int maxpages = ((itemList.size())/(GridPane.getColumnCount()*GridPane.getRowCount()));
		if((itemList.size())%(GridPane.getColumnCount()*GridPane.getRowCount())!=0) maxpages++;
		
		maxPages.setText(String.valueOf(Math.max(maxpages, 1)));
		currentPage.setText("1");
		catalogPages = new HashMap<>();
		int currentpages = 1;
		int columnIndex=0,rowIndex=0;
		for (Item item : itemList) {
			if (columnIndex==GridPane.getColumnCount())
			{
				columnIndex=0;
				rowIndex++;
			}
			if (rowIndex==GridPane.getRowCount())
			{
				rowIndex=0;
				columnIndex=0;
				currentpages++;
			}
			if(!catalogPages.containsKey(currentpages)) {
				ArrayList<Item> newPage = new ArrayList<Item>();
				newPage.add(item);
				catalogPages.put(currentpages, newPage);
			}
			else {
				ArrayList<Item> samePage = catalogPages.get(currentpages);
				catalogPages.remove(currentpages);
				samePage.add(item);
				catalogPages.put(currentpages, samePage);
			}
			columnIndex++;
		}	
		ShowPage(1);
	}

	/**
	 * On enter.
	 * The first action to run -  set catalog at page 1 and set the catalog data by the user choose
	 */
	@Override
	public void onEnter() {
		
		productsChecked.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateCatalog();
			}
		});
		itemsChecked.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateCatalog();
			}
		});
		productsChecked.setSelected(false);
		itemsChecked.setSelected(false);
		productsChecked.setSelected(true);
		itemsChecked.setSelected(true);
		ShowPage(1);
		
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		GridPane.getChildren().clear();
		
	}
	
	/**
	 * Show page.
	 *
	 * @param num the number
	 */
	public void ShowPage(int num) {
		GridPane.getChildren().clear();
		
		ArrayList<Item> page = catalogPages.get(num);
		if(page == null) return;
		int columnIndex=0,rowIndex=0;
		for (Item item : page) {
		
			if (columnIndex==GridPane.getColumnCount())
			{
				columnIndex=0;
				rowIndex++;
			}
			if (rowIndex==GridPane.getRowCount())
			{
				break;
			}
			
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/components/EditableItem.fxml"));
			Parent root;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			EditableItemController itemHBoxController=loader.getController();
			itemHBoxController.init(item.getId(),item.getName() ,item.getJXImage());
			
			GridPane.add(root, columnIndex++, rowIndex);
		}
	
		
	}
}
