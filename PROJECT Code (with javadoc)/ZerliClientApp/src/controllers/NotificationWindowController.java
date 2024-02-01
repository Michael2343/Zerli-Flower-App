/*
 * 
 */
package controllers;

import Entities.NotificationInTable;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *The Class NotificationWindowController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to see his notifications (from who, content ,status)
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class NotificationWindowController implements UserControl {
	
	/** The All notification table list. */
	private ObservableList<NotificationInTable> AllnotificationTableList;
	
	/** The unread notification table list. */
	private ObservableList<NotificationInTable> unreadNotificationTableList;
    
    /** The all notifications show. */
    private boolean allNotificationsShow;
    
    /** The thread. */
    Thread thread;
    
    /** The Error label. */
    @FXML
    private Label ErrorLabel;

    /** The Notifications status button. */
    @FXML
    private ToggleButton NotificationsStatusBtn;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The content. */
    @FXML
    private TableColumn<NotificationInTable, String> content;

    /** The notification number. */
    @FXML
    private TableColumn<NotificationInTable, Integer> notificationNumber;

    /** The notifications center table. */
    @FXML
    private TableView<NotificationInTable> notificationsCenterTable;

    /** The send from. */
    @FXML
    private TableColumn<NotificationInTable, String> sendFrom;

    /** The status. */
    @FXML
    private TableColumn<NotificationInTable, String> status;
    
    /** The change notifications label. */
    @FXML
    private Label changeNotificationsLbl;
    
    
    /**
     * Creates the unread notification list.
     * @return the observable list
     */
    private ObservableList<NotificationInTable> createUnreadNotificationList()
    {
    	ObservableList<NotificationInTable> unreadAllnotificationTableList= FXCollections.observableArrayList();
		for (NotificationInTable notificationInTable : AllnotificationTableList) {
			if(notificationInTable.getStatus().equals("unread"))
					unreadAllnotificationTableList.add(notificationInTable);
		}
    	return unreadAllnotificationTableList;
    }


    /**
     * Sets the status.
     * When pressed set change notification status from unread to read status 
     * @param event the new status
     */
    @FXML
    void setStatus(MouseEvent event) {
    	
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		NotificationInTable temp = notificationsCenterTable.getSelectionModel().getSelectedItem();
    		if(temp.getStatus().equals("unread"))
    		{
    			int num = temp.getNotificationnumber();
    		    ClientApp.ProtocolHandler.Invoke(RequestType.UpdateNotification, num, null, false);
    			temp.setStatus("read");
    		}
			notificationsCenterTable.refresh();
		}
    }
    
    /**
     * Notifications status pressed.
     * When pressed NotificationsStatus, the table data change between read and unread status 
     * @param event the event
     */
    @FXML
    void NotificationsStatusPressed(ActionEvent event) {
      	if (allNotificationsShow)
    	{
  			allNotificationsShow=false;
  			NotificationsStatusBtn.setText("Show All Notifications");
  			changeNotificationsLbl.setText("Show Unread Notifications");
        	notificationsCenterTable.setItems(unreadNotificationTableList);
        	if(unreadNotificationTableList != null)
        	if(unreadNotificationTableList.size() == 0) {
        		ErrorLabel.setText("There are no new notifications");
        		ErrorLabel.setVisible(true);
    		}
    	}
    	else
    	{
    		ErrorLabel.setVisible(false);
    		allNotificationsShow=true;
    		changeNotificationsLbl.setText("Show All Notifications");
        	NotificationsStatusBtn.setText("Show Unread Notifications");
        	notificationsCenterTable.setItems(AllnotificationTableList);
    	}
    }
    
    /**
     * Refresh pressed.
     * When pressed refresh the table set the last update data
     * @param event the event
     */
    @FXML
    void refreshPressed(MouseEvent event) {
    	onExit();
    	onEnter();
    }
    
    /**
     * On enter.
     * The first action to run -  initialize the notifications tables columns and set the customer notifications data
     */
    @SuppressWarnings("unchecked")
	@Override
	public void onEnter()
	{
    	allNotificationsShow = false;
    	notificationNumber.setCellValueFactory(new PropertyValueFactory<NotificationInTable, Integer>("notificationnumber"));
		sendFrom.setCellValueFactory(new PropertyValueFactory<NotificationInTable, String>("from"));
		content.setCellValueFactory(new PropertyValueFactory<NotificationInTable, String>("content"));
		status.setCellValueFactory(new PropertyValueFactory<NotificationInTable, String>("status"));
		AllnotificationTableList = (ObservableList<NotificationInTable>) LoginController.windowControl.peekPipe("All Notification");
    	//notificationsCenterTable.setItems(AllnotificationTableList);
    	
		thread =new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(2000);
					
					
					AllnotificationTableList = (ObservableList<NotificationInTable>) LoginController.windowControl.peekPipe("All Notification");
					unreadNotificationTableList = createUnreadNotificationList();
					
					if(allNotificationsShow) {
				    	notificationsCenterTable.setItems(AllnotificationTableList);

					}
					else {
				    	notificationsCenterTable.setItems(unreadNotificationTableList);
					}
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});
		thread.start();
		ErrorLabel.setVisible(false);
    	changeNotificationsLbl.setText("Show Unread Notifications");
    	NotificationsStatusBtn.setText("Show All Notifications");
    	LoginController.windowControl.putPipe("notif_thread", thread);
	}
	
	/**
	 * On exit.
	 * The last action to run - clear the the notifications table
	 */
	@Override
	public void onExit() {	
		thread.interrupt();
		notificationsCenterTable.getItems().clear();

	}
}
