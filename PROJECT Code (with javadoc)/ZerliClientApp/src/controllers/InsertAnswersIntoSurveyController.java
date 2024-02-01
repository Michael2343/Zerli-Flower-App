/*
 * 
 */
package controllers;

import java.util.Optional;
import Entities.Survey;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;

/**
 *The Class InsertAnswersIntoSurveyController is the controller part of the service GUI.
 *The Class give the ability to the service to insert Customer's answers into the Data Base
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class InsertAnswersIntoSurveyController implements UserControl{
	
	/** The spinner array. */
	@SuppressWarnings("unchecked")
	private Spinner<Integer>[] spinnerArray= new Spinner[6];
	
	/** The survey. */
	private Survey survey=new Survey();
	
	/** The index. */
	private int index;
	
	/** The Question text array. */
	private TextField[] QuestionTextArray=new TextField[6];
	
	/** The survey names. */
	private ObservableList<String> surveyNames;
    
    /** The Submit result 1. */
    @FXML
    private Button SubmitResult1;

    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The question text 1. */
    @FXML
    private TextField questionText1;

    /** The question text 2. */
    @FXML
    private TextField questionText2;

    /** The question text 3. */
    @FXML
    private TextField questionText3;

    /** The question text 4. */
    @FXML
    private TextField questionText4;

    /** The question text 5. */
    @FXML
    private TextField questionText5;

    /** The question text 6. */
    @FXML
    private TextField questionText6;

    /** The spinner question 1. */
    @FXML
    private Spinner<Integer> spinnerQuestion1;

    /** The spinner question 2. */
    @FXML
    private Spinner<Integer> spinnerQuestion2;

    /** The spinner question 3. */
    @FXML
    private Spinner<Integer> spinnerQuestion3;

    /** The spinner question 4. */
    @FXML
    private Spinner<Integer> spinnerQuestion4;

    /** The spinner question 5. */
    @FXML
    private Spinner<Integer> spinnerQuestion5;

    /** The spinner question 6. */
    @FXML
    private Spinner<Integer> spinnerQuestion6;
    

    /** The submit label. */
    @FXML
    private Label submitLabel;

    /** The survey name selector. */
    @FXML
    private ComboBox<String> surveyNameSelector;
    
	/** The unsaved. */
	private boolean unsaved=true;

    /**
     * Back button pressed.
     * When the back pressed go to the previous page (Survey Manger Home Screen) and if its needed save the typed data 
     * @param event the event
     */
    @FXML
    void BackBtnPressed(ActionEvent event) 
    {
    	if(inputValid())
    	{
	      	Alert confirmAlert = new Alert(AlertType.NONE);
			confirmAlert.setTitle("Leaving Insert Answers Into Survey ");
			confirmAlert.setContentText("Do you want to save typed data?");
			ButtonType yes = new ButtonType("Yes", ButtonData.YES);
			ButtonType no = new ButtonType("No",ButtonData.NO);
			confirmAlert.getDialogPane().getButtonTypes().add(yes);
			confirmAlert.getDialogPane().getButtonTypes().add(no);
			Optional<ButtonType> result = confirmAlert.showAndWait();
			result.ifPresent(response -> { 
				if(response == no) {unsaved=true; }
				else unsaved=false;
			});
    	}
    	LoginController.windowControl.setUserControl("/gui/usercontrols/SurveyMangerHomeScreen.fxml");
    }

    /**
     * Input validation.
     *
     * @return true, if successful
     */
    private boolean inputValid() {
    	if(spinnerArray[0].getValue()!=null)
    	{
	    	for (Spinner<Integer> spinner : spinnerArray) {	
	    		
	    		if(spinner.getValue()!=0)
	    		{
	    			return true;
	    		}
	    	}
	    }
		return false;
	}

	/**
	 * On enter.
	 * The first action to run - Set the surveys into the comboBox selection
	 */
	@Override
    public void onEnter() 
    {
		SetArrays();
    	setComboBox();
    }
    
    /**
     * Sets the arrays.
     */
    private void SetArrays()
    {
 	   spinnerArray[0]=spinnerQuestion1;
 	   spinnerArray[1]=spinnerQuestion2;
 	   spinnerArray[2]=spinnerQuestion3;
 	   spinnerArray[3]=spinnerQuestion4;
 	   spinnerArray[4]=spinnerQuestion5;
 	   spinnerArray[5]=spinnerQuestion6;
 	   ////
 	   QuestionTextArray[0]=questionText1;
 	   QuestionTextArray[1]=questionText2;
 	   QuestionTextArray[2]=questionText3;
 	   QuestionTextArray[3]=questionText4;
 	   QuestionTextArray[4]=questionText5;
 	   QuestionTextArray[5]=questionText6;
    }
    
    /**
     * Sets the combo box with data from the Data Base by the user request.
     */
    @SuppressWarnings("unchecked")
	private void setComboBox() 
    {	
    	ClientApp.ProtocolHandler.Invoke(RequestType.GetSurveysNames, survey, null, true);
    	surveyNames=(ObservableList<String>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetSurveysNames);
    	surveyNameSelector.setItems(FXCollections.observableArrayList(surveyNames));
    }

    /**
     * Sets the survey.
     *
     * @param event the event
     */
    @FXML
    void SetSurvey(ActionEvent event) 
    {
		if(surveyNameSelector.getSelectionModel().getSelectedItem()!=null)
		{
			String	string=surveyNameSelector.getSelectionModel().getSelectedItem().toString();
			survey.setId(Integer.parseInt(string.substring(0,string.indexOf(" "))));
	    	SetSurveyById(survey.getId());
		}
    }
    
    /**
     * Sets the survey by id.
     *
     * @param id the id
     */
    private void SetSurveyById(int id) 
    {
    	if(unsaved)
    	{
    		SetSpinners();
    	}
		ClientApp.ProtocolHandler.Invoke(RequestType.GetSurvey, id, null, true);
		survey=(Survey)ClientApp.ProtocolHandler.GetResponse(RequestType.GetSurvey);
		setQuestion();
    }
    
    /**
     * Sets the spinners.
     */
    private void SetSpinners()
    {
		for (Spinner<Integer> spinner : spinnerArray) {
			SpinnerValueFactory<Integer> valueFactory = 
					new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
			valueFactory.setValue(0);
			spinner.setValueFactory(valueFactory);
		}
    }

    /**
     * Sets the question.
     */
    private void setQuestion() 
    {
    	for (int i = 0; i < QuestionTextArray.length; i++) {
    		QuestionTextArray[i].setText(survey.getQuestions()[i]);
    	}
    }

