package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * The Class SurveyMangerHomeScreenController - implements UserControl for switching sub windows.
 * Responsible : shows service employee Survey home screen   
 */
public class SurveyMangerHomeScreenController implements UserControl {

    /** The activePanelContainer - AnchorPane that contains all the FXML element */
    @FXML
    private AnchorPane activePanelContainer;

    /**
     * Open the Add new survey window.
     * @param event - button pressed 
     */
    @FXML
    void AddNewSurvey(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/AddNewSurvey.fxml");
    }

    /**
     * Open the Insert survey Answers window.
     * @param event - button pressed 
     */
    @FXML
    void InsertAnswers(ActionEvent event) {
    	LoginController.windowControl.setUserControl("/gui/usercontrols/InsertAnswersIntoSurvey.fxml");
    }

	/**
	 * Commands that the Controller do the screen will do while uploading window
	 */
    
	@Override
	public void onEnter() {}
	
	/**
	 * Commands that Controller do the screen will do while existing window
	 */
	@Override
	public void onExit() {}

}
