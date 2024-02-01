package controllers;

import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import Entities.Survey;
import ProtocolHandler.RequestType;
import client.ClientApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The Class SurveyHistogramController - implements UserControl for switching sub windows.
 * Responsible : shows the expert user all the date of chosen survey - Histogram Q&A and option to submit PDF.
 * 
 */
public class SurveyHistogramController implements UserControl {

		/** The checkBox -  image of check symbol */
		@FXML
    	private ImageView checkBox;
		
		/** The PdfTextBox - The Pdf text box. */
		@FXML
	    private TextField PdfTextBox;
	   
	    /** The Image1 - ImageView of Q&A number 1. */
    	@FXML
	    private ImageView Image1;

    	 /** The Image2 - ImageView of Q&A number 2. */
    	@FXML
	    private ImageView Image2;

    	/** The Image2 - ImageView of Q&A number 3. */
    	@FXML
	    private ImageView Image3;

    	/** The Image2 - ImageView of Q&A number 4. */
    	@FXML
	    private ImageView Image4;

    	/** The Image2 - ImageView of Q&A number 5. */
    	@FXML
	    private ImageView Image5;

    	/** The Image2 - ImageView of Q&A number 6. */
    	@FXML
	    private ImageView Image6;

    	 /** The q1Label - Label of question number 1. */
    	@FXML
	    private Label q1Label;

    	 /** The q2Label - Label of question number 2. */
    	@FXML
	    private Label q2Label;

    	 /** The q3Label - Label of question number 3. */
    	@FXML
	    private Label q3Label;

    	 /** The q4Label - Label of question number 4. */
    	@FXML
	    private Label q4Label;

    	 /** The q5Label - Label of question number 5. */
    	@FXML
	    private Label q5Label;

    	 /** The q6Label - Label of question number 6. */
    	@FXML
	    private Label q6Label;
	    
    	/** The active panel container - AnchorPane that contains all the FXML element */
    	@FXML
	    private AnchorPane activePanelContainer;

	    /** The surveyNameLabel - Main label upper page */
    	@FXML
	    private Label surveyNameLabel;

		/** The survey - instance of Survey entity. */
		private Survey survey=new Survey();
		
		/** The imegeTest1 - instance of image(as ByteArrayInputStream) of Q&A number 1. */
		private Image imegeTest1;
		
		/** The imegeTest2 - instance of image(as ByteArrayInputStream) of Q&A number 2. */
		private Image imegeTest2;
		
		/** The imegeTest2 - instance of image(as ByteArrayInputStream) of Q&A number 3. */
		private Image imegeTest3;
		
		/** The imegeTest2 - instance of image(as ByteArrayInputStream) of Q&A number 4. */
		private Image imegeTest4;

		/** The imegeTest2 - instance of image(as ByteArrayInputStream) of Q&A number 5. */
		private Image imegeTest5;

		/** The imegeTest2 - instance of image(as ByteArrayInputStream) of Q&A number 6. */
		private Image imegeTest6;

	/**
	 * Commands that the Controller do the screen will do while uploading window
	 * Load all the images of Q&A histograms ans show them
	 */
	@Override
	public void onEnter() {
		checkBox.setVisible(false);
		ArrayList<byte[]> listOfImages;
		String string =(String)LoginController.windowControl.peekPipe("surveyForExpert");
		survey.setId(Integer.parseInt(string.substring(0,string.indexOf(" "))));
		survey.setContent(string.substring(string.indexOf(" ")+1));
		surveyNameLabel.setText(survey.getContent()+"'s histograms");
		
		ClientApp.ProtocolHandler.Invoke(RequestType.GetSurvey, survey.getId(), null, true);
		survey=(Survey)ClientApp.ProtocolHandler.GetResponse(RequestType.GetSurvey);
		ClientApp.ProtocolHandler.Invoke(RequestType.GetSurveyHistogram, survey, null, true);
		listOfImages=((ArrayList<byte[]>)ClientApp.ProtocolHandler.GetResponse(RequestType.GetSurveyHistogram));
		
		imegeTest1=(new Image (new ByteArrayInputStream(listOfImages.get(0))));
		Image1.setImage(imegeTest1);
		//q1Label.setText(survey.getQuestions()[0]);
		
		imegeTest2=(new Image (new ByteArrayInputStream(listOfImages.get(1))));
		Image2.setImage(imegeTest2);
		//q2Label.setText(survey.getQuestions()[1]);
		
		imegeTest3=(new Image (new ByteArrayInputStream(listOfImages.get(2))));
		Image3.setImage(imegeTest3);
		//q3Label.setText(survey.getQuestions()[2]);
		
		imegeTest4=(new Image (new ByteArrayInputStream(listOfImages.get(3))));
		Image4.setImage(imegeTest4);
		//q4Label.setText(survey.getQuestions()[3]);
		
		imegeTest5=(new Image (new ByteArrayInputStream(listOfImages.get(4))));
		Image5.setImage(imegeTest5);
		//q5Label.setText(survey.getQuestions()[4]);
		
		imegeTest6=(new Image (new ByteArrayInputStream(listOfImages.get(5))));
		Image6.setImage(imegeTest6);
		//q6Label.setText(survey.getQuestions()[5]);
	}
	
