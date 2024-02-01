package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Class ReportSelectorController - implements UserControl for switching sub windows.
 * Responsible : Select report type and dates to show reports.
 */
public class ReportSelectorController implements UserControl {

	/** The report_income - Const string "Branch Income Report" for comboBox*/
	private final String report_income = "Branch Income Report";
	
	/** The report_orders - Const string "Branch Order Report" for comboBox*/
	private final String report_orders = "Branch Order Report";
	
	/** The report_service - Const string "Branch Complaints Report" for comboBox*/	
	private final String report_service = "Branch Complaints Report";
	   
	/** The active panel container - AnchorPane that contains all the FXML element */
	@FXML
    private AnchorPane activePanelContainer;

    /** The eerrorLabel- Label for error strings. */
	@FXML
    private Label errorLabel;

    /** The isCompare - CheckBox for options of showing 2 reports at once . */
	@FXML
    private CheckBox isCompare;

    /** The isMonthly - RadioButton for Monthly report options. */
	@FXML
    private RadioButton isMonthly;

    /** The isQuarterly - RadioButton for Quarterly report options. */	
	@FXML
    private RadioButton isQuarterly;

    /** The panelMonth - AnchorPane for month selection panel. */
	@FXML
    private AnchorPane panelMonth;

	 /** The panelQuarter - AnchorPane for Quarter selection panel. */	
	@FXML
    private AnchorPane panelQuarter;

    /** The periodGroup - ToggleGroup of panelMonth and panelQuarter. */
	@FXML
    private ToggleGroup periodGroup;
    
    /** The reportMonth - ComboBox of months. */
	@FXML
    private ComboBox<String> reportMonth;
    
	  /** The reportMonth - ComboBox of years. */	
	@FXML
    private ComboBox<String> reportYear;
    
    /** The reportMonth - ComboBox of compared months. */	
	@FXML
    private ComboBox<String> comparedMonth;
    
	/** The reportMonth - ComboBox of compared years. */	
	@FXML
    private ComboBox<String> comparedYear;

	/** The reportMonth - ComboBox of compared Quarter. */	
	@FXML
    private ComboBox<String> comparedQuarter;

	/** The reportMonth - ComboBox of compared Quarter year. */	
	@FXML
    private ComboBox<String> comparedQuarterY;    

	/** The reportMonth - ComboBox of Quarter month. */	
	@FXML
    private ComboBox<String> reportQuarter;

	/** The reportMonth - ComboBox of Quarter year. */	
	@FXML
    private ComboBox<String> reportQuarterY;

	/** The reportType - ComboBox of report Types. */	
	@FXML
    private ComboBox<String> reportType;

    /** The viewRepBtn - Button for open chosen report. */
	@FXML
    private Button viewRepBtn;

    /**
     * View report button click - get reports from chosen input
     * @param event - button pressed 
     */
    @FXML
    void viewReport_click(ActionEvent event) {
    	if(!isInputValid()) return;
    	var comparing = isCompare.isSelected();
    	byte[] reportMain,reportCompared = null;
    	LocalDate d;
    	if(!periodGroup.getSelectedToggle().equals(isMonthly))
    	{
    		d = LocalDate.of(Integer.parseInt(reportQuarterY.getValue()), Integer.parseInt(reportQuarter.getValue()), 1);
    	}
    	else
    	{
    		d = LocalDate.of(Integer.parseInt(reportYear.getValue().toString()),Month.valueOf(reportMonth.getValue()).getValue(),1);
    	}
    	
		ClientApp.ProtocolHandler.Invoke(RequestType.GetReport,null,
				new Object[] {
							  Utilities.GenericUtilties.StringToReportType(reportType.getValue()),
							  periodGroup.getSelectedToggle().equals(isMonthly),
							  d
						     },
				true);
		reportMain = (byte[])ClientApp.ProtocolHandler.GetResponse(RequestType.GetReport);
		if(reportMain == null)
		{
			Alert confirmAlert = new Alert(AlertType.NONE);
			confirmAlert.setTitle("No such report");
			confirmAlert.setContentText("The requested report does not exist");
			ButtonType yes = new ButtonType("OK", ButtonData.OK_DONE);
			confirmAlert.getDialogPane().getButtonTypes().add(yes);
			confirmAlert.showAndWait();
			return;
		}
    	if(comparing)
    	{
        	if(!periodGroup.getSelectedToggle().equals(isMonthly))
        	{
        		d = LocalDate.of(Integer.parseInt(comparedQuarterY.getValue()), Integer.parseInt(comparedQuarter.getValue()), 1);
        	}
        	else
        	{
        		d = LocalDate.of(Integer.parseInt(comparedYear.getValue().toString()),Month.valueOf(comparedMonth.getValue()).getValue(),1);
        	}
        	
    		ClientApp.ProtocolHandler.Invoke(RequestType.GetReport,null,
    				new Object[] {
    							  Utilities.GenericUtilties.StringToReportType(reportType.getValue()),
    							  periodGroup.getSelectedToggle().equals(isMonthly),
    							  d
    						     },
    				true);
    		reportCompared = (byte[])ClientApp.ProtocolHandler.GetResponse(RequestType.GetReport);
    		if(reportCompared == null)
    		{
    			Alert confirmAlert = new Alert(AlertType.NONE);
    			confirmAlert.setTitle("No such report");
    			confirmAlert.setContentText("The requested report does not exist");
    			ButtonType yes = new ButtonType("OK", ButtonData.OK_DONE);
    			confirmAlert.getDialogPane().getButtonTypes().add(yes);
    			confirmAlert.showAndWait();
    			return;
    		}
    	}
    	
    	if(comparing)
    		OpenReportViewer(reportMain, reportCompared);
    	else
    		OpenReportViewer(reportMain);
    }
    @FXML
    void helpBtnPressed(ActionEvent event) {
    	Utilities.GenericUtilties.ShowHelp("","help_report_selection_mng.png");
    }
    /**
     * Checks if is input valid - report type, dates, comparing reports.
     * @return true, if is input valid
     */
    private boolean isInputValid()
    {
    	boolean valid = true;
    	StringBuilder error = new StringBuilder("");
    	if(reportType.getValue() == null)
    	{
    		valid = false;
    		error.append("please select a report\n");
    	}
    	if(periodGroup.getSelectedToggle() == null)
    	{
    		valid = false;
    		error.append("Please select the report period\n");
    	}
    	else if(periodGroup.getSelectedToggle().equals(isMonthly))
    	{
	    	if(reportMonth.getValue() == null || reportYear.getValue() == null)
	    	{
	    		valid = false;
	    		error.append("Please select a date for the report\n");
	    	}
	    	if(isCompare.isSelected())
	    	{
	    		if(comparedMonth.getValue() == null || comparedYear.getValue() == null)
	    		{
	        		error.append("Please select a date for the compared report\n");
	        		valid = false;
	    		}
	    	}
    	}
    	else //Quarterly
    	{
	    	if(reportQuarter.getValue() == null || reportQuarterY.getValue() == null)
	    	{
	    		valid = false;
	    		error.append("Please select a quarter for the report\n");
	    	}
	    	if(isCompare.isSelected())
	    	{
	    		if(comparedQuarter.getValue() == null || comparedQuarterY.getValue() == null)
	    		{
	        		error.append("Please select a quarter for the compared report\n");
	        		valid = false;
	    		}
	    	}
    	}
    	errorLabel.setText(error.toString());
    	return valid;
    }
    