/**
 * Submit result click.
 * When pressed submit validate the data and insert it into the Data Base
 * @param event the event
 */
@FXML
void SubmitResult_Click(ActionEvent event) { 
	if (spinnerArray[0].getValue()==null) {
		return;
	}
	index=0;
	for (Spinner<Integer> spinner : spinnerArray) {		
		if(spinner.getValue()==0)
		{	
			submitLabel.setText("**At Q"+(index+1)+" the answer must be 1-10");
			submitLabel.setVisible(true);
			
			Thread thread =new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					submitLabel.setVisible(false);
				}
			});
			thread.start();
		break;
			
		}
		survey.getAnswers()[index]=spinner.getValue();
		index++;
	}
	if (index==6)
	{
		String	string=surveyNameSelector.getSelectionModel().getSelectedItem().toString();
		survey.setId(Integer.parseInt(string.substring(0,string.indexOf(" "))));
		ClientApp.ProtocolHandler.Invoke(RequestType.SaveSurveyAnswers, survey, null, false);
		Alert confirmAlert = new Alert(AlertType.NONE);
		confirmAlert.setTitle("Your feedback update into the system");
		confirmAlert.setContentText("Thank you for your feedback");
		ButtonType ok = new ButtonType("ok",ButtonData.OK_DONE);
		confirmAlert.getDialogPane().getButtonTypes().add(ok);
		Optional<ButtonType> result = confirmAlert.showAndWait();
		result.ifPresent(response -> { 
			if(response == ok) SetSpinners();
		});
		
	}
	
}

/**
 * On exit.
 */
@Override
public void onExit() {
	
}

}
