/*
 * 
 */
package controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import Entities.Order;
import Entities.OrderStatus;
import Entities.PaymentMethods;
import Entities.RedNotificationCircle;
import Entities.paymentInfo;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *The Class PaymentController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to see his  order cost ,payment information and chose his  payment method 
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class PaymentController implements UserControl {

	/** The current order. */
	private Order currentOrder;
	
	/** The is shipping. */
	private boolean isShipping;
	
	/** The save. */
	boolean save = false;
    
    /** The pay details. */
    paymentInfo payDetails;
    
    /** The back button. */
    @FXML
    private Button backBtn;
    
    /** The active panel container 2. */
    @FXML
    private AnchorPane activePanelContainer2;

    /** The card number text 1. */
    @FXML
    private TextField cardNumTxt1;

    /** The card number text 2. */
    @FXML
    private TextField cardNumTxt2;

    /** The card number text 3. */
    @FXML
    private TextField cardNumTxt3;

    /** The card number text 4. */
    @FXML
    private TextField cardNumTxt4;

    /** The credit card dialog. */
    @FXML
    private AnchorPane creditCardDialog;

    /** The credit card RB. */
    @FXML
    private RadioButton creditCardRB;

    /** The cvv text. */
    @FXML
    private TextField cvvText;

    /** The discount percentage label. */
    @FXML
    private Label discountPercentageLabel;

    /** The exertion month CB. */
    @FXML
    private ComboBox<String> expMonthCB;

    /** The net price label. */
    @FXML
    private Label netPriceLabel;

    /** The next button. */
    @FXML
    private Button nextBtn;

    /** The payment method. */
    @FXML
    private ToggleGroup paymentMethod;

    /** The total price label. */
    @FXML
    private Label totalPriceLabel;

    /** The w. */
    @FXML
    private Label w;

    /** The year CB. */
    @FXML
    private ComboBox<String> yearCB;

    /** The zerli coins dialog. */
    @FXML
    private VBox zerliCoinsDialog;

    /** The zerli coins RB. */
    @FXML
    private RadioButton zerliCoinsRB;

    /** The post pay balance label. */
    @FXML
    private Label postPayBalanceLbl;

    /** The balance label. */
    @FXML
    private Label balanceLbl;
    
    /** The error label. */
    @FXML
    private Label errorLabel;
    
    /**
     * Next button click.
     * When the next pressed in based on the payment method decrease the order cost from the coin or charge the credit card 
     * Reset the cart notification 
     * Then go to the next page (Customer Outro View Window ) 
     * @param event the event
     */
    @FXML
    void nextBtn_Click(ActionEvent event) {
    	if(!validateInput()) return;
    	if(paymentMethod.getSelectedToggle().equals(creditCardRB))
    		currentOrder.setPaymentMethod(PaymentMethods.credit_card);
    	else if(paymentMethod.getSelectedToggle().equals(zerliCoinsRB))
    		currentOrder.setPaymentMethod(PaymentMethods.zerli_coins);
    	
    	if(paymentMethod.getSelectedToggle() == zerliCoinsRB)
    	{
    		if(payDetails.ZerliCoins < currentOrder.getTotalPrice())
    		{
    			Alert confirmAlert = new Alert(AlertType.ERROR);
    			confirmAlert.setTitle("No funds");
    			confirmAlert.setContentText("Insufficient ZCoins to perform payment");
    			ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    			confirmAlert.getDialogPane().getButtonTypes().add(ok);
    			confirmAlert.showAndWait();
    			return;
    		}
    		else
    		{
    			var newval = payDetails.ZerliCoins - currentOrder.getTotalPrice();
    			ClientApp.ProtocolHandler.Invoke(RequestType.UpdateZerliCoins, newval, null, false);
    		}
    	}
    	currentOrder.setStatus(OrderStatus.pending_confirm);
    	ClientApp.ProtocolHandler.Invoke(RequestType.InsertOrder, currentOrder, null, false);
    	////
    	RedNotificationCircle cartLabelAndImage=(RedNotificationCircle) LoginController.windowControl.getPipe("cartLabel");
    	int cartNotificationsNumber=cartLabelAndImage.getCartNotificationsNumber();
    	cartNotificationsNumber=0;
    	cartLabelAndImage.setCartNotificationsNumber(cartNotificationsNumber);
    	Label label;
    	ImageView image=new ImageView();
    	label=cartLabelAndImage.getLabel();
    	image=cartLabelAndImage.getImage();
		label.setVisible(false);
    	image.setVisible(false);
    	label.setText(String.valueOf(cartNotificationsNumber));
    	LoginController.windowControl.putPipe("cartLabel", cartLabelAndImage);
    	////
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerOutroViewWindow.fxml");
    	if(payDetails.newUser) 
    		ClientApp.ProtocolHandler.Invoke(RequestType.ResetNewCustomer,null, null, false);

    	clearAll();
    	LoginController.windowControl.putPipe("OrderCompleted",true);
    }
    
    /**
     * Validate input.
     *
     * @return true, if successful
     */
    public boolean validateInput()
    {
    	StringBuilder error = new StringBuilder("");
    	var creds = new String[] {cardNumTxt1.getText(),cardNumTxt2.getText(),cardNumTxt3.getText(),cardNumTxt4.getText()};
    	boolean valid = true;
    	if(paymentMethod.getSelectedToggle() == null)
    	{
    		error.append("Please select payment method\n");
    		valid = false;
    		return valid;
    	}
    	if(creditCardDialog.isVisible())
    	{
	    	for(String s : creds)
	    	{
	    		if(s.length() != 4)
	    		{
	    			error.append("Please enter valid credit card\n");
	    			valid = false;
	    			break;
	    		}
	    		try
	    		{
	    			Integer.parseInt(s);
	    		}
	    		catch(NumberFormatException e)
	    		{
	    			error.append("Please enter valid credit card\n");
	    			valid = false;
	    			break;
	    		}
	    	}
	    	if(cvvText.getText().equals("") || cvvText.getText().length() < 3)
	    	{
	    		error.append("CVV Cannot be empty\n");
	    		valid = false;
	    	}
	    	if(yearCB.getValue() == null || expMonthCB.getValue() == null)
	    	{
	    		error.append("Please enter a valid expiration date\n");
	    		valid = false;
	    	}
	    	else {
	    		int year= Integer.parseInt(yearCB.getValue());
	    		int month = Integer.parseInt(expMonthCB.getValue());
	    		if(LocalDateTime.now().getYear() == year) {
	    			if(LocalDateTime.now().getMonthValue() > month) {
	    				error.append("Please enter a valid expiration date\n");
	    			}
	    		}
	    	}
	    	
	    	errorLabel.setText(error.toString());
	    	return valid;
    	}
    	else
    	{
    		return valid;
    	}
    }

	/**
	 * On enter.
	 * The first action to run - get from the Data Base and set the customer zerli coin and credit card details into the GUI
	 * Check if their is enough zerli coin for this order
	 */
	@Override
	public void onEnter() {
		/* load user status */
		ClientApp.ProtocolHandler.Invoke(RequestType.GetUserCurrency,null ,null,true);
		payDetails = (paymentInfo)ClientApp.ProtocolHandler.GetResponse(RequestType.GetUserCurrency);
		isShipping = (boolean)LoginController.windowControl.peekPipe("isShipping");
		currentOrder = (Order)LoginController.windowControl.peekPipe("orderInfo");
    	int netPrice = (int) LoginController.windowControl.peekPipe("totalCost");
    	int discount = payDetails.newUser ? (int)(0.2f * netPrice) : 0;
    	int shipping = isShipping ? 15 : 0;
    	currentOrder.setTotalPrice(netPrice - discount + shipping);
    	
    	/** credit card view **/
    	if(!save)
    	{
			var currentYear = LocalDateTime.now().toLocalDate().getYear();
			var years = new ArrayList<String>();
			var months = new ArrayList<String>();
			for(var i = currentYear;i < currentYear + 12;i++)
				years.add(i + "");
			for(var i = 1;i<12;i++)
				months.add((i+"").length() == 1 ? ("0"+ i) : i + "");
			yearCB.setItems(FXCollections.observableArrayList(years));
			expMonthCB.setItems(FXCollections.observableArrayList(months));
			cardNumTxt1.setText(payDetails.creditPhrases[0]);
			cardNumTxt2.setText(payDetails.creditPhrases[1]);
			cardNumTxt3.setText(payDetails.creditPhrases[2]);
			cardNumTxt4.setText(payDetails.creditPhrases[3]);
			cvvText.setText(payDetails.CVV);
			yearCB.getSelectionModel().select(payDetails.expirationYear);
			expMonthCB.getSelectionModel().select(payDetails.expirationMonth);
    	}
		/** zerli coins view **/	
		balanceLbl.setText(payDetails.ZerliCoins + " Zerli Coins");
		postPayBalanceLbl.setText("" + (payDetails.ZerliCoins - currentOrder.getTotalPrice())+ " Zerli Coins");

		/** summary **/

		if(isShipping)
			w.setText(Utilities.Constants.SHIPPING + " " + Utilities.Constants.SHEKEL);
		else
			w.setText("-");
		
    	netPriceLabel.setText("" + netPrice + " " + Utilities.Constants.SHEKEL);
		discountPercentageLabel.setText(payDetails.newUser ? "-20%" : "-");
		totalPriceLabel.setText("" + currentOrder.getTotalPrice()+ " " + Utilities.Constants.SHEKEL);
		if(payDetails.ZerliCoins < currentOrder.getTotalPrice())
			zerliCoinsRB.setDisable(true);
		paymentMethod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(!newValue.equals(oldValue))
				{
					if(newValue.equals(creditCardRB))
					{
						creditCardDialog.setVisible(true);
						zerliCoinsDialog.setVisible(false);
					}
					else
					{

						creditCardDialog.setVisible(false);
						zerliCoinsDialog.setVisible(true);
					}
				}
				
			}
		});
		if(!save)
		{
			paymentMethod.selectToggle(creditCardRB);
		}
		
	}
	
	/** The back. */
	boolean back = false;
    
    /**
     * Back button press.
     * When the back pressed go to the previous page (Customer Order Information )
     * @param event the event
     */
    @FXML
    void backBtn_press(ActionEvent event) {
    	back = true;
		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerOrderInformation.fxml");
    }
	
	/**
	 * On exit.
	 * The first action to run - if its needed save the typed data
	 */
	@Override
	public void onExit() {
		if(!back) return;
		Alert confirmAlert = new Alert(AlertType.NONE);
		confirmAlert.setTitle("Leaving Order Information");
		confirmAlert.setContentText("Do you want to save typed data?");
		ButtonType yes = new ButtonType("Yes", ButtonData.YES);
		ButtonType no = new ButtonType("No",ButtonData.NO);
		confirmAlert.getDialogPane().getButtonTypes().add(yes);
		confirmAlert.getDialogPane().getButtonTypes().add(no);
		Optional<ButtonType> result = confirmAlert.showAndWait();
		result.ifPresent(response -> { 
			if(response == no) clearAll();
			else save = true;
		});
		back = false;
		
	}
	
	/**
	 * Clear all.
	 */
	private void clearAll() {
		save = false;
	}

}
