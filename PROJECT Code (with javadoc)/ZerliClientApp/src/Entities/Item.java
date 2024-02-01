package Entities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javafx.scene.image.Image;


/**
 * The Class Item holds all the necessary details 
 * about an item .
 */
public class Item implements Serializable {
	
	/** The item's ID. */
	private int id;
	
	/** The item's name. */
	private String name;
	
	/** The item's price. */
	private int price;
	
	/** The item's catalog type. */
	private  CatalogType catalogType ;
	
	/** The item's type. */
	private ItemType itemType;
	
	/** The item's color. */
	private Color color;
	
	/** The item's image. */
	private byte[] image;
	
	/** Shows if an item is on sale. */
	private boolean isOnSale = false;
	
	/** The item's sale price. */
	private int salePrice = -1;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param id the item's ID
	 * @param name the item's name
	 * @param price the item's price
	 * @param catalogType the item's catalog type
	 * @param itemType the item's type
	 * @param color the item's color
	 * @param image the item's image
	 * @param isOnSale shows if an item is on sale
	 * @param salePrice the item's sale price
	 */
	public Item( int id, String name,int price, CatalogType catalogType, ItemType itemType, Color color, byte[] image,boolean isOnSale, int salePrice) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.catalogType = catalogType;
		this.itemType = itemType;
		this.color = color;
		this.image =image;
		this.salePrice = salePrice;
		this.isOnSale = isOnSale;
	}
	
	/**
	 * Checks if an item is on sale.
	 *
	 * @return true, if the item is on sale
	 */
	public boolean isOnSale() {
		return isOnSale;
	}
	
	/**
	 * Sets the item on sale status.
	 *
	 * @param isOnSale the new  item's on sale status
	 */
	public void setOnSale(boolean isOnSale) {
		this.isOnSale = isOnSale;
	}
	
	/**
	 * Gets the item's sale price.
	 *
	 * @return the item's sale price
	 */
	public int getSalePrice() {
		return salePrice;
	}
	
	/**
	 * Sets the item's sale price.
	 *
	 * @param salePrice the new item's sale price
	 */
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}
	
	/**
	 * Gets the item's name.
	 *
	 * @return the item's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the item's name.
	 *
	 * @param name the new item's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the item's ID.
	 *
	 * @return the item's ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the item's ID.
	 *
	 * @param id the new item's ID
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the item's price.
	 *
	 * @return the item's price
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Gets the item's current price.
	 *
	 * @return the item's current price
	 */
	public int getCurrentPrice()
	{
		return isOnSale? salePrice : price;
	}
	
	/**
	 * Sets the item's price.
	 *
	 * @param price the new item's price
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * Gets the item's catalog type.
	 *
	 * @return the item's catalog type
	 */
	public CatalogType getCatalogType() {
		return catalogType;
	}
	
	/**
	 * Sets the item's catalog type.
	 *
	 * @param catalogType the new item's catalog type
	 */
	public void setCatalogType(CatalogType catalogType) {
		this.catalogType = catalogType;
	}
	
	/**
	 * Gets the item's type.
	 *
	 * @return the item's type
	 */
	public ItemType getItemType() {
		return itemType;
	}
	
	/**
	 * Sets the item's type.
	 *
	 * @param itemType the new item's type
	 */
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
	/**
	 * Gets the item's color.
	 *
	 * @return the item's color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the item's color.
	 *
	 * @param color the new item's color
	 */
	public void setColor(Color color) {
		this.color = color;	
	}
	
	/**
	 * Gets the item's image.
	 *
	 * @return the item's image
	 */
	public byte[] getImage() {
		return image;
	}
	
	/**
	 * Sets the item's image.
	 *
	 * @param image the new item's image
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	/**
	 * Gets the item's JX image.
	 *
	 * @return the item's JX image
	 */
	public Image getJXImage() {
		return new Image(new ByteArrayInputStream(image));
	}
}