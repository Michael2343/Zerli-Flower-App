/*
 * 
 */
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import Entities.CatalogType;

/**
 *The Class CustomerHomePageController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to choose the catalog type that he want to get access
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerHomePageController implements UserControl{

    /** The costume button. */
    @FXML
    private Button castumBtn;

    /** The pre-define button. */
    @FXML
    private Button prePefineBtn;
    
    /** The private button. */
    @FXML
    private Button privateBtn;

    /**
     * Custom pressed.
     * When the custom pressed go to the custom catalog and import the custom items 
     * @param event the event
     */
    @FXML
    void customPressed(ActionEvent event) {
    	LoginController.windowControl.putPipe("catalog", CatalogType.custom);
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCatalogViewer.fxml");
    }

    /**
     * Pre-define pressed.
     * When the Pre-define pressed go to the Pre-define catalog and import the Pre-define items 
     * @param event the event
     */
    @FXML
    void preDefinePressed(ActionEvent event) {
    	LoginController.windowControl.putPipe("catalog", CatalogType.pre_define);
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCatalogViewer.fxml");
    }
    
    

    /**
     * Private pressed.
     * When the newItem pressed go to the newItem catalog and import all the items 
     * @param event the event
     */
    @FXML
    void privatePressed(ActionEvent event) {
    	LoginController.windowControl.putPipe("catalog", CatalogType.new_item);
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCatalogViewer.fxml");
    }
    
    @FXML
    void helpBtnPressed(ActionEvent event) {
    	Utilities.GenericUtilties.ShowHelp("Customer Catalog", "help_catalog.png");
    }
	/**
	 * On enter.
	 */
	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
	}
}