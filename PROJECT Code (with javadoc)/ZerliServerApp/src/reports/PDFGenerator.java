package reports;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.apache.pdfbox.debugger.ui.ImageUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.knowm.xchart.*;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

import Entities.ItemType;

// TODO: Auto-generated Javadoc
/**
 * The Class PDFGenerator holds all the method used to generate
 * a pdf file with tables/graphs/histograms etc.
 */
public class PDFGenerator {
	  
	/** The standard width of a PDF page. */
	final int WIDTH = 595;
	
	/** The standard height of a PDF page. */
	final int HEIGHT = 842;
	
	/** An array with definitions of the months. */
	String[] months = new String[] {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	/**
	 * Creates a bar chart for an income report.
	 *
	 * @param width - chart's width
	 * @param height - chart's height
	 * @param quarter - the quarter [1,2,3,4]
	 * @return the chart as image
	 */
	private BufferedImage createBarChartIncome(int width,int height,int quarter)
	{
		
		quarter *= 3;
	    CategoryChart chart = new CategoryChartBuilder().width((int)(height)).height((int)(width * 0.7f)).title("Quarterly CEO Income Report").xAxisTitle("Month").yAxisTitle("Income").build();
	    
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
	    //chart.getStyler().setHasAnnotations(true);
	 
	    // Series
	    chart.addSeries(months[quarter], Arrays.asList(new String[] {" "}), Arrays.asList(new Integer[] {15}));
	    chart.addSeries(months[quarter+1], Arrays.asList(new String[] { " "}), Arrays.asList(new Integer[] {5}));
	    chart.addSeries(months[quarter+2], Arrays.asList(new String[] { " "}), Arrays.asList(new Integer[] {10}));
		
	    return ImageUtil.getRotatedImage(BitmapEncoder.getBufferedImage(chart), 90) ;
	}
	
	/**
	 * Creates an XY Histogram.
	 *
	 * @param width - chart's width
	 * @param height - chart's height
	 * @param X - Collection of X values
	 * @param Y - Collection of Y values
	 * @param Name - the name of the histogram
	 * @param chartname - the name of the chart
	 * @return The chart as image
	 */
	private BufferedImage createHistogram(int width,int height,ArrayList<Integer> X,ArrayList<Integer> Y,String name,String chartname)
	{
	    XYChart chart = new XYChartBuilder().width((int)(height)).height((int)(width * 0.7f)).title(name).xAxisTitle("Month").yAxisTitle("Number of complaints").build();
	    
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	    //chart.getStyler().setHasAnnotations(true);
	 
	    // Series
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
	    //chart.getStyler().setXAxisTickMarkSpacingHint(1)
	    chart.addSeries(chartname,null , Y);
	    return ImageUtil.getRotatedImage(BitmapEncoder.getBufferedImage(chart), 90) ;
	}
	
	/**
	 * Creates the bar chart of the complaints result
	 *
	 * @param width - width of the chart
	 * @param height - height of the chart
	 * @param X - collection of x axis values
	 * @param Y - collection of y axis values
	 * @return the chart as image
	 */
	private BufferedImage createBarChartComplaints(int width,int height,ArrayList<Integer> X,ArrayList<Integer> Y)
	{
	    XYChart chart = new XYChartBuilder().width((int)(height)).height((int)(width * 0.7f)).title("Quarterly Customer Service Report").xAxisTitle("Month").yAxisTitle("Number of complaints").build();
	    
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	    //chart.getStyler().setHasAnnotations(true);
	 
	    // Series
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
	    //chart.getStyler().setXAxisTickMarkSpacingHint(1)
	    chart.addSeries("Monthly Complaints",null , Y);
	    return ImageUtil.getRotatedImage(BitmapEncoder.getBufferedImage(chart), 90) ;
	}
	
	/**
	 * Creates the bar chart for the orders
	 *
	 * @param width - width of the chart
	 * @param height - height of the chart
	 * @param histo - hashmap item_type->integer as the histogram function
	 * @return the bar chart as image
	 */
	private BufferedImage createBarChartOrder(int width,int height,HashMap<ItemType,Integer> histo)
	{
	    CategoryChart chart = new CategoryChartBuilder().width((int)(height)).height((int)(width * 0.7f)).title("Order Report").xAxisTitle("Item Type").yAxisTitle("Number of orders").build();
	    
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
	    //chart.getStyler().setHasAnnotations(true);
	 
	    // Series
	    for(ItemType t : histo.keySet())
	    {
	    	chart.addSeries(Utilities.GenericUtilties.ItemTypeToString(t), Arrays.asList(new String[] {Utilities.GenericUtilties.ItemTypeToString(t)}), Arrays.asList(new Integer[] {histo.get(t)}));
	    }
	    
	    //chart.addSeries("Custom", Arrays.asList(new String[] { "Custom Defined Items"}), Arrays.asList(new Integer[] {5}));
	    //chart.addSeries("Self Created Products", Arrays.asList(new String[] { "Self Created Products"}), Arrays.asList(new Integer[] {10}));
		
	    return ImageUtil.getRotatedImage(BitmapEncoder.getBufferedImage(chart), 90) ;
	}
	/*
	private BufferedImage createHistogram(int width,int height)
	{
	    // Create Chart
	    CategoryChart chart = new CategoryChartBuilder().width(width).height(height).title("Score Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	    chart.getStyler().setAvailableSpaceFill(.96);
	    chart.getStyler().setOverlapped(true);
	    // Series
	    ArrayList<Integer> a = new ArrayList<Integer>();
	    for(int i = 0;i<20;i++)
	    	a.add(i);
	    Histogram histogram1 = new Histogram(a, 20, -20, 20);
	    //Histogram histogram2 = new Histogram(getGaussianData(5000), 20, -20, 20);
	    chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
	    //chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());
	    return BitmapEncoder.getBufferedImage(chart);
	}
	*/
/*
	@SuppressWarnings("unused")
	private BufferedImage createChart(int width, int height) {
		  XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(width).height(height)
		      .theme(ChartTheme.Matlab).build();
		  XYSeries series = chart.addSeries("Random", null, getRandomNumbers(200));
		  series.setMarker(SeriesMarkers.NONE);
		  return BitmapEncoder.getBufferedImage(chart);
		}
		 
		/**
		 * Gets the random numbers.
		 *
		 * @param numPoints the num points
		 * @return the random numbers
		 */
	/*
		private double[] getRandomNumbers(int numPoints) {
		  double[] y = new double[numPoints];
		  for (int i = 0; i < y.length; i++) {
		    y[i] = ThreadLocalRandom.current().nextDouble(0, 1000);
		  }
		  return y;
		}
		
	*/
	/**
	 * Creates the order report histogram.
	 *
	 * @param branch the branch of the report
	 * @param date the date of the report
	 * @param histo the histogram function (item to price)
	 * @return the byte[] stream of a pdf file
	 */
	public byte[] createOrderReportHistogram(String branch,String date,HashMap<ItemType,Integer> histo) {
		    try (PDDocument document = new PDDocument()) {
		      PDPage page = new PDPage(PDRectangle.A4);
		      page.setRotation(0);
		 
		      float pageWidth = page.getMediaBox().getWidth();
		      float pageHeight = page.getMediaBox().getHeight();
		      PDPageContentStream contentStream = new PDPageContentStream(document, page);
		      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
		      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
		      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
		      contentStream.drawImage(logoImage, 5, HEIGHT-80,150,80); 
		      contentStream.beginText();
		      contentStream.newLineAtOffset(WIDTH/2 - 100, 760);  
	    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 30);
		      contentStream.showText("Order Report");
		      contentStream.endText();
		      
		      contentStream.beginText();
		      contentStream.newLineAtOffset(80, 715);  
		      contentStream.setLeading(20);
	    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
		      contentStream.showText(String.format("Created for branch %s" ,branch));
		      contentStream.newLine();
		      contentStream.showText(String.format("Date: %s",date));
		      contentStream.endText();
		      
		      contentStream.beginText();
		      contentStream.newLineAtOffset(150, 15);  
		      contentStream.setLeading(20);
	    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
		      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
		      contentStream.newLine();
		      contentStream.endText();
		      
		      PDImageXObject chartImage = JPEGFactory.createFromImage(document,
		      createBarChartOrder((int) pageHeight, (int) pageWidth, histo));
		      contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
		      contentStream.drawImage(chartImage, 80, 0); 
		      contentStream.close();
		      document.addPage(page);
		 
		      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		      document.save(byteArrayOutputStream);
		      return byteArrayOutputStream.toByteArray();
		    }
		    catch(IOException e)
		    {
		    	e.printStackTrace();
		    }
			return null;
	}
	
