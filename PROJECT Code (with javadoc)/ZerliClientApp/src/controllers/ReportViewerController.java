package controllers;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * The Class ReportViewerController - PDF Viewer of Zerli app. 
 * Responsible : shows The PDF in different window with many options (Zoom,export PDF)
 */
public class ReportViewerController {

	/** The pagination - Pagination the PDF.*/
	@FXML private Pagination pagination ;
	
	/** The currentZoomLabel - Label that shows the current zoom of PDf. */
	@FXML private Label currentZoomLabel ;
    
    /** The export - Button that export PDF to local PC. */
    @FXML
    private Button export;
    
	/** The fileChooser - FileChooser that export PDF to local PC. */
	private FileChooser fileChooser ;
	
	/** The currentFile - ObjectProperty that binded to the PDF file. */
	private ObjectProperty<PDFFile> currentFile ;
	
	/** The currentImage - ObjectProperty that binded to the image. */
	private ObjectProperty<ImageView> currentImage ;
	
	/** The scroller - ScrollPane for PDF file  */
	@FXML  private ScrollPane scroller ;
	
	/** The zoom - DoubleProperty for zooming the PDF image. */
	private DoubleProperty zoom ;
	
	/** The currentPageDimensions - PageDimensions saves the current page dimensions . */
	private PageDimensions currentPageDimensions ;
	
	/** The imageLoadService - Thread for parallel working. */
	private ExecutorService imageLoadService ;
	
	/** The ZOOM_DELTA - Const zoom = 1.05. */
	private static final double ZOOM_DELTA = 1.05 ;
	
