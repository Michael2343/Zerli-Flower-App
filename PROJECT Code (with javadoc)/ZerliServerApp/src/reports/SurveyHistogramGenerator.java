package reports;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.pdfbox.debugger.ui.ImageUtil;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

import Entities.Survey;

// TODO: Auto-generated Javadoc
/**
 * The Class SurveyHistogramGenerator is responsible for the survey histogram generation
 */
public class SurveyHistogramGenerator {
	
	/**
	 * Gets the 6 images.
	 *
	 * @param listOfSurveyAnswers the list of survey answers
	 * @param survey the survey
	 * @return the 6 images
	 */
	public ArrayList<byte[]> get6images(ArrayList<int[]> listOfSurveyAnswers, Survey survey)
	{
		int i=0;
		ArrayList<byte[]> listOfimeges=new ArrayList<byte[]> ();
		for (int[] is : listOfSurveyAnswers) {
			listOfimeges.add(createBarChartIncome(is,survey.getQuestions()[i]));
			i++;
		} 
		return listOfimeges;
	}
	
	/**
	 * Creates the bar chart income.
	 *
	 * @param surveyAnswersArray the survey answers array
	 * @param histogramTitle the histogram title
	 * @return the byte[]
	 */
	private  byte[] createBarChartIncome(int[] surveyAnswersArray, String histogramTitle)
	{
		byte[] imageInBytes=null;
	    CategoryChart chart = new CategoryChartBuilder().width((int)(300)).height((int)(300*0.7f)).title(histogramTitle).xAxisTitle("Rating").yAxisTitle("Number of Answers").build();
	    
	    // Customize Chart
	    chart.getStyler().setLegendVisible(false);

	    // Series
	    chart.addSeries("Answers", Arrays.asList(new String[] {"1","2","3","4","5","6","7","8","9","10"}), Arrays.asList(new Integer[] {surveyAnswersArray[0],surveyAnswersArray[1],surveyAnswersArray[2],surveyAnswersArray[3],surveyAnswersArray[4],surveyAnswersArray[5],surveyAnswersArray[6],surveyAnswersArray[7],surveyAnswersArray[8],surveyAnswersArray[9]}));

	    BufferedImage bufferedImage = ImageUtil.getRotatedImage(BitmapEncoder.getBufferedImage(chart),0) ;
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	    	ImageIO.write(bufferedImage, "jpg", baos);
	      baos.flush();
	      imageInBytes = baos.toByteArray();
	      
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	    return imageInBytes;
	}

}
