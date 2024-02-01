/*
 * 
 */
package controllers;

import java.util.Optional;

import Entities.Survey;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;

/**
 * The Class AddNewSurveyController is the controller part of the service employees GUI.
 * THe class give to the service employees the ability to crate a new survey.
 * The class implement user control interface to be able to insert into frame users GUI
 */
public class AddNewSurveyController implements UserControl {
	
	/** The survey. */
	private Survey survey=new Survey();
	
	/** The index. */
	private int index;
	
	/** The Question text array. */
	private TextField[] QuestionTextArray=new TextField[6];
    
    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The add survey. */
    @FXML
    private Button addSurvey;

    /** The add survey label. */
    @FXML
    private Label addSurveyLabel;

    /** The add violation label. */
    @FXML
    private Label addViolationLabel;

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

    /** The survey name label. */
    @FXML
    private TextField surveyName;

	/**
	 * On enter.
	 * The first action to run - crate the connection between the GUI questions labels to the controller
	 */
	@Override
	public void onEnter() {
		addSurveyLabel.setVisible(false);
		SetQuestions();		
	}
    
    /**
     * Sets the questions.
     */
    private void SetQuestions() {

  	   QuestionTextArray[0]=questionText1;
  	   QuestionTextArray[1]=questionText2;
  	   QuestionTextArray[2]=questionText3;
  	   QuestionTextArray[3]=questionText4;
  	   QuestionTextArray[4]=questionText5;
  	   QuestionTextArray[5]=questionText6;
  }
    
    /**
     * Adds the survey click.
     * Add the survey into the data base if it is valid
     * @param event the event
     */
    @FXML
    void addSurvey_Cllick(ActionEvent event) {
    	
	    	index=0;
	    	if(surveyName.getText().length() < 4) {
				addSurveyLabel.setText("**Survey name must be longer than 3 letters");
				addSurveyLabel.setVisible(true);
	    		new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						addSurveyLabel.setVisible(false);
					}
				}).start();
	    		return;
	    	}
	    	for (TextField textField : QuestionTextArray) {
				if(textField.getText().equals(""))
				{
					addSurveyLabel.setText("**Please answer question "+(index+1));
					addSurveyLabel.setVisible(true);
		    		
					Thread thread =new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							addSurveyLabel.setVisible(false);
						}
					});
					thread.start();
				break;
				}
				survey.getQuestions()[index]=textField.getText();
				index++;
				}
		    if(index==6)
		    {	
		    	survey.setContent(surveyName.getText());
		    	ClientApp.ProtocolHandler.Invoke(RequestType.SaveSurvey, survey, null, false);
		    	Alert confirmAlert = new Alert(AlertType.NONE);
				confirmAlert.setTitle("The survey added successfully");
				confirmAlert.setContentText("Thank you , the Survey update into the system");
				ButtonType ok = new ButtonType("ok",ButtonData.OK_DONE);
				confirmAlert.getDialogPane().getButtonTypes().add(ok);
				Optional<ButtonType> result = confirmAlert.showAndWait();
				result.ifPresent(response -> { 
					if(response == ok) resetQuestion();
				});
		    	
		    }
    }
    
    /**
     * Reset question to "".
     */
    private void resetQuestion() {
    	for (TextField textField : QuestionTextArray) {
			textField.setText("");
    	}
    	surveyName.setText("");
	}

    /**
     * Back button pressed.
     * When the back pressed go to the previous page (service home page) and if its needed save the typed data 
     * @param event the event
     */
    @FXML
    void BackBtnPressed(ActionEvent event) {
    	if(inputValid())
    	{
	    	Alert confirmAlert = new Alert(AlertType.NONE);
			confirmAlert.setTitle("Leaving Add New Survey");
			confirmAlert.setContentText("Do you want to save typed data?");
			ButtonType yes = new ButtonType("Yes", ButtonData.YES);
			ButtonType no = new ButtonType("No",ButtonData.NO);
			confirmAlert.getDialogPane().getButtonTypes().add(yes);
			confirmAlert.getDialogPane().getButtonTypes().add(no);
			Optional<ButtonType> result = confirmAlert.showAndWait();
			result.ifPresent(response -> { 
				if(response == no) resetQuestion();
			});
    	}
    	LoginController.windowControl.setUserControl("/gui/usercontrols/SurveyMangerHomeScreen.fxml");
    }
	
	/**
	 * On exit.
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Input valid.
	 *
	 * @return true, if successful
	 */
	private boolean inputValid()
	{
		for (TextField textField : QuestionTextArray) {
			if (!textField.getText().equals(""))
				return true;
    	}
		if(!surveyName.getText().equals(""))
			return true;
		return false;
	}

}