    /** The bytefile - byte[] for image for loading PDF. */
    byte[] bytefile = null;
	
	
	/**
	 * Add PDF to window - set default zoom and changing zoom listener.
	 */
	public void initialize() {
		
		createAndConfigureImageLoadService();
		createAndConfigureFileChooser();
		
		currentFile = new SimpleObjectProperty<>();
		//updateWindowTitleWhenFileChanges();
		
		currentImage = new SimpleObjectProperty<>();
		scroller.contentProperty().bind(currentImage);
		
		zoom = new SimpleDoubleProperty(1);
		// To implement zooming, we just get a new image from the PDFFile each time.
		// This seems to perform well in some basic tests but may need to be improved
		// E.g. load a larger image and scale in the ImageView, loading a new image only
		// when required.
		zoom.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateImage(pagination.getCurrentPageIndex());
			}
		});
		
		currentZoomLabel.textProperty().bind(Bindings.format("%.0f %%", zoom.multiply(100)));
		
		bindPaginationToCurrentFile();
		createPaginationPageFactory();
	}

	/**
	 * Creates the and configure image load service.
	 */
	private void createAndConfigureImageLoadService() {
		imageLoadService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});
	}

	/**
	 * Creates the and configure file chooser.
	 */
	private void createAndConfigureFileChooser() {
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf", "*.PDF"));
	}
	
	/**
	 * Bind pagination to current file.
	 */
	private void bindPaginationToCurrentFile() {
		currentFile.addListener(new ChangeListener<PDFFile>() {
			@Override
			public void changed(ObservableValue<? extends PDFFile> observable, PDFFile oldFile, PDFFile newFile) {
				if (newFile != null) {
					pagination.setCurrentPageIndex(0);
				} 
			}
		});
		pagination.pageCountProperty().bind(new IntegerBinding() {
			{
				super.bind(currentFile);
			}
			@Override
			protected int computeValue() {
				return currentFile.get()==null ? 0 : currentFile.get().getNumPages() ;
			}
		});
		pagination.disableProperty().bind(Bindings.isNull(currentFile));
	}
	
	/**
	 * Creates the pagination page factory.
	 */
	private void createPaginationPageFactory() {
		pagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageNumber) {
				if (currentFile.get() == null) {
					return null ;
				} else {
					if (pageNumber >= currentFile.get().getNumPages() || pageNumber < 0) {
						return null ;
					} else {
						updateImage(pageNumber);
						return scroller ;
					}
				}
			}
		});
	}
    
    /**
     * Export the PDF current view.
  	 * @param event - export button pressed
     */
    @FXML
    void exportAction(ActionEvent event) {
    	  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Save as pdf");
          fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF", "*.pdf"));
          File f = fileChooser.showSaveDialog((Stage)((Node) event.getSource()).getScene().getWindow());
          if(f != null)
          {
        	  try
        	  {
    		      FileOutputStream fs = new FileOutputStream(f);
    		      BufferedOutputStream fileWriter = new BufferedOutputStream(fs);
    		      fileWriter.write(bytefile);
    		      fileWriter.close();
    		      fs.close();
        	  }
        	  catch(IOException e)
        	  {
        		  
        	  }
        	  
          }
    }
   
	/**
	 * Load PDf report.
	 * @param byte[] file
	 */
	public void loadReport(byte[] file) {
		//final File file = fileChooser.showOpenDialog(pagination.getScene().getWindow());
		if (file != null) {
			final Task<PDFFile> loadFileTask = new Task<PDFFile>() {
				@Override
				protected PDFFile call() throws Exception {
						ByteBuffer buffer = ByteBuffer.wrap(file);
						bytefile = file;
						return new PDFFile(buffer);
					}
			};
			loadFileTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					//pagination.getScene().getRoot().setDisable(false);
					final PDFFile pdfFile = loadFileTask.getValue();
					currentFile.set(pdfFile);
					updateImage(0);
				}
			});
			loadFileTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					//pagination.getScene().getRoot().setDisable(false);
					showErrorMessage("Could not load report", loadFileTask.getException());
				}
			});
			//pagination.getScene().getRoot().setDisable(true);
			imageLoadService.submit(loadFileTask);
		}
	}
	// ************** Event Handlers ****************
	
	/**
	 * Load PDF file.
	 */
	@FXML private void loadFile() {
		final File file = fileChooser.showOpenDialog(pagination.getScene().getWindow());
		if (file != null) {
			final Task<PDFFile> loadFileTask = new Task<PDFFile>() {
				@Override
				protected PDFFile call() throws Exception {
					try ( 
							RandomAccessFile raf = new RandomAccessFile(file, "r");
							FileChannel channel = raf.getChannel() 
						) {
						ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
						return new PDFFile(buffer);
					}
				}
			};
			loadFileTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					//pagination.getScene().getRoot().setDisable(false);
					final PDFFile pdfFile = loadFileTask.getValue();
					currentFile.set(pdfFile);
				}
			});
			loadFileTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					//pagination.getScene().getRoot().setDisable(false);
					showErrorMessage("Could not load file "+file.getName(), loadFileTask.getException());
				}
			});
			//pagination.getScene().getRoot().setDisable(true);
			imageLoadService.submit(loadFileTask);
		}
	}
	
	/**
	 * Zoom in the PDF.
	 */
	@FXML private void zoomIn() {
		zoom.set(zoom.get()*ZOOM_DELTA);
	}
	
	/**
	 * Zoom out the PDF.
	 */
	@FXML private void zoomOut() {
		zoom.set(zoom.get()/ZOOM_DELTA);
	}
	
	/**
	 * Zoom fit the PDF.
	 */
	@FXML private void zoomFit() {
		// TODO: the -20 is a kludge to account for the width of the scrollbars, if showing.
		double horizZoom = (scroller.getWidth()-20) / currentPageDimensions.width ;
		double verticalZoom = (scroller.getHeight()-20) / currentPageDimensions.height ;
		zoom.set(Math.min(horizZoom, verticalZoom));
	}
	
	/**
	 * Zoom width the PDF.
	 */
	@FXML private void zoomWidth() {
		zoom.set((scroller.getWidth()-20) / currentPageDimensions.width) ;
	}

	// *************** Background image loading ****************
	
	/**
	 * Update image for multipages PDF.
	 * @param pageNumber - the PDF page number
	 */
	private void updateImage(final int pageNumber) {
		final Task<ImageView> updateImageTask = new Task<ImageView>() {
			@Override
			protected ImageView call() throws Exception {
				PDFPage page = currentFile.get().getPage(pageNumber+1);
				Rectangle2D bbox = page.getBBox();
				final double actualPageWidth = bbox.getWidth();
				final double actualPageHeight = bbox.getHeight();
				// record page dimensions for zoomToFit and zoomToWidth:
				currentPageDimensions = new PageDimensions(actualPageWidth, actualPageHeight);
				// width and height of image:
				final int width = (int) (actualPageWidth * zoom.get());
				final int height = (int) (actualPageHeight * zoom.get());
				// retrieve image for page:
				// width, height, clip, imageObserver, paintBackground, waitUntilLoaded:
				java.awt.Image awtImage = page.getImage(width, height, bbox, null, true, true); 
				// draw image to buffered image:
				BufferedImage buffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				buffImage.createGraphics().drawImage(awtImage, 0, 0, null);
				// convert to JavaFX image:
				Image image = SwingFXUtils.toFXImage(buffImage, null);
				// wrap in image view and return:
				ImageView imageView = new ImageView(image);
				imageView.setPreserveRatio(true);
				return imageView ;
			}
		};

		updateImageTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				//pagination.getScene().getRoot().setDisable(false);
				currentImage.set(updateImageTask.getValue());
			}
		});
		
		updateImageTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				//pagination.getScene().getRoot().setDisable(false);
				updateImageTask.getException().printStackTrace();
			}
			
		});
		
		//pagination.getScene().getRoot().setDisable(true);
		imageLoadService.submit(updateImageTask);
	}
	
	/**
	 * Show error message.
	 * @param message - String of the error message
	 * @param exception - Throwable exception
	 */
	private void showErrorMessage(String message, Throwable exception) {
		// TODO: move to fxml (or better, use ControlsFX)
		final Stage dialog = new Stage();
		dialog.initOwner(pagination.getScene().getWindow());
		dialog.initStyle(StageStyle.UNDECORATED);
		final VBox root = new VBox(10);
		root.setPadding(new Insets(10));
		StringWriter errorMessage = new StringWriter();
		exception.printStackTrace(new PrintWriter(errorMessage));
		final Label detailsLabel = new Label(errorMessage.toString());
		TitledPane details = new TitledPane();
		details.setText("Details:");
		Label briefMessageLabel = new Label(message);
		final HBox detailsLabelHolder =new HBox();
		
		Button closeButton = new Button("OK");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.hide();
			}
		});
		HBox closeButtonHolder = new HBox();
		closeButtonHolder.getChildren().add(closeButton);
		closeButtonHolder.setAlignment(Pos.CENTER);
		closeButtonHolder.setPadding(new Insets(5));
		root.getChildren().addAll(briefMessageLabel, details, detailsLabelHolder, closeButtonHolder);
		details.setExpanded(false);
		details.setAnimated(false);

		details.expandedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue) {
					detailsLabelHolder.getChildren().add(detailsLabel);
				} else {
					detailsLabelHolder.getChildren().remove(detailsLabel);
				}
				dialog.sizeToScene();
			}
			
		});
		final Scene scene = new Scene(root);

		dialog.setScene(scene);
		dialog.show();
	}
	
	/**
	 * The Class PageDimensions - like class intended to represent the physical dimensions of a page in pixels.
	 * (as opposed to the dimensions of the (possibly zoomed) view.
	 * Used to compute zoom factors for zoomToFit and zoomToWidth.
	 */
	private class PageDimensions {
		
		/** The width - width of page. */
		private double width ;
		
		/** The height - height of page.  */
		private double height ;
		
		/**
		 * Instantiates a new page dimensions.
		 * @param width - width of page.
		 * @param height - height of page.
		 */
		PageDimensions(double width, double height) {
			this.width = width ;
			this.height = height ;
		}
		
		/**
		 * To string of Page dimensions.
		 * @return String of Page dimensions "[width, height]".
		 */
		@Override
		public String toString() {
			return String.format("[%.1f, %.1f]", width, height);
		}
	}
}
