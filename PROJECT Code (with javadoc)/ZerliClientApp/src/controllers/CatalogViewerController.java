/*
 * 
 */
 package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import Entities.CatalogType;
import Entities.Item;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * The Class CatalogViewerController is the controller part of the Customer GUI.
 * The Class CatalogViewerController give the Customer the ability to browse throw the catalog.
 * The class implement user control interface to be able to insert into frame users GUI
 */
public class CatalogViewerController implements UserControl {

	
    @FXML
    private TextField rangeNumber;
    
    /** The item list. */
	private ObservableList<Item> itemRangeList;
	
	/** The item list. */
	private ObservableList<Item> itemList;
	
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
    
    /** The no item error label. */
    @FXML
    private Label noItemErrorLabel;
    
    /** The current page. */
    @FXML
    private Label currentPage;

    /** The max pages. */
    @FXML
    private Label maxPages;
    
    
    /** The next button. */
    @FXML
    private Button nextBtn;

    @FXML
    private Label ValidNumberError;
    
    @FXML
    void searchRangePressed(ActionEvent event) {
    	int number; 
    	try {
        	number = Integer.parseInt(rangeNumber.getText());

		} catch (Exception e) {
			ValidNumberError.setVisible(true);
			return;
		}
    	ValidNumberError.setVisible(false);
    	itemRangeList = FXCollections.observableArrayList();
    	for(Item item: itemList) {
    		if(item.getPrice()<= number)
    			itemRangeList.add(item);
    	}
    	GridPane.getChildren().clear();
    	CatalogBuilder(itemRangeList);
    }
    
    @FXML
    void showAllPressed(ActionEvent event) {
    	GridPane.getChildren().clear();
    	CatalogBuilder(itemList);
    }
    
    /**
     * Next pressed.
     * When the next pressed go to the creation of newItem ,give him name and quantity 
     * @param event the event
     */
    @FXML
    void nextPressed(ActionEvent event) {
    	if(LoginController.windowControl.peekPipe("newItem") == null) 
    	{
    		noItemErrorLabel.setVisible(true);
    		Thread thread =new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					noItemErrorLabel.setVisible(false);
				}
			});
			thread.start();
    	return;
    	}
    	LoginController.windowControl.setUserControl("/gui/usercontrols/NewItemNameAndQuantity.fxml");
    	
    }													
   
    
    /**
     * Back pressed.
     *When the back pressed go to the previous page (Customer Home Page) and if its needed save the typed data 
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	if(LoginController.windowControl.peekPipe("newItem") != null)
    	{
	    	Alert confirmAlert = new Alert(AlertType.NONE);
			confirmAlert.setTitle("Leaving New item information");
			confirmAlert.setContentText("Do you want to save typed data?");
			ButtonType yes = new ButtonType("Yes", ButtonData.YES);
			ButtonType no = new ButtonType("No",ButtonData.NO);
			confirmAlert.getDialogPane().getButtonTypes().add(yes);
			confirmAlert.getDialogPane().getButtonTypes().add(no);
			Optional<ButtonType> result = confirmAlert.showAndWait();
			result.ifPresent(response -> { 
				if(response == no) Reset();
			});
    	}
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerHomePage.fxml");
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
     *When Browse left pressed move to the next page in the catalog
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
    
    public void CatalogBuilder(ObservableList<Item> itemList) {
    	int maxpages = ((itemList.size())/(GridPane.getColumnCount()*GridPane.getRowCount()));
		if((itemList.size())%(GridPane.getColumnCount()*GridPane.getRowCount())!=0) maxpages++;
		if(maxpages == 0) {
			maxPages.setText("0");
			currentPage.setText("0");
			return;
		}
		else {
			maxPages.setText(String.valueOf(maxpages));
			currentPage.setText("1");
		}
	
		
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
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {	
    	ValidNumberError.setVisible(false);
		noItemErrorLabel.setVisible(false);
		catalogType = (CatalogType) LoginController.windowControl.peekPipe("catalog");
		if(catalogType == CatalogType.new_item) {
			ClientApp.ProtocolHandler.Invoke(RequestType.GetCatalog, null,CatalogType.pre_define, true);
			itemList=(ObservableList<Item>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCatalog);
			ClientApp.ProtocolHandler.Invoke(RequestType.GetCatalog, null,CatalogType.custom, true);
			itemList.addAll((ObservableList<Item>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCatalog));		
		}
		else {
			ClientApp.ProtocolHandler.Invoke(RequestType.GetCatalog, null,catalogType, true);
			itemList=(ObservableList<Item>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetCatalog);
		}
	
		if(catalogType== CatalogType.custom) {
			catalogNameLbl.setText("Zerli items");
			nextBtn.setVisible(false);
		}
		else if(catalogType== CatalogType.pre_define){
			catalogNameLbl.setText("Zerli products");
			nextBtn.setVisible(false);

		}
		else {
			catalogNameLbl.setText("Full Zerli catalog");
			nextBtn.setVisible(true);
		}
		GridPane.getChildren().clear();
		CatalogBuilder(itemList);
		
	}
	
	/**
	 * On exit.
	 * The first action to run - clear the window
	 */
	@Override
	public void onExit() {
		GridPane.getChildren().clear();
		rangeNumber.setText("");
	}
	
	/**
	 * Reset the item selection.
	 */
	private void Reset() {
		LoginController.windowControl.putPipe("newItem",null);
		
	}


	/**
	 * Show page.
	 * Show the current catalog page
	 * @param num the num
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
			loader.setLocation(getClass().getResource("/gui/components/ItemHBox.fxml"));
			Parent root;
			try {
				root = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			ItemHBoxController itemHBoxController=loader.getController();
			itemHBoxController.init(item.getId(),item.getName(),String.valueOf(item.getCurrentPrice()) ,item.getJXImage(),item.isOnSale());
			
			GridPane.add(root, columnIndex++, rowIndex);
		}
	
		
	}
	
}

