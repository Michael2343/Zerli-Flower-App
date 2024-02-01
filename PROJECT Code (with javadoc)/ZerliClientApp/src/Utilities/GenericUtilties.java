package Utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import Entities.*;
import controllers.HelperController;
import controllers.pdfComparer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GenericUtilties {
	public static void ShowHelp(String title,String helpImageName)
	{
		FXMLLoader loader = new FXMLLoader();
    	Parent root = null;
		loader.setLocation(GenericUtilties.class.getResource("/gui/mainframes/helper.fxml"));
		try {
			root =  loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		HelperController hCtrl = loader.getController();
		Stage newStage = new Stage();
		//Utilities.GenericUtilties.SetWindowMovable(root, newStage);
		Scene scene = new Scene(root);
		newStage.initStyle(StageStyle.UNDECORATED);
		SetWindowMovable(root,newStage);
		newStage.setScene(scene); 	
		hCtrl.SetHelper(title, helpImageName, newStage);
		newStage.show();

	}
	public static void SetWindowMovable(Parent parent,Stage stage)
	{
		class TwoInOneEventHandler {
			EventHandler<MouseEvent> mousePressed;
			EventHandler<MouseEvent> mouseDragged;
			private double xOffset = 0; 
			private double yOffset = 0;
			private Stage stage;
			private Parent parent;
			public TwoInOneEventHandler(Parent parent, Stage stage)
			{
				this.stage = stage;
				this.parent = parent;
				mousePressed = new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent event) {
		            	
		                xOffset = event.getSceneX();
		                yOffset = event.getSceneY();
		            }
				};
				mouseDragged = new EventHandler<MouseEvent>() {
		            @Override
		            public void handle(MouseEvent event) {
		                stage.setX(event.getScreenX() - xOffset);
		                stage.setY(event.getScreenY() - yOffset);
		            }
				};
			}
			public EventHandler<MouseEvent> GetPressedEvent()
			{
				return mousePressed;
			}
			
			public EventHandler<MouseEvent> GetDraggedEvent()
			{
				return mouseDragged;
			}
		};
		var DoubledEvents = new TwoInOneEventHandler(parent,stage);
		parent.setOnMousePressed(DoubledEvents.GetPressedEvent()); 
        parent.setOnMouseDragged(DoubledEvents.GetDraggedEvent());
	}
	
	public static java.sql.Timestamp Convert_LocalDate_To_SQLDate(LocalDate date,LocalTime time)
	{
		return java.sql.Timestamp.valueOf(date.atTime(time));
	}
	
	public static LocalDateTime Convert_LocalDate_To_SQLDate(java.sql.Timestamp datetime)
	{
		return datetime.toLocalDateTime();
	}
	
	public static String ColorToString(Color color)
	{
		switch(color)
		{	
			case red:
				return "Red";
			case blue:
				return "Blue";
			case green:
				return "Green";
			case white:
				return "White";
			case black:
				return "Black";
			case pink:
				return "Pink";
			case purple:
				return "Purple";
			case orange:
				return "Orange";
			case yellow:
				return "Yellow";
			case brown:
				return "Brown";
			default:
				return "No Color";
		}
	}
	
	public static ItemType StringToItemType(String item)
	{
		switch(item)
		{	
			case "Bouquet":
				return ItemType.bouquet;
			case "Bridal Bouquet":
				return ItemType.bridal_bouquet;
			case "Pot":
				return ItemType.pot;
			case "Flower":
				return ItemType.flower;
			case "Seed":
				return ItemType.seed;
			case "Branch":
				return ItemType.branch;
			case "Soil":
				return ItemType.soil;
			default:
				return ItemType.bouquet;
		}
	}
	public static CatalogType StringToCatalogType(String item)
	{
		switch(item)
		{	
			case "Item":
				return CatalogType.custom;
			case "Product":
				return CatalogType.pre_define;
			default:
				return CatalogType.new_item;
		}
	}
	public static Color StringToColor(String color)
	{
		switch(color)
		{	
			case "Red":
				return Color.red;
			case "Blue":
				return Color.blue;
			case "Green":
				return Color.green;
			case "White":
				return Color.white;
			case "Black":
				return Color.black;
			case "Pink":
				return Color.pink;
			case "Purple":
				return Color.purple;
			case "Orange":
				return Color.orange;
			case "Yellow":
				return Color.yellow;
			case "Brown":
				return Color.brown;
			default:
				return Color.non_color;
		}
	}
	
	public static String ItemTypeToString(ItemType itemType)
	{
		switch(itemType)
		{	
			case bouquet:
				return "Bouquet";
			case bridal_bouquet:
				return "Bridal Bouquet";
			case pot:
				return "Pot";
			case flower:
				return "Flower";
			case seed:
				return "Seed";
			case branch:
				return "Branch";
			case soil:
				return "Soil";
			default:
				return "Undefined: Add To Converter";
		}
	}
	public static String CatalogTypeToString(CatalogType itemType)
	{
		switch(itemType)
		{	
			case custom:
				return "Item";
			case pre_define:
				return "Product";
			default:
				return "Undefined: Add To Converter";
		}
	}
	
	
	public static Roles StringToRoles(String role)
	{
		switch(role)
		{	
			case "CEO":
				return Roles.ceo;
			case "Manager":
				return Roles.manager;
			case "Customer":
				return Roles.customer;
			case "Delivery":
				return Roles.delivery;
			case "Marketing":
				return Roles.marketing;
			case "Service":
				return Roles.service;
			case "Expert":
				return Roles.expert;
			default:
		}
		return null;
	}
	public static String RolesToString(Roles role)
	{
		switch(role)
		{	
			case ceo:
				return "CEO";
			case manager:
				return "Manager";
			case customer:
				return "Customer";
			case delivery:
				return "Delivery";
			case marketing:
				return "Marketing";
			case service:
				return "Service";
			case expert:
				return "Expert";
			default:
				return null;
		}
	}
	
	public static Access StringToAccessStatus(String access)
	{
		switch(access)
		{	
			case "Inactive":
				return Access.inactive;
			case "Active":
				return Access.active;
			case "Frozen":
				return Access.frozen;
			case "No authorization":
				return Access.noaut;
			default:
				return null;
		}
	}
	
	public static String AccessStatusToString(Access access)
	{
		switch(access)
		{	
			case inactive:
				return "Inactive";
			case active:
				return "Active";
			case frozen:
				return "Frozen";
			case noaut:
				return "No authorization";
			default:
				return "Undefined: Add To Converter";
		}
	}
	
	public static ReportType StringToReportType(String reportType)
	{
		switch(reportType)
		{
			case "Branch Income Report":
				return ReportType.income;
			case "Branch Order Report":
				return ReportType.order;
			case "Branch Complaints Report":
				return ReportType.service;
			case "CEO Income Report":
				return ReportType.ceo;
		default:
				return null;
		}
	}
	
	public static int DateToMonthly(LocalDate date)
	{
		return date.getMonthValue();
	}
	public static int DateToQuarter(LocalDate date)
	{
		return ((date.getMonthValue()-1) / 4) + 1; //1-4[jan-apr] : 1
	}
}
