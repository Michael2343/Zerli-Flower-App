package Entities;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javafx.scene.image.Image;


/**
 * The Class ItemInList hold the details about an item in a given order.
 */
public class ItemInList implements Serializable{

	/** The item's id. */
	private int item_id;
	
	/** The item's quantity. */
	private int quantity;
	
	/** The item's price. */
	private int price;
	
	/** The item's name. */
	private String itemName;
	
	/** The item's type - ItemType format. */
	private ItemType itemType;
	
	/** The item's catalog type - CatalogType format. */
	private CatalogType catalogType;
	
	/** The item's type - String format. */
	private String item_type;
	
	/** The item's catalog type - String format. */
	private String catalog_Type;
	
	/** The item's image. */
	private byte[] image;
	
	/**
	 * Instantiates a new item in list.
	 */
	public ItemInList() {}
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param image the item's image
	 * @param item_id the item's ID
	 * @param quantity the item's quantity
	 * @param price the item's price
	 * @param itemName the item's name
	 * @param itemType the item's type
	 * @param catalogType the item's catalog type
	 */
	public ItemInList(byte[] image,int item_id, int quantity, int price,String itemName, ItemType itemType, CatalogType catalogType) {
		super();
		this.image=image;
		this.item_id = item_id;
		this.quantity = quantity;
		this.price=price;
		this.itemName = itemName;
		this.itemType = itemType;
		this.catalogType = catalogType;
		
	}	
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param itemName the item's name
	 * @param catalogType the item's catalog type
	 * @param itemType the item's type
	 * @param price the item's price
	 * @param quantity the item's quantity
	 */
	public ItemInList(String itemName,CatalogType catalogType, 
			ItemType itemType, int price, int quantity) {
		this.quantity = quantity;
		this.price = price;
		this.itemName = itemName;
		this.itemType = itemType;
		this.catalogType = catalogType;
	}
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param item_id the item's ID
	 * @param quantity the item's quantity
	 */
	public ItemInList(int item_id, int quantity) {
		this.item_id = item_id;
		this.quantity = quantity;
	}
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param item_id the item's ID
	 * @param quantity the item's quantity
	 * @param itemName the item's name
	 * @param itemType the item's type
	 * @param catalogType the item's catalog type
	 */
	public ItemInList(int item_id, int quantity, String itemName, ItemType itemType, CatalogType catalogType) {
		this.item_id = item_id;
		this.quantity = quantity;
		this.itemName = itemName;
		this.itemType = itemType;
		this.catalogType = catalogType;
	}
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param item_id the item's ID
	 * @param quantity the item's quantity
	 * @param price the item's price
	 * @param itemName the item's name
	 * @param itemType the item's type
	 * @param catalogType the item's catalog type
	 */
	public ItemInList(int item_id, int quantity, int price, String itemName, ItemType itemType,
			CatalogType catalogType) {
		this.item_id = item_id;
		this.quantity = quantity;
		this.price = price;
		this.itemName = itemName;
		this.itemType = itemType;
		this.catalogType = catalogType;
	}
	
	/**
	 * Instantiates a new item in list.
	 *
	 * @param item_id the item's ID
	 * @param itemName the item's name
	 * @param quantity the item's quantity
	 */
	public ItemInList(int item_id, String itemName, int quantity) {
		this.item_id = item_id;
		this.quantity = quantity;
		this.itemName = itemName;
	}

	/**
	 * Gets the item's image.
	 *
	 * @return the image
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
	 * Gets the item's price.
	 *
	 * @return the item's price
	 */
	public int getPrice() {
		return price;
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
	 * Gets the item's ID.
	 *
	 * @return the item's ID
	 */
	public int getItem_id() {
		return item_id;
	}

	/**
	 * Sets the item's ID.
	 *
	 * @param item_id the new item's ID
	 */
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	/**
	 * Gets the item's quantity.
	 *
	 * @return the item's quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the item's quantity.
	 *
	 * @param quantity the new item's quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public String getItem_type() {
		if(itemType==null) return"";
		switch (itemType) {
		case bridal_bouquet: return "Bridal bouquet";
		default:
			String type= itemType.toString();
			String s1 = type.substring(0, 1).toUpperCase();
			String nameCapitalized = s1 + type.substring(1);
			return nameCapitalized;
		}
	}
	
	/**
	 * Sets the item's type.
	 *
	 * @param item_type the new item's type
	 */
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
	
	/**
	 * Gets the item's name.
	 *
	 * @return the item's name
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Sets the item's name.
	 *
	 * @param itemName the new item's name
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Gets the item's catalog type.
	 *
	 * @return the item's catalog type
	 */
	public String getCatalog_Type() {
		switch (catalogType) {
			case custom:return "Custom";
			case pre_define:return "Pre define";
			case new_item: return "New item";
		}
		return "";
	}

	/**
	 * Sets the item's catalog type.
	 *
	 * @param catalog_Type the new item's catalog type
	 */
	public void setCatalog_Type(String catalog_Type) {
		this.catalog_Type = catalog_Type;
	}

	/**
	 * Returns a string containing the item's attributes
	 *
	 * @return String containing the item's attributes
	 */
	@Override
	public String toString() {
		return "ItemInList [item_id=" + item_id + ", quantity=" + quantity + ", price=" + price + ", itemName="
				+ itemName + ", item_type=" + item_type + ", catalog_Type=" + catalog_Type + "]";
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