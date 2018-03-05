package com.b07.store;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.users.Customer;

/**
 * Account class holding user cart
 * @author Robert
 *
 */
public class Account implements Serializable {
  // initialize needed variables
  private ShoppingCart shoppingCart;
  private int accountId;
  private int active;
  private DatabaseUpdateHelper updateHelper;
  private DatabaseInsertHelper insertHelper;
  private Context context;

  /**
   * Construct account.
   * @param accountId Id of account.
   * @param customer customer associalted with account.
   * @param active activity level of account.
   */
  public Account(Context context, int accountId, Customer customer, int active) {
    // Create the account information variables
    this.accountId = accountId;
    this.shoppingCart = new ShoppingCart(context, customer);
    this.active = active;
    this.context = context;
    updateHelper = new DatabaseUpdateHelper(context);
    insertHelper = new DatabaseInsertHelper(context);

  }

  /**
   * Add item to generate users shopping cart.
   * @param item item to be added to cart.
   * @param quantity quantitiy of item to be added to cart.
   */
  public void addItem(Item item, int quantity) throws SQLException, BadInputException {
    // Add item to cart
    this.shoppingCart.addItem(item.getName(), quantity);
  }

  /**
   * get activility leve of account
   * @return int representaion of active level
   */
  public int getActive() {
    // Return Activity
    return this.active;
  }

  /**
   * Set account to inactive
   */
  public void setActive(int active) {
    // Set account to inactive.
    this.active = active;
  }

  /**
   * Get the accounts shoppingcart.
   * @return ShoppingCart related to account
   */
  public ShoppingCart getShoppingCart() {
    // Return ShoppingCart.
    return this.shoppingCart;
  }

  /**
   * Refresh the cart stored in Database
   * @throws SQLException failere in SQLE.
   * @throws DatabaseInsertException Occurs if the insersion of rows fails.
   * @throws BadInputException Occurs if bad values are passed.
   */
  public void refreshCart() throws SQLException, DatabaseInsertException, BadInputException {
    // Check if account is active
    if (this.active == 1) {
      // Clear the previous cart
      updateHelper = new DatabaseUpdateHelper(context);
      updateHelper.removeCart(this.accountId);
        // Nothing occures as an error is expected if the cart is already empty
      // Add the new cart.
      List<Item> items = this.shoppingCart.getItems();
      insertHelper = new DatabaseInsertHelper(context);
      for (Item e : items) {
        insertHelper.insertAccountLineHelper(this.accountId, e.getId(),
            this.shoppingCart.getQuantity(e));
      }
    }
  }

  /**
   * Get the id of teh account.
   * @return int represenataion of the account id
   */
  public int getAccountId() {
    // return the accounts id.
    return accountId;
  }
}
