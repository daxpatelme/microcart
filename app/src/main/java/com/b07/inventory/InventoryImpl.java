package com.b07.inventory;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

public class InventoryImpl implements Inventory, Serializable {

  private static InventoryImpl instance = new InventoryImpl();
  private HashMap<Item, Integer> itemMap;
  private int totalItems;
  private DatabaseSelectHelper selectHelper;
  
  private InventoryImpl(){
    
  }

  public static InventoryImpl getInstance() {
    return instance;
  }

  /**
   * Get item map of inventory.
   *
   * @return item map has hashmap where items are keys and the quantity of the item is the value
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    return itemMap;
  }

  /**
   * get amount of items in Inventory.
   *
   * @return int representing amount of total items in inventory
   */
  @Override
  public int getTotalItems() {
    return totalItems;
  }

  /**
   * Sets item map to the input HashMap of items and integers.
   *
   * @param itemMap the HashMap to be set
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> itemMap) {
    // TODO Auto-generated method stub
    this.itemMap = itemMap;
  }

  /**
   * get the quatity of an item
   * @param item item which is to be found
   * @return int quantity of the item
   */
  public int getQuantity (Item item) {
    return getItemMap().get(item);
  }

  /**
   * Given an item and an integer value, updates the inventory to have an amount of that item equal
   * to the value.
   *
   * @param item item to be added
   * @param value amount of item to be added
   */
  @Override
  public void updateMap(Item item, Integer value) {
    // if the item is in the inventory
    if (this.itemMap.containsKey(item)) {
      // determine what to add to the total value
      Integer adder = value - this.itemMap.get(item);
      // call setTotalItems with adder
      this.setTotalItems(this.totalItems + adder);
      // put item in map with its value
      this.itemMap.put(item, value);
      // if item is not in map
    } else {
      // simply place it in the map
      this.itemMap.put(item, value);
      // and add the value to the total items
      this.setTotalItems(this.totalItems + value);
    }

  }

  /**
   * sets total items.
   *
   * @param total the amount to set total to
   */
  @Override
  public void setTotalItems(int total) {
    // TODO Auto-generated method stub
    this.totalItems = total;
  }
}
