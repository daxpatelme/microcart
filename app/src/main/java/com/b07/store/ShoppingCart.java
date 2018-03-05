package com.b07.store;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.users.Customer;

public class ShoppingCart implements Serializable {
  // Create base variables.
  private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
  private Customer customer;
  private BigDecimal total = new BigDecimal(0);
  private static final BigDecimal TAXRATE = new BigDecimal(1.13);
  private static final BigDecimal DISCOUNT_LIMIT = new BigDecimal(100);
  private DatabaseInsertHelper insertHelper;
  private DatabaseSelectHelper selectHelper;
  private DatabaseUpdateHelper updateHelper;
  private Context context;
  private int activeAccountId;

  /**
   * construct class.
   * 
   * @param customer customer using the shopping cart
   */
  public ShoppingCart(Context context, Customer customer) {
    // If not null add the customer to the shopping cart.
    if (null != customer) {
      this.customer = customer;
    }
    this.context = context;

  }

  public void setActiveAccountId(int id){
    this.activeAccountId = id;
  }

  public void setCustomer (Customer customer) {
    this.customer = customer;
  }

  public int getActiveAccountId() {
    return this.activeAccountId;
  }

  public HashMap<Item, Integer> getItemMap() {
    return this.items;
  }

  /**
   * Add item to the cart
   * 
   * @param itemName item wanted to be added to cart
   * @param quantity number of such itme to be added t cart
   */
  public void addItem(String itemName, int quantity) throws SQLException,BadInputException {
    Item item = null;
    selectHelper = new DatabaseSelectHelper(context);
    for (Item givenItem: selectHelper.getAllItemsHelper()) {
      if (givenItem.getName().equals(itemName)) {
        item = givenItem;
      }
    }
    // check if inputs are valid.
    if (quantity > 0 && item != null) {
      // if the item is already in cart add thte quantity to the existing one.
      Item itemToUpdate = getSimilarItem(item);
      if (this.items.containsKey(itemToUpdate)) {
        items.replace(itemToUpdate, items.get(itemToUpdate) + quantity);
        // ELse add the item and quantity to cart
      } else {
        items.put(item, quantity);
      }
      // update total values based on inputed item.
      BigDecimal newQuantity = new BigDecimal(quantity);
      this.total = this.total.add(item.getPrice().multiply(newQuantity));
    }
  }

  public int getQuantity(Item item) {
    return this.items.get(item);
  }

  // ADDED BY DAX AND ALEX PLEASE CONSULT BEFORE CHANIGNG
  private Item getSimilarItem(Item item) {
    Item hashmapItem = null;
    for (Item i : this.items.keySet()) {
      if (i.getId() == item.getId()) {
        hashmapItem = i;
      }
    }
    return hashmapItem;
  }

  /**
   * remove item from the cart
   * 
   * @param itemName items to be removed
   * @param quantity amount of item to e removed
   */
  public void removeItem(String itemName, int quantity) throws SQLException,BadInputException {
    Item item = null;
    selectHelper = new DatabaseSelectHelper(context);
    for (Item givenItem: selectHelper.getAllItemsHelper()) {
      if (givenItem.getName().equals(itemName)) {
        item = givenItem;
      }
    }
    // chekc if quanitiy and item are valid
    Item itemToUpdate = getSimilarItem(item);
    if (quantity > 0 && this.items.get(itemToUpdate) != null) {
      // if quantity bigger then remove item and decrease total by original amount of item
      if (this.items.get(itemToUpdate) - quantity <= 0) {
        BigDecimal newQuantity = new BigDecimal(this.items.get(itemToUpdate));
        this.total = this.total.subtract(itemToUpdate.getPrice().multiply(newQuantity));
        this.items.remove(itemToUpdate);
        // Remove item by quanityt and subtract total by amount removed by quantity.
      } else {
        BigDecimal newQuantity = new BigDecimal(quantity);
        this.total = this.total.subtract(itemToUpdate.getPrice().multiply(newQuantity));
        this.items.replace(itemToUpdate, this.items.get(itemToUpdate) - quantity);
      }
    }
  }

  /**
   * get the list of items
   * 
   * @return list of items
   */
  public List<Item> getItems() {
    // Create a list of items in map.
    List<Item> itemList = new ArrayList<Item>();
    for (Item e : this.items.keySet()) {
      itemList.add(e);
    }
    // Return liat of items.
    return itemList;
  }

  /**
   * using customer
   * 
   * @return Customer of cart
   */
  public Customer getCustomer() {
    return this.customer;
  }

