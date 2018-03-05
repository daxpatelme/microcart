package com.b07.store;

import java.util.HashMap;
import java.util.List;
import com.b07.inventory.Item;

/**
 * the class reprsentatio of sales log.
 * 
 * @author Robert
 *
 */
public interface SalesLog {

  /**
   * add sale to the log
   * 
   * @param sale commited sale
   */
  public void addSale(Sale sale);

  /**
   * return the log to the user
   * 
   * @return list of all sales.
   */
  public List<Sale> getLog();

  /**
   * Get the itemized map of the sales.
   * @return HashMap<Item, Integer> of the getItemizedMap
   */
  public HashMap<Item, Integer> getItemizedMap();

  /**
   * Get index sales.
   * @param i index of sale
   * @return Sale at given index
   */
  public Sale get(int i);
}
