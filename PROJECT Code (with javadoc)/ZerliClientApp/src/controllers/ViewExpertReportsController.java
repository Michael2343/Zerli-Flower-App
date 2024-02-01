package controllers;

import java.io.IOException;
import java.util.ArrayList;

import Entities.SurveyResult;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewExpertReportsController implements UserControl {
	ArrayList<SurveyResult> results;
	
    @FXML
    private ListView<String> Reports;

    @FXML
    private AnchorPane activePanelContainer;

    @FXML
    void viewReportCllicked(ActionEvent event) {
    	if(Reports.getSelectionModel().getSelectedItem() != null)
    	{	
    	    		OpenReportViewer(results.get(Reports.getSelectionModel().getSelectedIndex()).getExpertSummary(),
    	    				results.get(Reports.getSelectionModel().getSelectedIndex()).getSurveyResults());
    	}
    }

	@SuppressWarnings("unchecked")
	@Override
	public void onEnter() {
		ClientApp.ProtocolHandler.Invoke(RequestType.GetReadySurveys, null, null, true);
		results = (ArrayList<SurveyResult>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetReadySurveys);
		for(SurveyResult sr : results)
			Reports.getItems().add(sr.getSurveyId() + " - " + sr.getSurveyName());
		
	}

	@Override
	public void onExit() {
		Reports.getItems().clear();
	}
	
	private void OpenReportViewer(byte[] reportLeft,byte[] reportRight)
	{
		FXMLLoader loader = new FXMLLoader();
    	Parent root = null;
		loader.setLocation(getClass().getResource("/gui/mainframes/pdfComparer.fxml"));
		try {
			root =  loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		pdfComparer rvCtrl = loader.getController();
		Stage newStage = new Stage();
		//Utilities.GenericUtilties.SetWindowMovable(root, newStage);
		Scene scene = new Scene(root);
		//newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setScene(scene); 	
		rvCtrl.start(reportLeft, reportRight);
		newStage.show();
	}

}