  /**
   * get total
   * 
   * @return BigDemimal of total
   */
  public BigDecimal getTotal() {
    return this.total;
  }

  /**
   * get tas rate
   * 
   * @return BigDecimal of tac rate
   */
  public BigDecimal getTaxRate() {
    return ShoppingCart.TAXRATE;
  }

  /**
   * return the discount limit
   * @return BigDecimal of discount limit
   */
  public BigDecimal getDiscountLimit(){return ShoppingCart.DISCOUNT_LIMIT;}
  /**
   * Remove all items from cart.
   */
  public void clearCart() {
    this.items = new HashMap<Item, Integer>();
  }

  /**
   * checkout and buy items in shopping cart
   * 
   * @param shoppingCart cart with all items wish to buy
   * @return boolean whether cart was bought.
   * @throws DatabaseInsertException
   * @throws BadInputException
   */
  public boolean checkOutCustomer(ShoppingCart shoppingCart) throws
      DatabaseInsertException, BadInputException, SQLException  {
    // Check if the user does not exist or the quantity does not suffice.
    if (null == customer || !shoppingCart.checkQuantitiy(shoppingCart.getItems())) {
      return false;
    }
    try {
      // get the id of the built sale.
      insertHelper = new DatabaseInsertHelper(context);
      long sale = insertHelper.insertSaleHelper(shoppingCart.getCustomer().getId(),
          shoppingCart.getTaxRate().multiply(shoppingCart.getTotal()).setScale(2,
              BigDecimal.ROUND_HALF_EVEN));
      // Loop through the items in shopping cart
      updateHelper = new DatabaseUpdateHelper(context);
      for (Item e : shoppingCart.getItems()) {
        // create the needed itemized sale.
        insertHelper.insertItemizedSaleHelper((int) sale, e.getId(), shoppingCart.items.get(e));
        // get new quantity for the item in cart.
        selectHelper = new DatabaseSelectHelper(context);
        int newQuantity =
                selectHelper.getInventoryQuantityHelper(e.getId()) - shoppingCart.items.get(e);
        // Remove items from inventory.
        updateHelper.updateInventoryQuantityHelper(newQuantity, e.getId());
      }
    } catch (SQLException e1) {
      return false;
    }
    
    // CLear the cart.
    shoppingCart.clearCart();
    // Update total value with tax
    // get cart total
    BigDecimal cartTotal = shoppingCart.getTotal();
    //check if customer has discount greater than 0 dollars
    if(this.customer.getDiscount().compareTo(new BigDecimal(0)) == 1){
      // remove discount from db
      if(this.total.compareTo(this.customer.getDiscount())==-1){
        System.out.println(this.total.negate());
        BigDecimal newDisc = this.customer.getDiscount().subtract(this.total);
        System.out.println(this.total);
        this.total = new BigDecimal(0);
        updateHelper.removeDiscountHelper(this.customer.getId());
        insertHelper.insertDiscountHelper(this.customer.getId(), newDisc);
        //System.out.println(this.total);

      }else {
        // subtract the discount from the db
        this.total = cartTotal.subtract(this.customer.getDiscount());
        updateHelper.removeDiscountHelper(this.customer.getId());
      }

    }
    shoppingCart.total = shoppingCart.getTaxRate().multiply(this.total).setScale(2,
        BigDecimal.ROUND_HALF_EVEN);

    //if the total before tax was above hundred insert discount for user for next purchase
    if(this.total.compareTo(this.getDiscountLimit()) == 1){
      this.getCustomer().insertDiscount();
      Toast.makeText(context,"You have  $10 Store credit for next purchase", Toast.LENGTH_LONG ).show();
    }
    return true;
  }

  /**
   * Check if you buy all items by quantity
   * 
   * @param items list of items.
   * @return Boolean if the items can be bought
   */
  private boolean checkQuantitiy(List<Item> items) throws BadInputException {
    // Set bool value.
    boolean enougthItems = true;
    // Loop through each item and check if the inventoy quantity satisifies the sale quantity.
    for (Item e : items) {
      try {
        selectHelper = new DatabaseSelectHelper(context);
        if (selectHelper.getInventoryQuantityHelper(e.getId()) < this.items.get(e)) {
          enougthItems = false;
        }
      } catch (SQLException e1) {
        // retun false if an error occures in getting quantity as it does not exist.
        return false;
      }
    }
    // Return if the sale can occur.
    return enougthItems;
  }

}
