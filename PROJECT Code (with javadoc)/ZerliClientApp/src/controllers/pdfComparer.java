/*
 * 
 */
package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * The Class pdfComparer compare between 2 different pdf's.
 */
public class pdfComparer {

    /** The pdf 1. */
    @FXML
    private AnchorPane pdf1;

    /** The pdf 2. */
    @FXML
    private AnchorPane pdf2;
    

	/**
	 * Start- load 2 pdf's one beside the other one to compare between them 
	 *
	 * @param pdfA the pdf A
	 * @param pdfB the pdf B
	 */
	public void start(byte[] pdfA, byte[] pdfB)
	{
		FXMLLoader loader = new FXMLLoader();
    	Parent root = null;
		loader.setLocation(getClass().getResource("/gui/mainframes/PdfViewer.fxml"));
		try {
			root =  loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		FXMLLoader loader2 = new FXMLLoader();
		
    	Parent root2 = null;
		loader2.setLocation(getClass().getResource("/gui/mainframes/PdfViewer.fxml"));
		try {
			root2 =  loader2.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		ReportViewerController rvCtrl = loader.getController();
		ReportViewerController rvCtrl2 = loader2.getController();
		rvCtrl.loadReport(pdfA);
		rvCtrl2.loadReport(pdfB);
		pdf1.getChildren().add(root);
		pdf2.getChildren().add(root2);

		//Stage newStage = new Stage();
		//Utilities.GenericUtilties.SetWindowMovable(root, newStage);
		//Scene scene = new Scene(root);
		//newStage.initStyle(StageStyle.UNDECORATED);
		//newStage.setScene(scene); 	
		//newStage.show();
	}
}
