/*
 * 
 */
package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import Entities.ItemInList;
import Entities.Order;
import Entities.ShippingMethods;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *The Class OrderInformationController is the controller part of the Customer GUI.
 *The Class give the ability to the Customer to insert his order details 
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class OrderInformationController implements UserControl {
	
	/** The order. */
	Order order = new Order();
	
    /** The back button. */
    @FXML
    private Button backBtn;
    
    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The arrival date. */
    @FXML
    private DatePicker arrivalDate;

    /** The branch comboBox. */
    @FXML
    private ComboBox<String> branchCombobox;

    /** The city text. */
    @FXML
    private TextField cityText;
    
    /** The use greeting. */
    @FXML
    private CheckBox useGreeting;
    
    /** The delivery method. */
    @FXML
    private ToggleGroup deliveryMethod;
    
    /** The error label. */
    @FXML
    private Label errorLabel;
    
    /** The greeting text. */
    @FXML
    private TextArea greetingText;
    
    /** The shipping price. */
    @FXML
    private Label shippingPrice;
    
    /** The next button. */
    @FXML
    private Button nextBtn;

    /** The street address text. */
    @FXML
    private TextField streetAddressText;

    /** The pickup RB. */
    @FXML
    private RadioButton pickupRB;

    /** The shipping RB. */
    @FXML
    private RadioButton shippingRB;
    
    /** The hour slider. */
    @FXML
    private Slider hourSlider;

    /** The minutes slider. */
    @FXML
    private Slider minutesSlider;
    
    /** The time presented. */
    @FXML
    private Label timePresented;
    
    /** The system change. */
    boolean systemChange = false;
    
    /**
     * Hour slide.
     * When the event happened check if the hour it more the new data + 3 hours
     * @param event the event
     */
    @FXML
    void hourSlide(MouseEvent event) {
    	LocalTime timeMeasure = LocalTime.of((int)hourSlider.getValue(),(int)minutesSlider.getValue());
    	LocalDateTime arrivalFull = LocalDateTime.of(arrivalDate.getValue(), timeMeasure);
    	@SuppressWarnings("unused")
		long diff;

    		if(LocalDateTime.now().plusHours(3).isAfter(arrivalFull))
    		{
    			var minDT = LocalDateTime.now().plusHours(3);
    			arrivalDate.setValue(minDT.toLocalDate());
    			hourSlider.setValue(minDT.getHour());
    			minutesSlider.setValue(minDT.getMinute());
    		}
    	
    	/*
		if((required = Duration.between(LocalDateTime.now(),LocalDateTime.of(arrivalDate.getValue(),timeMeasure))
				.toSeconds()) < 3*3600) 
		{
			systemChange = true;
			var minimumDate = LocalDateTime.of(arrivalDate.getValue(), timeMeasure).plusSeconds(required);
			arrivalDate.setValue(minimumDate.toLocalDate());
			hourSlider.setValue(minimumDate.getHour());
			minutesSlider.setValue(minimumDate.getMinute());
		}
		*/
    }

    /**
     * Minute slide.
     *W hen the event happened check if the minutes are  more the new data + 3 hours
     * @param event the event
     */
    @SuppressWarnings("unused")
	@FXML
    void minuteSlide(MouseEvent event) {
    	LocalTime timeMeasure = LocalTime.of((int)hourSlider.getValue(),(int)minutesSlider.getValue());
    	LocalDateTime arrivalFull = LocalDateTime.of(arrivalDate.getValue(), timeMeasure);
    	long diff;

    		if(LocalDateTime.now().plusHours(3).isAfter(arrivalFull))
    		{
    			var minDT = LocalDateTime.now().plusHours(3);
    			arrivalDate.setValue(minDT.toLocalDate());
    			hourSlider.setValue(minDT.getHour());
    			minutesSlider.setValue(minDT.getMinute());
    		}
    	

    	/*else if((diff = Duration.between(LocalDateTime.now(),LocalDateTime.of(arrivalDate.getValue(),timeMeasure))
				.toSeconds()) < 3*3600) 
		{
			System.out.println(diff);
			systemChange = true;
			var minimumDate = LocalDateTime.of(arrivalDate.getValue(), timeMeasure).plusSeconds(diff);
			arrivalDate.setValue(minimumDate.toLocalDate());
			hourSlider.setValue(minimumDate.getHour());
			minutesSlider.setValue(minimumDate.getMinute());
		}*/
    }
    
    /**
     * Change date event.
     *
     * @param event the event
     */
    @FXML
    void changeDateEvent(ActionEvent event) {

    }
    
    /**
     * Next button click.
     * When the next pressed check if the data is valid ,update the data into the Data Base and go to the next page (Customer Payment Window )
     * @param event the event
     */
    @SuppressWarnings("unchecked")
	@FXML
    void nextBtn_Click(ActionEvent event) {
    	if(!isInputValid()) return;
    	order.setBranchName(branchCombobox.getSelectionModel().getSelectedItem().toString());
    	order.setItems((ArrayList<ItemInList>)LoginController.windowControl.getPipe("CartItems"));
    	LocalTime time = LocalTime.of((int)(hourSlider.getValue()), (int)(minutesSlider.getValue()));
    	order.setOrderDate(Utilities.GenericUtilties.Convert_LocalDate_To_SQLDate(LocalDateTime.now().toLocalDate(),LocalDateTime.now().toLocalTime()));

    	if(deliveryMethod.getSelectedToggle().equals(shippingRB))
    	{
    		order.setShippingMethod(ShippingMethods.shipping);
    		LoginController.windowControl.putPipe("isShipping", true);
    		order.setAddress(streetAddressText.getText());
    		order.setCity(cityText.getText());
        	order.setShippingDate(Utilities.GenericUtilties.Convert_LocalDate_To_SQLDate(arrivalDate.getValue(),time));
    	}
    	else if(deliveryMethod.getSelectedToggle().equals(pickupRB))
    	{
    		order.setShippingMethod(ShippingMethods.pickup);
    		LoginController.windowControl.putPipe("isShipping", false);
    		order.setAddress(null);
    		order.setCity(null);
        	order.setShippingDate(Utilities.GenericUtilties.Convert_LocalDate_To_SQLDate(arrivalDate.getValue(),time));
    	}
    	if(useGreeting.isSelected())
    		order.setGreetingCard(greetingText.getText());
    	else
    		order.setGreetingCard(null);
    	order.setUserID(ClientApp.UserID);
    	LoginController.windowControl.putPipe("orderInfo",order);
    	LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerPaymentWindow.fxml");
    }
    
    /**
     * Checks if is input valid.
     * @return true, if is input valid
     */
    private boolean isInputValid()
    {
    	StringBuilder errorNotification = new StringBuilder("");
    	boolean valid = true;
    	if(deliveryMethod.getSelectedToggle() == null)
    	{
    		errorNotification.append("Must select delivery method\n");
    		valid = false;
    	}
    	else
    	{
	    	if(deliveryMethod.getSelectedToggle().equals(shippingRB))
	    	{
		    	if(cityText.getText().equals("") || streetAddressText.getText().equals(""))
		    	{
		    		errorNotification.append("Address fields cannot be empty\n");
		    		valid = false;
		    	}
		    	if(arrivalDate.getValue() == null)
		    	{
		    		errorNotification.append("Please select arrival date\n");
		    		valid = false;
		    	}
	    	}
    	}
    	if(branchCombobox.getValue() == null)
    	{
    		errorNotification.append("Please select a branch\n");
    		valid = false;
    	}
    	if(useGreeting.isSelected() && greetingText.getText().equals(""))
    	{
    		errorNotification.append("Greeting cannot be empty\n");
    		valid = false;
    	}

    	
    	errorLabel.setText(errorNotification.toString());
    	return valid;
    }
    
    /** The back. */
    boolean back = false;
	
	/**
	 * Back button press.
	 * When the back pressed go to the previous page (Customer Cart)
	 * @param event the event
	 */
	@FXML
	void backBtn_press(ActionEvent event) {
		back = true;
		LoginController.windowControl.setUserControl("/gui/usercontrols/CustomerCart.fxml");
	}
	
	/**
	 * On enter.
	 * The first action to run - set the data from the Data Base and insert options by the shipping method
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		if(LoginController.windowControl.getPipe("OrderCompleted")!=null) {
			clearAll();
		}
		
		arrivalDate.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();

				setDisable(empty || date.compareTo(today) < 0);
			}
		});
		arrivalDate.setValue(LocalDate.now());

		ClientApp.ProtocolHandler.Invoke(RequestType.GetBranches, null, null, true);
		ObservableList<String> branches = (ObservableList<String>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetBranches);
		branchCombobox.setItems((ObservableList<String>)branches);
        hourSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(!oldValue.equals(newValue))
					timePresented.setText(String.format("%02d:%02d", newValue.intValue(),(int)minutesSlider.getValue()));
				else
					systemChange = false;
			}
		});
        minutesSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(!oldValue.equals(newValue))
					timePresented.setText(String.format("%02d:%02d", (int)hourSlider.getValue(),newValue.intValue()));
				else
					systemChange = false;
			}
		});
		deliveryMethod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@SuppressWarnings("unused")
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(newValue == null) return;
				if(!newValue.equals(oldValue))
				{
					if(newValue == null ) return;
					
					if(newValue.equals(pickupRB) )
					{
						cityText.setDisable(true);
						streetAddressText.setDisable(true);
						arrivalDate.setDisable(false);
						hourSlider.setDisable(false);
						minutesSlider.setDisable(false);
					}
					else
					{
						cityText.setDisable(false);
						streetAddressText.setDisable(false);
						arrivalDate.setDisable(false);
						hourSlider.setDisable(false);
						minutesSlider.setDisable(false);
					}
				}
			}
		});
		arrivalDate.valueProperty().addListener((observable, oldDate, newDate) -> { minuteSlide(null);});
		minuteSlide(null);
		shippingPrice.setText("+ "+ Utilities.Constants.SHIPPING + " " +Utilities.Constants.SHEKEL );
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
			});
			back = false;
	}
	
	/**
	 * Clear all.
	 */
	public void clearAll()
	{
    	cityText.clear();
    	deliveryMethod.selectToggle(null);
    	branchCombobox.getSelectionModel().select(null);
		streetAddressText.clear();
		useGreeting.setSelected(false);
		greetingText.clear();
	}
}
