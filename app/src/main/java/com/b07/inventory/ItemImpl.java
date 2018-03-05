package com.b07.inventory;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemImpl implements Item, Serializable {
  // Initialize the needed Variables.
  private int id;
  private String name;
  private BigDecimal price;

  /**
   * Construct the Item.
   * @param id int id of the item
   * @param name String name of item
   * @param price BigDecimal Price of item
   */
  public ItemImpl(int id, String name, BigDecimal price) {
    // Set the variables
    this.setId(id);
    this.setName(name);
    this.setPrice(price);
  }

  /**
   * Get the item id.
   * @return int id of item
   */
  @Override
  public int getId() {
    return this.id;
  }

  /**
   * Set the id of the item.
   * @param id int representation of the id
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the name of the item.
   * @return Sreing representation of the name.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of the item.
   * @param name string representation of the name
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the price.
   * @return BigDecimal price of item
   */
  @Override
  public BigDecimal getPrice() {
    return this.price;
  }

  /**
   * Set the price of the item.
   * @param price BigDecimal price of the object
   */
  @Override
  public void setPrice(BigDecimal price) {
    price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    this.price = price;
  }

  /**
   * Check if item is equal to current item.
   * @param item other item
   * @return boolean whether items are equal
   */
  @Override
  public boolean equals(Item item) {
    if (null == item) {
      return false;
    } else {
      return this.getId() == item.getId();
    }

  }


}
