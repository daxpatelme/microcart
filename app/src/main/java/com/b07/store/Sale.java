package com.b07.store;

import com.b07.inventory.Item;
import com.b07.users.User;
import java.math.BigDecimal;
import java.util.HashMap;

public interface Sale {

  /**
   * Get the id of the sale
   * @return int id of sale
   */
  public int getId();

  /**
   * Set the id of the sale.
   * @param id int id of sale
   */
  public void setId(int id);

  /**
   * Get the user who bought the product.
   * @return User customer
   */
  public User getuser();

  /**
   * Set teh user who bought the product.
   * @param user user customer who made sale
   */
  public void setUser(User user);

  /**
   * Get teh total price of the sale.
   * @return BigDecimal price of sale
   */
  public BigDecimal getTotalPrice();

  /**
   * Set the total price of the sale.
   * @param price BigDecimal price of sale
   */
  public void setTotalPrice(BigDecimal price);

  /**
   * Get the hashmap representation of the sale with items and quantity.
   * @return HashMap<Item, Integer> of the sale
   */
  public HashMap<Item, Integer> getItemMap();

  /**
   * Set the itemized aspect of teh sale
   * @param itemMap HashMap<Item, Integer> of the sale quantity and items
   */
  public void setItemMap(HashMap<Item, Integer> itemMap);
}
