package com.b07.database.helper;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.store.Sale;
import com.b07.store.SalesLog;
import com.b07.users.Roles;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class DatabaseInsertHelper extends DatabaseDriverAndroid {
  private Context context;

  /**
   * Create a new object helper so that the nonstatic methods can be used.
   * @param context the given contex passed down from the activity.
   */
  public DatabaseInsertHelper(Context context) {
    // Call the super constructor and set the context
    super(context);
    this.context = context;
  }
  /**
   * Inserts new roles into the database.
   *
   * @param name the name of the role.
   * @return role of the id that was inserted if no insert exception is thrown, otherwise returns -1
   * @throws SQLException Exception on failure to close database
   * @throws DatabaseInsertException on failure to insert into database
   * @throws BadInputException a bad input was added to select method.
   */
  public long insertRoleHelper(String name) throws DatabaseInsertException, SQLException,
      BadInputException {
    // Initialize the role id value.
    long roleId;
    // Check for good input.
    if (null == name) {
      throw new DatabaseInsertException();
    } else {
      boolean validRole = false;
      name = name.toUpperCase();
      // Loop through roles enum and see if name matches any enum.
      for (Roles role : Roles.values()) {
        if (role.toString().equalsIgnoreCase(name)) {
          validRole = true;
        }
      }
      // If the role is not valid throw and exception, else add the role to the database
      // returning the id.
      if (!validRole) {
        throw new DatabaseInsertException();
      } else {
        roleId = this.isRoleInDb(name);
        if (roleId == -1) {
          roleId = this.insertRole(name);
          return roleId;
        } else {
          return roleId;
        }
      }
    }
  }

  /**
   * Inserts a new user into the database.
   * 
   * @param name the name of the user
   * @param age the age of the user
   * @param address the address of the user
   * @param password the users password
   * @return the users id
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to insert into database
   */
  public long insertNewUserHelper(String name, int age, String address, String password)
      throws DatabaseInsertException, SQLException {
    // Check for null values.
    if (null == name || null == address || null == password || name.length() < 2 
        || address.length() > 100 || password.length() < 1) {
      throw new DatabaseInsertException();
    // Add the user to the database.
    } else {
      long userId = this.insertNewUser(name, age, address, password);
      return userId;
     
    }
  }

  /**
   * Inserts a relationship between a user and a role into the database.
   * 
   * @param userId the id of the user
   * @param roleId the id of the role
   * @return the relationship id
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to insert into database
   */
  public long insertUserRoleHelper(int userId, int roleId) throws DatabaseInsertException,
      SQLException {
    // Verify the roleid and add the user role to the user.
    if (!this.verifyRoleId(roleId)) {
      throw new DatabaseInsertException();
    } else {
      long userRoleId = this.insertUserRole(userId, roleId);
      return userRoleId;
    }
  }

  /**
   * Inserts an item into the database.
   * 
   * @param name the name of the item
   * @param price the price of the item
   * @return the item id
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to insert into database
   */
  public long insertItemHelper(String name, BigDecimal price)
      throws DatabaseInsertException, SQLException {
    // Check for bad input.
    if (null == name || null == price || name.length() > 64 || name.length() == 0 
        || price.compareTo(new BigDecimal(1)) < -1) {
      throw new DatabaseInsertException();
    } else {
      // round the big decimal and return the item id
      price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      long itemId = this.insertItem(name, price);
      return itemId;
    }

  }

  /**
   * Inserts discount of the user.
   * @param userId id of the user
   * @param amount amount to be inserted
   * @throws DatabaseInsertException on failure to insert into database
   * @throws SQLException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public void insertDiscountHelper(int userId, BigDecimal amount)throws DatabaseInsertException,SQLException,BadInputException{
    // Veryfy user and add a discount to the user
    if(this.verifyUserId(userId)){
      this.insertDiscount(userId, amount);
    } else {
      throw new BadInputException();
    }
  }

  /**
   * Inserts an inventory into the database.
   * @param itemId the id of the item in the inventory
   * @param quantity the amount of that item
   * @return the id of the inventory
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public long insertInventoryHelper(int itemId, int quantity) throws DatabaseInsertException,
      SQLException, BadInputException {
    // Check for bad input, and insert item to teh inventory.
    if (quantity < 0) {
      throw new DatabaseInsertException();
    } else if (this.verifyItemId(itemId)) {
      long inventoryId = this.insertInventory(itemId, quantity);
      return inventoryId;
    } else {
      throw new DatabaseInsertException();
    }

  }

  /**
   * Inserts a sale into the database.
   * @param userId the id of the user buying
   * @param totalPrice the price of the item on sale
   * @return the id of the sale
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public long insertSaleHelper(int userId, BigDecimal totalPrice) throws
      DatabaseInsertException, SQLException, BadInputException {
    // Check for bad inputs and if good then add the sale.
    if (null == totalPrice || totalPrice.compareTo(new BigDecimal(1)) < -1 
        || !this.verifyUserId(userId)) {
      throw new DatabaseInsertException();
    } else {
      long saleId = this.insertSale(userId, totalPrice);
      return saleId;
    }
  }

  /**
   *  Inserts itemized record for a specific item on sale.
   * @param saleId the id of the sale
   * @param itemId the id of the item
   * @param quantity the amount of the item
   * @return the id of the itemized record
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public long insertItemizedSaleHelper(int saleId, int itemId, int quantity)
      throws DatabaseInsertException, SQLException, BadInputException {
    // Check for bad input and if good add the itemized sale to the database.
    if (!this.verifySaleId(saleId) || !this.verifyItemId(itemId)
        || quantity < 0) {
      throw new DatabaseInsertException();
    } else {
      long itemizedId = this.insertItemizedSale(saleId, itemId, quantity);
      return itemizedId;
    }
  }

  /**
   * chekc if the role exists in the database.
   * @param role given role
   * @return boolean whether the role was found
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public int isRoleInDb(String role) throws SQLException, DatabaseInsertException,
      BadInputException {
    // Create a select database and use it to get all roles
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    List<Integer> roleIds = selectHelper.getRolesHelper();
    // Check if role exists and return the boolean representing such answer.
    for (int roleId : roleIds) {
      if (role.equals(selectHelper.getRoleNameHelper(roleId))) {
        return roleId;
      }
    }
    return -1;
  }


  /**
   * Verify if the inputs meets the requirements.
   * @param userId integer value – auto incrementing
   * @return true if successfully inserted
   * @throws SQLException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public boolean verifyUserId(int userId) throws SQLException, BadInputException {
    // Create a select helper and het a list of users
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    List<User> users = selectHelper.getUsersDetailsHelper();
    List<Integer> userIds = new ArrayList<Integer>();
    // loop through a list of user ids ato check if the given one exists returning the answer.
    for (User user : users) {
      userIds.add(user.getId());
    }
    return (userIds.contains(userId));
  }

  /**
   * Verify if the inputs meets the requirements.
   * @param roleId integer value – auto incrementing
   * @return true if successfully inserted
   * @throws SQLException on failure to close database
   */
  public boolean verifyRoleId(int roleId) throws SQLException {
    // Get a list of role ids from the select helper and check if the role exists, returning bool.
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    ArrayList<Integer> roleIds = (ArrayList<Integer>) selectHelper.getRolesHelper();
    return (roleIds.contains(roleId));
  }

  /**
   * Verify if the inputs meets the requirements.
   * @param saleId integer value – auto incrementing
   * @return true if successfully inserted
   * @throws SQLException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public boolean verifySaleId(int saleId) throws SQLException, BadInputException {
    // Get a list of sales.
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    SalesLog sales = selectHelper.getSalesHelper();
    List<Integer> saleIds = new ArrayList<Integer>();
    // Check if a sale exists and return the bool representing such.
    for (Sale sale : sales.getLog()) {
      saleIds.add(sale.getId());
    }
    return (saleIds.contains(saleId));
  }



  /**
   * Verify if the inputs meets the requirements.
   * @param itemId integer value – auto incrementing
   * @return true if successfully inserted
   * @throws SQLException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public boolean verifyItemId(int itemId) throws SQLException, BadInputException {
    // Get a list of items
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    List<Integer> itemIds = new ArrayList<Integer>();
    List<Item> items = (ArrayList<Item>) selectHelper.getAllItemsHelper();
    // Check if an item exists and if it does then return bool showing as such.
    for (Item item : items) {
      itemIds.add(item.getId());
    }
    return (itemIds.contains(itemId));
  }

  /**
   * Insert account into database.
   * @param userId the id of the user
   * @return the account id
   * @throws SQLException on sql error
   * @throws DatabaseInsertException on insertion error
   * @throws BadInputException on failure to find given information in database
   */
  public long insertAccountHelper(int userId) throws SQLException, DatabaseInsertException,
      BadInputException  {
    // Check input.
    if (!this.verifyUserId(userId)) {
      throw new DatabaseInsertException();
    }
    // Add the account to database.
    long acctId = this.insertAccount(userId, true);
    return acctId;
  }

  /**
   * Inserts account line into the database.
   * 
   * @param accountId the id of the account
   * @param itemId the item to add to the line
   * @param quantity the quantity of the item
   * @return the account id
   * @throws SQLException on sql error
   * @throws DatabaseInsertException on insertion error
   * @throws BadInputException on failure to find given information in database
   **/
  public long insertAccountLineHelper(int accountId, int itemId, int quantity)
      throws SQLException, DatabaseInsertException, BadInputException {
    // Check for bad input.
    if (!this.verifyItemId(itemId) || quantity < 0) {
      throw new DatabaseInsertException();
    }
    // Insert the account line.
    long acctId = this.insertAccountLine(accountId, itemId, quantity);
    return acctId;
  }

}
