package com.b07.inventory;

import java.math.BigDecimal;

public interface Item {

  /**
   * Get the item id.
   * @return int id of item
   */
  public int getId();

  /**
   * Set the id of the item.
   * @param id int representation of the id
   */
  public void setId(int id);

  /**
   * Get the name of the item.
   * @return Sreing representation of the name.
   */
  public String getName();

  /**
   * Set the name of the item.
   * @param name string representation of the name
   */
  public void setName(String name);

  /**
   * Get the price.
   * @return BigDecimal price of item
   */
  public BigDecimal getPrice();

  /**
   * Set the price of the item.
   * @param price BigDecimal price of the object
   */
  public void setPrice(BigDecimal price);

  /**
   * Check if item is equal to current item.
   * @param item other item
   * @return boolean whether items are equal
   */
  public boolean equals(Item item);

}
