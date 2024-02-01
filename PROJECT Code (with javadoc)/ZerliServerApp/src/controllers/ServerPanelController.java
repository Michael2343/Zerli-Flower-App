package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Entities.ClientInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import reports.ReportScheduler;
import server.Server;
import server.ServerApp;


/**
 * The Class ServerPanelController is the controller part of the ServerPanelController GUI
 * used to manage UI and control UX for the server operator side while also starting vital
 * background processes.
 * 
 */
public class ServerPanelController implements Initializable{
	
 	/**  Server panel exit button. */
	 @FXML
	private ImageView exitBtn;
 	
    /**  Console text area used for logging. */
    @FXML
    private TextArea console;

    /** The Database status server. */
    @FXML
    private Label dbStatusServer;
    
    /** The server status label. */
    @FXML
    private Label serverStatusLbl;
    
    /** The connected client table. */
    @FXML
    private TableView<ClientInfo> clientTable;

    /** The ip column. */
    @FXML
    private TableColumn<ClientInfo, String> ipCol;
    
    /** The connected host column. */
    @FXML
    private TableColumn<ClientInfo, String> hostCol;

    /** The status column. */
    @FXML
    private TableColumn<ClientInfo, String> statusCol;

    /** The clients info. */
    ObservableList<ClientInfo> clientsInfo;
    
	/**  The stage used for window control. */
	private Stage stage;
	
    @FXML
    private Button importBtn;
	
	 /**
 	 * This event invokes when the exit button is pressed.
 	 *
 	 * @param MouseEvent - not used here
 	 */
 	@FXML
     void exitPressed(MouseEvent event) {
	 try {
    		ServerApp.server.close();
		} catch (IOException e) {
			Server.Log("Server","Falied close Server");
			e.printStackTrace();
		}
    	System.exit(0);
    }
 

	/**
	 * Initialization of the Server Panel Controller:
	 * property binding (ip, host, status)
	 * the thread that periodically scans for new hosts starts here
	 * the r-scheduler (see report scheduler for more info) compensator begins here.
	 *
	 * @param location - no use
	 * @param resources - no use
	 */
 	
	  @FXML
	    void importPressed(ActionEvent event) {
		  server.ServerConnSQL.importData();
		  importBtn.setVisible(false);
	    }
 	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ipCol.setCellValueFactory(new PropertyValueFactory<>("ip"));	
		hostCol.setCellValueFactory(new PropertyValueFactory<>("hostName"));	
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));	
		console.setEditable(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					clientsInfo = FXCollections.observableArrayList();
					for (int i= 0; i<ServerApp.server.getNumberOfClients();i++) {
						String ip = ((ConnectionToClient)ServerApp.server.getClientConnections()[i]).getInetAddress().getHostAddress();
			    		String host = ((ConnectionToClient)ServerApp.server.getClientConnections()[i]).getInetAddress().getHostName();
						clientsInfo.add(new ClientInfo(ip, host, "Active"));
					}
					clientTable.setItems(clientsInfo);
					try {Thread.sleep(2000);} 
					catch (InterruptedException e) {e.printStackTrace();}
					//Server.Log("Server", "Updating Connections List");
				}
			}
		}).start();
		Server.Console = console;
		Server.Log("Application", "Loading Application version " + Server.VERSION);
		Server.Log("Database", "is connected");
		Server.Log("Server", "is connected on port: " + Server.DEFAULT_PORT) ;
		ReportScheduler rScheduler = new ReportScheduler();
		new Thread(new Runnable() {

			@Override
			public void run() {
				rScheduler.Run();	
			}

		}).start();
	}
}

