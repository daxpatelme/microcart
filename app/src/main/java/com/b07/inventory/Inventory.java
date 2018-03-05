package com.b07.inventory;

import java.util.HashMap;

public interface Inventory {

  /**
   * Get item map of inventory.
   * 
   * @return item map has hashmap where items are keys and the quantity of the item is the value
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * get amount of items in Inventory.
   * 
   * @return int representing amount of total items in inventory
   */
  public int getTotalItems();

  /**
   * Sets item map to the input HashMap of items and integers.
   * 
   * @param itemMap the HashMap to be set
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);

  /**
   * Given an item and an integer value, updates the inventory to have an amount of that item equal
   * to the value.
   * 
   * @param item item to be added
   * @param value amount of item to be added
   */
  public void updateMap(Item item, Integer value);

  /**
   * sets total items.
   * 
   * @param total the amount to set total to
   */
  public void setTotalItems(int total);

  /**
   * get the quatity of an item
   * @param item item which is to be found
   * @return int quantity of the item
   */
  public int getQuantity (Item item);

}