	  /**
  	 * Back to previous screen - Expert Home Page 
  	 * @param event - ActionEvent of button pressed
  	 */
  	@FXML
	    void BackBtnPressed(ActionEvent event) {
		  LoginController.windowControl.setUserControl("/gui/usercontrols/ExpertHomePage.fxml");
	    }
	  
	  /**
  	 * Open filer chooser to insert type PDF file and Saves the PDF in Database 
  	 * @param event - ActionEvent of button pressed
  	 */
  	@FXML
	    void InsertPDFBtnpressed(ActionEvent event) {
		  
		  	ArrayList<byte[]> listOfPDFArrayList=new ArrayList<byte[]>();
	    	FileChooser PDFFile = new FileChooser();
	    	PDFFile.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf", "*.PDF"));
	    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        //Show open file dialog
	        File file = PDFFile.showOpenDialog(null);
	        FileInputStream inputStream = null;
	        byte[] data2=null;
	       if (file != null) 
	        {
	        	try 
	        	{
	        		inputStream = new FileInputStream(file);
	        		int nRead;
	          	   	data2 = new byte[(int) file.length()];
	          	   	while ((nRead = inputStream.read(data2, 0, data2.length)) != -1) 
	          	   	{
	          	   		buffer.write(data2, 0, nRead);
	          	   	}
	          	  
				} 
	            catch (Exception e) 
	            {
					e.printStackTrace();
				}
	        	
	        	byte[] data1= createPDF("C:\\Users\\Ido Mialy\\Desktop");	
	        	listOfPDFArrayList.add(data1);
	        	listOfPDFArrayList.add(data2);
	         	checkBox.setVisible(true);
	     		PdfTextBox.setText(file.getPath());
	        	ClientApp.ProtocolHandler.Invoke(RequestType.SavePDF, listOfPDFArrayList, survey, false);
	        }
	    }
	
	/**
	 * Commands that Controller do the screen will do while existing window On exit.
	 */
	@Override
	public void onExit() {}
	
	/**
	 * Creates experts PDf contains Q&A images and expert PDF .
	 * @param the path - String path to save file
	 * @return the byte[] - return byte[] of PDF from path
	 */
	public byte[] createPDF(String path) {
	    try (PDDocument document = new PDDocument()) {
	      PDPage page = new PDPage(PDRectangle.A4);
	      page.setRotation(0);
	 
	      float pageWidth = page.getMediaBox().getWidth();
	      float pageHeight = page.getMediaBox().getHeight();
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
	      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
	      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(logoImage, 5, pageHeight-80,150,80);
	      //contentStream.drawImage(logoImage, 225, 750,150,80); 
	      contentStream.beginText();
	      contentStream.newLineAtOffset(50, 715);  
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 30);
	      contentStream.showText(survey.getContent()+"'s histogram"); 
	      contentStream.endText();
	      
	      contentStream.beginText();
	      contentStream.newLineAtOffset(150, 15);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
	      contentStream.newLine();
	     // contentStream.showText(String.format("To Group VI 2022 ©"));
	      contentStream.endText();
	      
	      PDImageXObject image1 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest1, null));
	      AffineTransform at1 = new AffineTransform(250, 0, 0, 200, 30, 500);
	      at1.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image1, at1);
	      
	      PDImageXObject image2 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest2, null));
	      AffineTransform at2 = new AffineTransform(250, 0, 0, 200, 310, 500);
	      at2.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image2, at2);
	      
	      PDImageXObject image3 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest3, null));
	      AffineTransform at3 = new AffineTransform(250, 0, 0, 200, 30, 270);
	      at1.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image3, at3);
	      
	      PDImageXObject image4 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest4, null));
	      AffineTransform at4 = new AffineTransform(250, 0, 0, 200, 310, 270);
	      at2.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image4, at4);
	     
	      PDImageXObject image5 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest5, null));
	      AffineTransform at5 = new AffineTransform(250, 0, 0, 200, 30, 40);
	      at1.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image5, at5);
	      
	      PDImageXObject image6 = JPEGFactory.createFromImage(document,SwingFXUtils.fromFXImage(imegeTest6, null));
	      AffineTransform at6 = new AffineTransform(250, 0, 0, 200, 310, 40);
	      at2.rotate(Math.toRadians(360));
	      contentStream.drawXObject(image6, at6);
	      
	      contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.close();
	      document.addPage(page);
	 
	      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	      document.save(byteArrayOutputStream);
	      
		   /*
		  // THIS IS FILE SAVING FOR DEBUG 
		  File f = new File(path + "//" + "try1" + ".pdf");
		  FileOutputStream fs = new FileOutputStream(f);
          BufferedOutputStream fileWriter = new BufferedOutputStream(fs);
          fileWriter.write(byteArrayOutputStream.toByteArray());
		  fileWriter.close();
		  fs.close();
		  // FILE SAVING FOR DEBUG END
		  */
	      return byteArrayOutputStream.toByteArray();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		return null;
}

    /**
     * End of Insert PDF session 
     * return to Expert Home Page if there all ready inserted PDF Otherwise the button is not active
   	 * @param event - ActionEvent of button pressed
     */
    @FXML
    void submitPDFBtnpressed(ActionEvent event) {
    	if(!PdfTextBox.getText().equals(""))
    	{
    		checkBox.setVisible(false);
    		PdfTextBox.setText("");
    		LoginController.windowControl.setUserControl("/gui/usercontrols/ExpertHomePage.fxml");
    	}
    	
    }
}
