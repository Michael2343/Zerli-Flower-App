package Entities;

import java.util.ArrayList;


/**
 * The Class NewItem.
 */
public class NewItem extends ItemInList{
	
	/** The assemble of a new item. */
	private ArrayList<ItemInList> assemble;
	
	/**
	 * Instantiates a new item.
	 */
	public NewItem() {
		super();
		assemble = new ArrayList<>();
	}
	
	/**
	 * Adds an existing item to the new item.
	 *
	 * @param item the item to add
	 */
	public void addItem(ItemInList item) {
		if(assemble.size()==0) {
			assemble.add(item);
			return;
		}
		int  i=0;
		
		for(ItemInList items: assemble) {
			if(items.getItem_id() == item.getItem_id()) break;
			else i++;
		}
		
		if(i == assemble.size()) {
			assemble.add(item);
		}
		else {
			ItemInList chosen = assemble.get(i);
			assemble.remove(i);
			chosen.setQuantity(chosen.getQuantity()+item.getQuantity());
			assemble.add(chosen);
		}
		
	}
	
	/**
	 * Deletes an item from the new item.
	 *
	 * @param item the item to delete
	 */
	public void deleteItem(ItemInList item) {
		if(assemble.size()==0) {
			return;
		}
		
		int i=0;
		
		for(ItemInList items: assemble) {
			if(items.getItem_id() == item.getItem_id()) break;
			else i++;
		}
		

		if(i == assemble.size()) {
			return;
		}
		else {
			assemble.remove(i);
		}
	}
	
	/**
	 * Returns a string contacting the item, the new item is made of
	 *
	 * @return string of items in the new item
	 */
	@Override
	public String toString() {
		String str = "";
		for(ItemInList items:assemble) {
			str+= items.toString();
			str+= "\n";
		}
		return str;
	}
		
	/**
	 * Gets the list of items the new item is made of.
	 *
	 * @return the list of items the new item is made of
	 */
	public ArrayList<ItemInList> getAssemble() {
		return assemble;
	}

	/**
	 * Sets the the list of items the new item is made of
	 *
	 * @param assemble the new list of items the new item is made of
	 */
	public void setAssemble(ArrayList<ItemInList> assemble) {
		this.assemble = assemble;
	}

}
