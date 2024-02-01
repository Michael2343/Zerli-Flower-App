/*
 * 
 */
package controllers;

import java.sql.Timestamp;
import Entities.Complaint;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *The Class CustomerServiceViewComplaintsController is the controller part of the service GUI.
 *The Class give the ability to the service employee watch on the all customers complaints
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class CustomerServiceViewComplaintsController implements UserControl{

    /** The Post button. */
    @FXML
    private Button PostButton;

    /** The complaints table. */
    @FXML
    private TableView<Complaint> complaintsTable;

    /** The post time column. */
    @FXML
    private TableColumn<Complaint, Timestamp> postTimeColumn;

    /** The user ID column. */
    @FXML
    private TableColumn<Complaint, String> userIDColumn;
    
    /** The branch column. */
    @FXML
    private TableColumn<Complaint, String> branchColumn;

    /**
     * Post complaint press.
     *
     * @param event the event
     */
    @FXML
    void PostComplaintPress(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServicePostComplaint.fxml");
    }

 

    /**
     * Gets the complain info.
     * When the event happened go to Customer Service View Order Complaint Info
     * @param event the event Customer Service View Order Complaint Info and watch the full complaint info
     * @return if the event is not a double click end the event
     */
    @FXML
    void getComplainInfo(MouseEvent event) {
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		Complaint chosenComplaint = complaintsTable.getSelectionModel().getSelectedItem();
    		LoginController.windowControl.putPipe("Complaint select", chosenComplaint);
    		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerServiceViewOrderComplaintInfo.fxml");
    	}
    	else return;
    	
    }
    
    /**
     * Refresh pressed.
     * When pressed refresh the table set the last update data
     * @param event the event
     */
    @FXML
    void refreshPressed(MouseEvent event) {
    	onEnter();
    }

	/**
	 * On enter.
	 * The first action to run - initialize the table columns
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		userIDColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
		postTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Comp_date"));
		branchColumn.setCellValueFactory(new PropertyValueFactory<>("branch"));
		ClientApp.ProtocolHandler.Invoke(RequestType.GetComplaints,  null, null, true);
		complaintsTable.setItems((ObservableList<Complaint>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetComplaints));
	}

	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

}