	/**
	 * Creates the income report table.
	 *
	 * @param branch - the branch of the report
	 * @param date the date of the report
	 * @param data the periodic income sampling (day to day income)
	 * @param dates the dates the correspond to the periodic income
	 * @return the byte[] stream as pdf file
	 */
	public byte[] createIncomeReportTable(String branch, String date, ArrayList<Integer[]> data,ArrayList<LocalDate> dates) {
	    try (PDDocument document = new PDDocument()) {
	      PDPage page = new PDPage(PDRectangle.A4);
	      page.setRotation(0);
	 
	      float pageWidth = page.getMediaBox().getWidth();
	      float pageHeight = page.getMediaBox().getHeight();
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
	      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
	      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(logoImage, 5, HEIGHT-80,150,80); 
	      contentStream.beginText();
	      contentStream.newLineAtOffset(WIDTH/2 - 100, 760);  
	      try
	      {
	    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 30);
	      }
	      catch(Throwable e)
	      {
	    	  //System.out.println("caught patch");
	      }
	      contentStream.showText("Income Report");
	      contentStream.endText();
	      
	      contentStream.beginText();
	      contentStream.newLineAtOffset(80, 715);  
	      contentStream.setLeading(20);
	      contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")),16);
	      contentStream.showText(String.format("Created for branch %s" ,branch));
	      contentStream.newLine();
	      contentStream.showText(String.format("Date: %s",date));
	      contentStream.endText();
	      
//	      PDImageXObject chartImage = JPEGFactory.createFromImage(document,
//	    		  createBarChart((int) pageHeight, (int) pageWidth));
//	      contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
//	      contentStream.drawImage(chartImage, 80, 0); 
	      drawTable(document,contentStream,4,data.size()+2,50,675,120,20,
	    		  new String[] {"Date","Completed","Cancelled","Total"},data,dates);
	      contentStream.beginText();
	      contentStream.newLineAtOffset(150, 15);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
	      contentStream.newLine();
	      contentStream.endText();
	      contentStream.close();
	      document.addPage(page);
	      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	      //document.setVersion(1.3f);
	      document.save(byteArrayOutputStream);
	      return byteArrayOutputStream.toByteArray();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    	//System.out.println("nana");
	    }
		return null;
}
	
	/**
	 * Draw table.
	 *
	 * @param document the pdf document
	 * @param contentStream the content stream
	 * @param cols the number of cols in the table
	 * @param rows the rows in the table
	 * @param x the x position on the pdf document
	 * @param y the y position on the pdf document
	 * @param cellwidth the width of each table cell
	 * @param cellheight the height of each table cell
	 * @param header the first row data of each cell
	 * @param data the data to put inside the table (divided by rows cols)
	 * @param dates the dates that correspond to each data element
	 */
	private void drawTable(PDDocument document,PDPageContentStream contentStream,int cols,int rows,int x,
			int y,int cellwidth,int cellheight,String[] header,ArrayList<Integer[]> data,ArrayList<LocalDate> dates)
	{
		int totalCompleted = 0,totalCancelled = 0,totalIncome = 0;
		int tempx;
		int tempy = y;
		for(int i = 1;i<=rows;i++)
		{
			tempx = x;
			for(int j = 1;j<=cols;j++)
			{
				try {
					contentStream.addRect(tempx,tempy,cellwidth,-cellheight);
					contentStream.beginText();
					contentStream.newLineAtOffset(tempx+10, tempy-cellheight+10);
					contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 10);
					if(i == 1)
						contentStream.showText(header[j-1]);
					else if(i == rows)
					{
						if(j == 1)
							contentStream.showText("Total");
						if(j == 2)
							contentStream.showText(totalCompleted + "");
						else if(j == 3)
							contentStream.showText(totalCancelled + "");
						else if(j == 4)
							contentStream.showText(totalIncome + "");
					}
					else
					{
						if(j == 1)
						{
							totalCompleted += (data.get(i-2))[0];
							totalCancelled += (data.get(i-2))[1];
							totalIncome += (data.get(i-2))[2];
							contentStream.showText(dates.get(i-2).toString());
						}
						else
							contentStream.showText("" + (data.get(i-2))[j-2]);
					}
					
					contentStream.endText();
					tempx += cellwidth;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tempy -= cellheight;
		}
		try {
			contentStream.stroke();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the CEO report for branch.
	 *
	 * @param branch the branch which the report is created upon
	 * @param date the date of the report
	 * @param X the x
	 * @param Y the y
	 * @return the byte[]
	 */
	public byte[] createCEOReportForBranch(String branch,String date,ArrayList<Integer> X,ArrayList<Integer> Y) {
	    try (PDDocument document = new PDDocument()) {
	      PDPage page = new PDPage(PDRectangle.A4);
	      page.setRotation(0);
	 
	      float pageWidth = page.getMediaBox().getWidth();
	      float pageHeight = page.getMediaBox().getHeight();
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
	      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
	      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(logoImage, 5, HEIGHT-80,150,80); 
	      contentStream.beginText();
	      contentStream.newLineAtOffset(WIDTH/2 - 140, 760);  
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 30);
	      contentStream.showText("CEO Income Report - " + branch);
	      contentStream.endText();
	      
	      contentStream.beginText();
	      contentStream.newLineAtOffset(80, 715);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText(String.format("Presented Branch: %s " ,branch));
	      contentStream.newLine();
	      contentStream.showText(String.format("Date: %s",date));
	      contentStream.endText();

	      contentStream.beginText();
	      contentStream.newLineAtOffset(150, 15);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
	      contentStream.newLine();
	      contentStream.endText();
	      
	      PDImageXObject chartImage = JPEGFactory.createFromImage(document,
	      createHistogram((int) pageHeight, (int) pageWidth, X,Y,"Income" + branch,"Income Over Quarter"));
	      contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(chartImage, 80, 0); 
	      contentStream.close();
	      document.addPage(page);
	 
	      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	      document.save(byteArrayOutputStream);
	      return byteArrayOutputStream.toByteArray();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		return null;
}
	
	/**
	 * Creates the complaint report histogram.
	 *
	 * @param branch the branch
	 * @param date the date
	 * @param X - collection of x axis values
	 * @param Y - collection of y axis values
	 * @return the byte[] stream as pdf file
	 */
	public byte[] createComplaintsReportHistogram(String branch,String date,ArrayList<Integer> X,ArrayList<Integer> Y) {
	    try (PDDocument document = new PDDocument()) {
	      PDPage page = new PDPage(PDRectangle.A4);
	      page.setRotation(0);
	 
	      float pageWidth = page.getMediaBox().getWidth();
	      float pageHeight = page.getMediaBox().getHeight();
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
	      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
	      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(logoImage, 5, HEIGHT-80,150,80); 
	      contentStream.beginText();
	      contentStream.newLineAtOffset(WIDTH/2 - 100, 760);  
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 30);
	      contentStream.showText("Service Report");
	      contentStream.endText();
	      
	     
	      contentStream.beginText();
	      contentStream.newLineAtOffset(80, 715);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText(String.format("Created for branch %s" ,branch));
	      contentStream.newLine();
	      contentStream.showText(String.format("Date: %s",date));
	      contentStream.endText();
	      
	      contentStream.beginText();
	      contentStream.newLineAtOffset(150, 15);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
	      contentStream.newLine();
	      contentStream.endText();
	      
	      PDImageXObject chartImage = JPEGFactory.createFromImage(document,
	      createBarChartComplaints((int) pageHeight, (int) pageWidth, X,Y));
	      contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(chartImage, 80, 0); 
	      contentStream.close();
	      document.addPage(page);
	 
	      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	      document.save(byteArrayOutputStream);
	      return byteArrayOutputStream.toByteArray();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		return null;
}
	
/*
	public byte[] createIncomeReportTableCEO(String path,String name,String branch,String startDate,String endDate) {
	    try (PDDocument document = new PDDocument()) {
	      PDPage page = new PDPage(PDRectangle.A4);
	      page.setRotation(0);
	 
	      float pageWidth = page.getMediaBox().getWidth();
	      float pageHeight = page.getMediaBox().getHeight();
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      InputStream stream = getClass().getResourceAsStream("/png/ZerliLogo.png");
	      PDImageXObject logoImage = JPEGFactory.createFromImage(document, ImageIO.read(stream));
	      //contentStream.transform(new Matrix(0, 1, -1, 0, pageWidth, 0));
	      contentStream.drawImage(logoImage, 5, HEIGHT-80,150,80); 
	      contentStream.beginText();
	      contentStream.newLineAtOffset(WIDTH/2 - 100, 760);  
	      contentStream.setFont(PDType1Font.HELVETICA, 30);
	      contentStream.showText("Income Report");
	      contentStream.endText();
	      
	      contentStream.beginText();
	      contentStream.newLineAtOffset(80, 715);  
	      contentStream.setLeading(20);
	      contentStream.setFont(PDType1Font.HELVETICA,16);
	      contentStream.showText(String.format("Created for branch %s" ,branch));
	      contentStream.newLine();
	      contentStream.showText(String.format("Shown Month: %s ~ %s",startDate,endDate));
	      contentStream.endText();
	      contentStream.beginText();
	      contentStream.newLineAtOffset(15, 150);  
	      contentStream.setLeading(20);
    	  contentStream.setFont(PDType0Font.load(document, new File("C:\\Windows\\Fonts\\arial.ttf")), 16);
	      contentStream.showText("All Rights Reserved To Group VI 2022 ©");
	      contentStream.newLine();
	      contentStream.endText();
	      contentStream.close();
	      
	      document.addPage(page);
	 
	      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	      document.save(byteArrayOutputStream);
	      File f = new File(path + "//" + name + ".pdf");
	      FileOutputStream fs = new FileOutputStream(f);
	      BufferedOutputStream fileWriter = new BufferedOutputStream(fs);
	      fileWriter.write(byteArrayOutputStream.toByteArray());
	      fileWriter.close();
	      fs.close();
	      
	      return byteArrayOutputStream.toByteArray();
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		return null;
}
*/
}
