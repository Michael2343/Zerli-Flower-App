/*
 * 
 */
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *The Class ManagerAccountManagmentController is the controller part of the Manager GUI.
 *The Class give the ability to the Manager to select the manage operation.
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ManagerAccountManagmentController implements UserControl{

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The add new account. */
    @FXML
    private ImageView addNewAcoount;

    /** The edit accounts. */
    @FXML
    private ImageView editAccounts;

    /**
     * Adds the new account pressed.
     * When pressed add go to Manager Add New Customer
     * @param event the event
     */
    @FXML
    void addNewAcoountPressed(MouseEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerAddNewCustomer.fxml");
    }

    /**          
     * Edits the accounts pressed.
     * When pressed Edits go to Manager Edit Accounts
     * @param event the event
     */
    @FXML
    void editAccountsPressed(MouseEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerEditAccounts.fxml");
    }
    
    /**
     * Back pressed.
     * When the back pressed go to the previous page (Manager Home Page)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerHomePage.fxml");

    }

	/**
	 * On enter.
	 */
	@Override
	public void onEnter() {
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
	}

}