    /**
	 * Commands that the Controller do the screen will do while uploading window
	 * Load all comboBoxes informations and GUI defaults  
	 */
	@Override
	public void onEnter() {
		
		reportType.getItems().add(report_income);
		reportType.getItems().add(report_orders);
		reportType.getItems().add(report_service);
		
		for(int i = 1;i<=4;i++)
		{
			reportQuarter.getItems().add(i + "");
			comparedQuarter.getItems().add(i + "");
		}
		
		for(Month m : Month.values())
		{
			reportMonth.getItems().add(m.toString());
			comparedMonth.getItems().add(m.toString());
		}
		
		for(int y = 2015;y<LocalDate.now().getYear()+1;y++)
		{
			reportYear.getItems().add(y + "");
			comparedYear.getItems().add(y + "");
			reportQuarterY.getItems().add(y + "");
			comparedQuarterY.getItems().add(y + "");		
		}
		
		isMonthly.selectedProperty().addListener(new ChangeListener<>() {
		
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(oldValue != newValue)
				{
					panelMonth.setDisable(!newValue);
					panelQuarter.setDisable(newValue);
				}
			}
		});
		isCompare.selectedProperty().addListener(new ChangeListener<>() {
		
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(oldValue != newValue)
				{
					comparedMonth.setDisable(!newValue);
					comparedYear.setDisable(!newValue);
					comparedQuarter.setDisable(!newValue);
					comparedQuarterY.setDisable(!newValue);
				}
			}
		});
		comparedMonth.setDisable(true);
		comparedYear.setDisable(true);
		comparedQuarter.setDisable(true);
		comparedQuarterY.setDisable(true);
		isMonthly.setSelected(true);
	}

	/**
	 * Commands that Controller do the screen will do while existing window On exit.
	 * clear all GUI selections, comboBoxes, reset information of FXML window
	 */
	@Override
	public void onExit() {
		reportType.getItems().clear();
		reportQuarter.getItems().clear();
		reportQuarterY.getItems().clear();
		comparedQuarter.getItems().clear();
		comparedQuarterY.getItems().clear();
		reportMonth.getItems().clear();
		reportYear.getItems().clear();
		comparedMonth.getItems().clear();
		comparedYear.getItems().clear();
	}
	
	/**
	 * Open report viewer of chosen report information of single report
	 * @param report - single report as byte[]
	 */
	private void OpenReportViewer(byte[] report)
	{
		FXMLLoader loader = new FXMLLoader();
    	Parent root = null;
		loader.setLocation(getClass().getResource("/gui/mainframes/PdfViewer.fxml"));
		try {
			root =  loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		ReportViewerController rvCtrl = loader.getController();
		Stage newStage = new Stage();
		//Utilities.GenericUtilties.SetWindowMovable(root, newStage);
		Scene scene = new Scene(root);
		//newStage.initStyle(StageStyle.UNDECORATED);
		newStage.setScene(scene); 	
		rvCtrl.loadReport(report);
		newStage.show();
	}
	
	/**
     * Open report viewer of chosen reports information of compare two reports
	 * @param reportLeft - first report as byte[]
	 * @param reportRight - second report as byte[]
	 */
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
