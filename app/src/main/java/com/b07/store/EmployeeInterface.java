package com.b07.store;

import android.content.Context;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.database.serialization.Serialization;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.users.Admin;
import com.b07.users.User;

/**
 * Class representation of the interface.
 */
public class EmployeeInterface implements Serializable {
  // Initialize needed Variables.

  private Admin currentEmployee;
  private Inventory inventory;
  private Context context;
  private DatabaseInsertHelper insertHelper;
  private DatabaseUpdateHelper updateHelper;
  private DatabaseSelectHelper selectHelper;
  private Serialization ser;


  /**
   * Constructor containing only the inventory.
   * 
   * @param inventory store inventory
   */
  public EmployeeInterface(Context context,Inventory inventory) {
    // check if null and add either an empty inventory or given inventory.
    this.context = context;
    if (null == inventory) {
      this.inventory = InventoryImpl.getInstance();;
    } else {
      this.inventory = inventory;
    }
  }

  /**
   * construct the interface
   * 
   * @param currentEmployee employee using interface
   * @param inventory store inventory
   */
  public EmployeeInterface(Context context, Admin currentEmployee, Inventory inventory) {
    // Run constructor with inventory
    this(context, inventory);
    // CHeck for null and add employee if exists.
    if (null != currentEmployee) {
      this.currentEmployee = currentEmployee;
    }

  }

  /**
   * set new current employee
   * 
   * @param employee current employee user
   */
  public void setCurrentEmployee(Admin employee) {
    this.currentEmployee = employee;

  }

  public User getCurrentEmployee() {
    return this.currentEmployee;
  }

  /**
   * check if employee is using
   * 
   * @return bool as to wheter employee exists
   */
  public boolean hasCurrentEmployee() {
    // set the bool according to there being an employee.
    boolean result = true;
    if (null == this.currentEmployee) {
      result = false;
    }
    return result;
  }

  /**
   * restock inventory as to given preameters
   * 
   * @param item tiem to be restocked
   * @param quantity amount to be added
   * @return if restock was successful
\   */
  public boolean restockInventory(String item, int quantity) throws BadInputException,
          SQLException {
    updateHelper = new DatabaseUpdateHelper(context);
    selectHelper = new DatabaseSelectHelper(context);
    int itemId = 0;
    for (Item givenItem: selectHelper.getAllItemsHelper()) {
      if (givenItem.getName().equals(item)) {
        itemId = givenItem.getId();
      }
    }
    boolean isRestocked = true;
    // Attempt to restock.
    try {
      int currentQuantity = selectHelper.getInventoryQuantityHelper(itemId);
      isRestocked = updateHelper.updateInventoryQuantityHelper(currentQuantity + quantity, itemId);
    } catch (SQLException e) {
      isRestocked = false;
    }
    // return if correct restock.
    return isRestocked;
  }

  /**
   * Gets itemized and individual sales.
   * @return String representation of book
   * @throws SQLException on sql error
   * @throws BadInputException on failure to find given information in database
   */
  public String viewBooks() throws SQLException, BadInputException {
    StringBuilder sb = new StringBuilder();
    selectHelper = new DatabaseSelectHelper(context);
    SalesLog log = selectHelper.getSalesHelper();
    List<Sale> salesList = log.getLog();
    BigDecimal totalSales = new BigDecimal(0);
    sb.append("--------------------------------------------" + "\n");
    sb.append("         BEGINNING OF THE BOOK                |" + "\n");
    sb.append("--------------------------------------------" + "\n");
    for (Sale sale : salesList) {
      sb.append("Customer: " + sale.getuser().getName() + "\n");
      sb.append("Purchase Number: " + sale.getId() + "\n");
      System.out.println(sale.getTotalPrice());
      sb.append("Total Purchase Price: " + sale.getTotalPrice() + "\n");
      sb.append("Itemized Breakdown: \n");
      for (Item item : sale.getItemMap().keySet()) {
        System.out.println(item.getName());
        Integer value = sale.getItemMap().get(item);
        sb.append("\t" + item.getName() + ": " + value + "\n");
      }
      sb.append("--------------------------------------------" + "\n");
      totalSales = totalSales.add(sale.getTotalPrice());
    }
    sb.append(itemizedString(log.getItemizedMap()));
    sb.append("--------------------------------------------" + "\n");
    sb.append("TOTAL SALES: " + totalSales.toString() + "\n");
    sb.append("--------------------------------------------" + "\n");
    sb.append("           END OF THE BOOK                    |" + "\n");
    sb.append("--------------------------------------------" + "\n");

    return sb.toString();
  }

  /**
   * Get teh string representation of the item map.
   * @param itemizedMap map of the items and quantities
   * @return string representation
   */
  private String itemizedString(HashMap<Item, Integer> itemizedMap) {
    StringBuilder sb = new StringBuilder();
    for (Item item : itemizedMap.keySet()) {

      sb.append("Number of " + item.getName() + " sold: " + itemizedMap.get(item) + "\n");
    }
    return sb.toString();
  }

  /**
   * Get user classes which are active.
   * @param userId id of wanted user.
   * @return List<Account> of active accounts.
   */
  public List<Account> getActiveAccounts(int userId) {
    // Create an empty list and initilise list of all user accounts.
    List<Account> accounts = new ArrayList<Account>();
    List<Account> userAccounts;
    // Try to retireve user accounts if error occures then return empty list.
    try {
      selectHelper = new DatabaseSelectHelper(context);
      userAccounts = selectHelper.getUserAccountsHelper(userId);
    } catch (SQLException e) {
      return accounts;
    } catch (BadInputException e) {
      return accounts;
    }
    // Loop through and add all active accounts.
    for (Account account: userAccounts) {
      if (account.getActive() == 1) {
        accounts.add(account);
      }
    }
    // Return active accounts.
    return accounts;
  }
  /**
   * Get user classes which are inactive.
   * @param userId id of wanted user.
   * @return List<Account> of inactive accounts.
   */
  public List<Account> getInactiveAccounts(int userId) {
    // Create an empty list and initilise list of all user accounts.
    List<Account> accounts = new ArrayList<Account>();
    List<Account> userAccounts;
    // Try to retireve user accounts if error occures then return empty list.
    try {
      selectHelper = new DatabaseSelectHelper(context);
      userAccounts = selectHelper.getUserAccountsHelper(userId);
    } catch (SQLException e) {
      return accounts;
    } catch (BadInputException e) {
      return accounts;
    }
    // Loop through and add all inactive accounts.
    for (Account account: userAccounts) {
      if (account.getActive() == 0) {
        accounts.add(account);
      }
    }
    // Return inactive accounts.
    return accounts;
  }

  /**
   * Serializes the database and stores the database_copy.ser in
   * current main directory
   * @throws IOException
   * @throws SQLException
   */

  public long addNewItem(String itemName, BigDecimal price) throws DatabaseInsertException, SQLException,
          BadInputException {
    insertHelper = new DatabaseInsertHelper(context);
    long itemId = insertHelper.insertItemHelper(itemName, price);
    insertHelper.insertInventoryHelper((int) itemId, 0);
    return itemId;
  }

}
