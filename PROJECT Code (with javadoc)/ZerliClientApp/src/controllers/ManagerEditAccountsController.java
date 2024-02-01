/*
 * 
 */
package controllers;

import java.util.ArrayList;
import Entities.Access;
import Entities.AccountInfo;
import Entities.Roles;
import ProtocolHandler.RequestType;
import Utilities.GenericUtilties;
import client.ClientApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * The Class ManagerEditAccountsController is the controller part of the Manager GUI.
 *The Class give the ability to the Manager to edit the users status and roles
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ManagerEditAccountsController implements UserControl{

	/** The customer list. */
	ObservableList<AccountInfo> customerList;
	
	/** The employee list. */
	ObservableList<AccountInfo> employeeList;
	
	/** The account info. */
	AccountInfo accountInfo = null;
	
    /** The Account management. */
    @FXML
    private TableView<AccountInfo> AccountManagment;

    /** The ID column. */
    @FXML
    private TableColumn<AccountInfo, String> IDCol;

    /** The account info column. */
    @FXML
    private TableColumn<AccountInfo, String> accountInfoCol;

    /** The account info choice. */
    @FXML
    private ComboBox<String> accountInfoChoice;

    /** The account info label. */
    @FXML
    private Label accountInfoLbl;

    /** The account type. */
    @FXML
    private ToggleGroup accountType;

    /** The accounts section. */
    @FXML
    private AnchorPane accountsSection;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The change section. */
    @FXML
    private AnchorPane changeSection;

    /** The customer button. */
    @FXML
    private RadioButton customerBtn;

    /** The employee button. */
    @FXML
    private RadioButton employeeBtn;

    /** The first name column. */
    @FXML
    private TableColumn<AccountInfo, String> firstNameCol;

    /** The full name label. */
    @FXML
    private Label fullNameLbl;

    /** The last name column. */
    @FXML
    private TableColumn<AccountInfo, String> lastNameCol;

    /** The user ID column. */
    @FXML
    private TableColumn<AccountInfo, String> userIDCol;
    
    /** The change error label. */
    @FXML
    private Label changeErrorLbl;

    /**
     * Change account info pressed.
     * When pressed change account ,open the operation that can change selected customer status 
     * @param event the event
     */
    @FXML
    void ChangeAccountInfoPressed(ActionEvent event) {
    	
    	if(accountInfoChoice.getValue() == null) {
    		changeErrorLbl.setText("Choose option from list");
    	}
    	else if (accountInfoChoice.getValue().equals(accountInfoLbl.getText())) {
    		changeErrorLbl.setText("Choose diffrent option\nfrom exist one");
    	}
    	else {
    		@SuppressWarnings("unused")
			String str;
    		if(accountType.getSelectedToggle() == customerBtn){
    			Access newAccess =  GenericUtilties.StringToAccessStatus(accountInfoChoice.getValue());
        		ClientApp.ProtocolHandler.Invoke(RequestType.UpdateAccountAccess, newAccess, accountInfo.getUserID(), false);
        		str = "Customer " + fullNameLbl.getText() + " changed access to " + accountInfoChoice.getValue();
        		accountType.selectToggle(employeeBtn);
        		accountType.selectToggle(customerBtn);
    		}
    		else {
        		Roles newRole = GenericUtilties.StringToRoles(accountInfoChoice.getValue());
        		ClientApp.ProtocolHandler.Invoke(RequestType.UpdateEmployeeRole, newRole, accountInfo.getUserID(), false);
        		str = "Employee " + fullNameLbl.getText() + " changed role to " + accountInfoChoice.getValue();
        		accountType.selectToggle(customerBtn);
        		accountType.selectToggle(employeeBtn);
    		}
    	}
    }

    /**
     * Gets the account info.
     * @param event the event
     * @return the account info
     */
    @FXML
    void getAccountInfo(MouseEvent event) {
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		accountInfoChoice.setValue(null);
    		
    		if(customerBtn.isSelected()) {
    			accountInfo = customerList.get(AccountManagment.getSelectionModel().getSelectedIndex());
    			accountInfoLbl.setText(GenericUtilties.AccessStatusToString(accountInfo.getAccess()));
    			accountType.selectToggle(customerBtn);
    		}
    		else if(employeeBtn.isSelected()){
    			accountInfo = employeeList.get(AccountManagment.getSelectionModel().getSelectedIndex());
    			accountInfoLbl.setText(GenericUtilties.RolesToString(accountInfo.getRole()));
    			accountType.selectToggle(employeeBtn);
    		}
    		fullNameLbl.setText(accountInfo.getFirstName() + " " + accountInfo.getLastName());
    		changeSection.setVisible(true);
    	}
    	else return;
    }
    

    /**
     * Back pressed.
     * When the back pressed go to the previous page (Manager Account Managment)
     * @param event the event
     */
    @FXML
    void backPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/ManagerAccountManagment.fxml");
    }

    /**
     * Refresh pressed.
     * When pressed refresh the table set the last update data
     * @param event the event
     */
    @FXML
    void refreshPressed(MouseEvent event) {
    	accountType.selectToggle(null);
    	onEnter();
    }
    
    
	/**
	 * On enter.
	 * The first action to run - initialize the table columns and set the user data by their role (customer and others)
	 */
	@Override
	public void onEnter() {
		changeErrorLbl.setText("");
		accountsSection.setVisible(false);
		changeSection.setVisible(false);
		customerBtn.setSelected(false);
		employeeBtn.setSelected(false);
		
		ArrayList<String> accountStatusList = new ArrayList<>();
		accountStatusList.add("Inactive");
		accountStatusList.add("Active");
		accountStatusList.add("Frozen");
		
		ArrayList<String> employeeRoleList = new ArrayList<>();
		for (Roles role : Roles.values()) {
			if(role == Roles.delivery || role == Roles.service)
			employeeRoleList.add(GenericUtilties.RolesToString(role));
        }
		
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));	
		firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));	
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));	
		IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		
		accountType.selectToggle(null);
		accountType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@SuppressWarnings("unchecked")
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue == null) {}
				else if(!newValue.equals(oldValue))
				{	 
					if(newValue.equals(customerBtn))
					{
						ClientApp.ProtocolHandler.Invoke(RequestType.GetAllCustomersInfo, null, null, true);
						customerList = (ObservableList<AccountInfo>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetAllCustomersInfo);
						accountInfoChoice.setItems( FXCollections.observableArrayList(accountStatusList));
						accountInfoCol.setText("Status");
						accountInfoCol.setCellValueFactory(new PropertyValueFactory<>("access"));
						AccountManagment.setItems(customerList);
					}
					else
					{
						ClientApp.ProtocolHandler.Invoke(RequestType.GetAllEmployeesInfo, null, null, true);
						employeeList = (ObservableList<AccountInfo>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetAllEmployeesInfo);
						accountInfoChoice.setItems( FXCollections.observableArrayList(employeeRoleList));
						accountInfoCol.setText("Role");
						accountInfoCol.setCellValueFactory(new PropertyValueFactory<>("role"));
					    AccountManagment.setItems(employeeList);
					}
					accountsSection.setVisible(true);
					changeSection.setVisible(false);
				}
			}
		});
	}

	/**
	 * On exit.
	 * The last action to run - reset the selection
	 */
	@Override
	public void onExit() {
		accountType.selectToggle(null);
	}

}

