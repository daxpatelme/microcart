package com.b07.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import com.b07.inventory.Item;
import com.b07.users.User;

public class SaleImpl implements Sale, Serializable {
  // Initialize fields.
  private int id;
  private User user;
  private BigDecimal totalPrice;
  private HashMap<Item, Integer> itemMap = new HashMap<>();

  /**
   * Construct a sale
   * @param id int id of sale
   * @param user User of customer
   * @param totalPrice Bigdecimal price of sale
   */
  public SaleImpl(int id, User user, BigDecimal totalPrice) {
    this.setId(id);
    this.setTotalPrice(totalPrice);
    this.setUser(user);
  }

  /**
   * Get the id of the sale.
   * @return int id of sale
   */
  @Override
  public int getId() {
    // TODO Auto-generated method stub
    return this.id;
  }

  /**
   * Set the id of the sale.
   * @param id int id of sale
   */
  @Override
  public void setId(int id) {
    this.id = id;

  }

  /**
   * Get the user who bought the product.
   * @return User customer
   */
  @Override
  public User getuser() {
    // TODO Auto-generated method stub
    return this.user;
  }

  /**
   * Set teh user who bought the product.
   * @param user user customer who made sale
   */
  @Override
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Get teh total price of the sale.
   * @return BigDecimal price of sale
   */
  @Override
  public BigDecimal getTotalPrice() {
    return this.totalPrice;
  }

  /**
   * Set the total price of the sale.
   * @param totalPrice BigDecimal price of sale
   */
  @Override
  public void setTotalPrice(BigDecimal totalPrice) {
    // BigDecimal totalPrice = new BigDecimal(0);
    // for(Item item: this.itemMap.keySet()) {
    // BigDecimal price = item.getPrice();
    // int quantity = this.itemMap.get(item);
    // totalPrice = totalPrice.add(price.multiply(new BigDecimal(quantity))) ;
    // }
    totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    this.totalPrice = totalPrice;
  }

  /**
   * Get the hashmap representation of the sale with items and quantity.
   * @return HashMap<Item, Integer> of the sale
   */
  @Override
  public HashMap<Item, Integer> getItemMap() {
    HashMap<Item, Integer> newItemMap = new HashMap<>();
    for (Item item : this.itemMap.keySet()) {
      newItemMap.put(item, this.itemMap.get(item));
    }
    return newItemMap;

  }

  /**
   * Set the itemized aspect of teh sale
   * @param newItemMap HashMap<Item, Integer> of the sale quantity and items
   */
  @Override
  public void setItemMap(HashMap<Item, Integer> newItemMap) {
    for (Item item : newItemMap.keySet()) {
      this.itemMap.put(item, newItemMap.get(item));
    }
  }

}
