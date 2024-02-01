/*
 * 
 */
package controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Entities.Access;
import Entities.ItemInList;
import Entities.NotificationInTable;
import Entities.RedNotificationCircle;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Class CustomerFrameController is the controller part of the customer static environment GUI
 * The GUI does not change and Contains Customer functions 
 * The Controller implements the IContainable interface that give to the controller the ability to add different user controls
 */
public class CustomerFrameController implements IContainable{
	
	/** The map that contains the parents(roots) */
	Map<String,Parent> map = new HashMap<>();
	
	/** The user control map. */
	Map<String,UserControl> uc_map = new HashMap<String, UserControl>();
	
	/** The thread. */
	Thread thread;
	
    /** The bell button. */
    @FXML
    private ImageView bellBtn;

    /** The cart button. */
    @FXML
    private ImageView cartBtn;

    /** The control container. */
    @FXML
    private AnchorPane controlContainer;

    /** The exit button. */
    @FXML
    private ImageView exitBtn;

    /** The home button. */
    @FXML
    private ImageView homeBtn;

    /** The logout button. */
    @FXML
    private Button logoutBtn;

    /** The name label. */
    @FXML
    private Label nameLabel;

    /** The orders button. */
    @FXML
    private Button ordersBtn;
    
    /** The cart red circle image. */
    @FXML
    private ImageView cartRedCircleImage;
    
    /** The cart red circle label. */
    @FXML
    private Label cartRedCricleLable;
    
    /** The bell red circle image. */
    @FXML
    private ImageView bellRedCircleImage;
    
    /** The bell red circle label. */
    @FXML
    private Label bellRedCricleLable;
    
    /** The cart notifications number. */
    private int cartNotificationsNumber;
    
    /** The media play. */
    private MediaPlayer mediaPlay;
    
    /**
     * Initialize the customer function 
     *	Reset the notifications 
     */
    void init(){
    	ClientApp.ProtocolHandler.Invoke(RequestType.GetCart, null, null, true);
    	@SuppressWarnings("unchecked")
		ObservableList<ItemInList> list=(ObservableList<ItemInList>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetCart);
    	for (ItemInList itemInList : list) {
    		cartNotificationsNumber+=itemInList.getQuantity();
		}
    	if (cartNotificationsNumber>0)
    	{
    		cartRedCircleImage.setVisible(true);
    		cartRedCricleLable.setText(String.valueOf(cartNotificationsNumber));
    		cartRedCricleLable.setVisible(true);
    	}
    	nameLabel.setText(ClientApp.UserID.toString());
    	RedNotificationCircle cartNotification=new RedNotificationCircle(cartRedCircleImage, cartRedCricleLable, cartNotificationsNumber);
    	LoginController.windowControl.putPipe("cartLabel", cartNotification);
    
    	bellRedCricleLable.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				
				if(Integer.parseInt(newValue) <= 0)
				{
					bellRedCircleImage.setVisible(false);
					bellRedCricleLable.setVisible(false);
				}
				
				else
				{
					bellRedCircleImage.setVisible(true);
					bellRedCricleLable.setVisible(true);
				}
				if(Integer.parseInt(newValue)>Integer.parseInt(oldValue))
				{
//					File tone= new File("src\\tones\\Messenger notification tone.mp3");
//					//File tone= new File("src\\tones\\MiSam.mp3");
//					mediaPlay =new MediaPlayer(new javafx.scene.media.Media(tone.toURI().toString()));
//					mediaPlay.play();				
				}
			}
		});
    	
    	
    
     	   	
		thread =new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					ClientApp.ProtocolHandler.Invoke(RequestType.GetNotification, null, null, true);
					@SuppressWarnings("unchecked")
					ObservableList<NotificationInTable> list = (ObservableList<NotificationInTable>) ClientApp.ProtocolHandler.GetResponse(RequestType.GetNotification);
					LoginController.windowControl.putPipe("All Notification", list);
					int cnt=0;
					for(NotificationInTable not: list) {
						if(not.getStatus().equals("unread")) cnt++;
					}
					
					String notificationNumber=String.valueOf(cnt);
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							bellRedCricleLable.setText(notificationNumber);							
						}
			    		});
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});
		thread.start();
    	
    }

    /**
     * Bell pressed.
     *	When the ball pressed go to the notifications window
     * @param event the event
     */
    @FXML
    void bellPressed(MouseEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/NotificationWindow.fxml");
    }

    /**
     * Cart pressed.
     *	When the cart pressed go to the cart window 
     * @param event the event
     */
    @FXML
    void cartPressed(MouseEvent event) {
    	if(ClientApp.UserStatus == Access.inactive)  {
    		Alert confirmAlert = new Alert(AlertType.NONE);
			confirmAlert.setTitle("Not registered customer");
			confirmAlert.setContentText("Your account is inactive\nContact Zerli's administration");
			ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
			confirmAlert.getDialogPane().getButtonTypes().add(ok);
			confirmAlert.showAndWait();
			return;
    	}
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCart.fxml");
    }

    /**
     * Exit pressed.
     *	When the exit pressed exit from the program 
     * @param event the event
     */
    @FXML
    void exitPressed(MouseEvent event) {
    	thread.interrupt();
    	ClientApp.ProtocolHandler.Invoke(RequestType.SetLogOut,null,null,false);
    	try {
    		if(LoginController.windowControl.peekPipe("notif_thread") != null)
    			((Thread)LoginController.windowControl.peekPipe("notif_thread")).interrupt();
			ClientApp.ClientConnection.closeConnection();
			//System.out.println(ClientApp.ClientConnection.isConnected());
			System.exit(0);
		} catch (IOException e) {e.printStackTrace();}
    	
    }

    /**
     * Home pressed.
     * When the home pressed go to the home page 
     * @param event the event
     */
    @FXML
    void homePressed(MouseEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerHomePage.fxml");
    }

    /**
     * Orders pressed.
     * When the orders pressed go to the order window
     * @param event the event
     */
    @FXML
    void ordersPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerViewOrders.fxml");
    }

    /**
     * Press logout.
     * When the logout pressed go to the insert user details window
     * @param event the event
     */
    @SuppressWarnings("static-access")
	@FXML
    void pressLogout(ActionEvent event) {
    	thread.interrupt();
		if(LoginController.windowControl.peekPipe("notif_thread") != null)
			((Thread)LoginController.windowControl.peekPipe("notif_thread")).interrupt();
    	LoginController.windowControl.stage.close();
    	Stage newStage = new Stage();
		Parent root = null;
		try 
		{
			ClientApp.ProtocolHandler.Invoke(RequestType.SetLogOut,null,null,false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/mainframes/Login.fxml"));
			root =  loader.load(); 
			Utilities.GenericUtilties.SetWindowMovable(root, newStage);
			Scene scene = new Scene(root);
			newStage.initStyle(StageStyle.UNDECORATED);
			newStage.setScene(scene); 
			newStage.show();
			LoginController.windowControl.stage = newStage;	
		}
		catch (Exception e) {e.printStackTrace();	}
	
    }
    
    /**
     * My account pressed.
     * When my account pressed go to the customer personal details
     * @param event the event
     */
    @FXML
    void myAccountPressed(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerAccountInfo.fxml");
    }

	/**
	 * Gets the control container.
	 * Return the user control that existing in the frame 
	 * @return the control container
	 */
	@Override
	public AnchorPane getControlContainer() {
		return controlContainer;		
	}

	


}
