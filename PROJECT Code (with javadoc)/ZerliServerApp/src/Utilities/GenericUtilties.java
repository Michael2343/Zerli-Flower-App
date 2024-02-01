package Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import Entities.CatalogType;
import Entities.Color;
import Entities.ItemType;
import Entities.ReportType;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc


/**
 * The Class GenericUtilties holds many useful independent
 * methods that are generally used around the project.
 */
public class GenericUtilties {
	
	/**
	 * Sets the window movable by clicking on it, used when creating
	 * border-less windows (undecorated).
	 *
	 * @param parent the encapsulating GUI parent
	 * @param stage the holding stage
	 */
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
	
	/**
	 * Convert local date to SQL date.
	 *
	 * @param date the date localdate
	 * @param time the time as localtime
	 * @return the sql timestamp
	 */
	public static java.sql.Timestamp Convert_LocalDate_To_SQLDate(LocalDate date,LocalTime time)
	{
		return java.sql.Timestamp.valueOf(date.atTime(time));
	}
	
	/**
	 * Convert local date to SQL date.
	 *
	 * @param datetime the datetime
	 * @return the java.sql. timestamp
	 */
	public static java.sql.Timestamp Convert_LocalDate_To_SQLDate(LocalDateTime datetime)
	{
		return java.sql.Timestamp.valueOf(datetime);
	}
	
	/**
	 * Convert local date to SQL date.
	 *
	 * @param datetime the datetime
	 * @return the local date time
	 */
	public static LocalDateTime Convert_LocalDate_To_SQLDate(java.sql.Timestamp datetime)
	{
		return datetime.toLocalDateTime();
	}
	
	/**
	 * Color to string.
	 *
	 * @param color the color
	 * @return the color string value
	 */
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
	
	/**
	 * String to item type.
	 *
	 * @param item the item
	 * @return the ItemType you want to return
	 */
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
	
	/**
	 * String to catalog type.
	 *
	 * @param item the item
	 * @return the catalog type
	 */
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
	
	/**
	 * String to color.
	 *
	 * @param color the color
	 * @return the color
	 */
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
	
	/**
	 * Item type to string.
	 *
	 * @param itemType the item type
	 * @return the string
	 */
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
	
	/**
	 * Catalog type to string.
	 *
	 * @param itemType the item type
	 * @return the string
	 */
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
	
	/**
	 * String to report type.
	 *
	 * @param reportType the report type
	 * @return the report type
	 */
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
		default:
				return null;
		}
	}
	
	/**
	 * Date to monthly.
	 *
	 * @param date the date
	 * @return the int
	 */
	public static int DateToMonthly(LocalDate date)
	{
		return date.getMonthValue();
	}
	
	/**
	 * Date to quarter.
	 *
	 * @param date the date
	 * @return the int
	 */
	public static int DateToQuarter(LocalDate date)
	{
		return ((date.getMonthValue()-1) / 4) + 1; //1-4[jan-apr] : 1
	}
}
