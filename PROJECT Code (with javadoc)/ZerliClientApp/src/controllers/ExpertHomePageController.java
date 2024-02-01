/*
 * 
 */
package controllers;

import Entities.Survey;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *The Class ExpertHomePageController is the controller part of the Expert GUI.
 *The Class give the ability to the Expert to select the survey to analyze it 
 *The class implement user control interface to be able to insert into frame users GUI
 */
public class ExpertHomePageController implements UserControl{
	
	/** The survey names. */
	private ObservableList<String> surveyNames;
	
	/** The survey. */
	private Survey survey=new Survey();
	
    /** The active panel container. */
    @FXML
    private AnchorPane activePanelContainer;

    /** The required surveys text. */
    @FXML
    private TextField requiredSurveysText;

    /** The survey name selector. */
    @FXML
    private ComboBox<String> surveyNameSelector;
    
    /**
     * Sets the combo box.
     */
    @SuppressWarnings("unchecked")
	private void setComboBox() 
    {	
    	surveyNameSelector.getItems().clear();
    	ClientApp.ProtocolHandler.Invoke(RequestType.GetSurveysNames, survey, null, true);
    	surveyNames=(ObservableList<String>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetSurveysNames);
    	surveyNameSelector.setItems(surveyNames);
    	
    }
    
    /**
     * Next button pressed.
     * When the next pressed go to the next page ( Survey Histogram)
     * @param event the event
     */
    @FXML
    void NextBtnPressed(ActionEvent event) {
    	if(LoginController.windowControl.peekPipe("surveyForExpert") == null) return;
    	requiredSurveysText.clear();
    	LoginController.windowControl.setUserControl("/gui/usercontrols/SurveyHistogram.fxml");
    }

    /**
     * Survey name selector pressed.
     * When pressed Survey name selector take the survey option and save it until the next press
     * @param event the event
     */
    @FXML
    void surveyNameSelectorPressed(ActionEvent event) 
    {
    	if(surveyNameSelector.getSelectionModel().getSelectedItem()!=null)
		{
			String	string=surveyNameSelector.getSelectionModel().getSelectedItem();
			requiredSurveysText.setText(string.substring(string.indexOf(" ")));
			LoginController.windowControl.putPipe("surveyForExpert", string);
		}
    }
		
	/**
	 * On enter.
	 */
	@Override
	public void onEnter() {
		setComboBox();
	}
	
	/**
	 * On exit.
	 * The first action to run - set the comboBox to the Surveys names
	 */
	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

}
