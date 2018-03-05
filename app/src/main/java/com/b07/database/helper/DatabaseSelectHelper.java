package com.b07.database.helper;

import android.content.Context;
import android.database.Cursor;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.BadInputException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.Account;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Customer;
import com.b07.users.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseSelectHelper extends DatabaseDriverAndroid {
  private Context context;


  public DatabaseSelectHelper(Context context) {
    super(context);
    this.context = context;
  }

  /**
   * get all the roleIds
   * @return the list of the roleIds if successfully selected
   * @throws SQLException when can't get information or close the database
   */
  public List<Integer> getRolesHelper() throws SQLException {
    // get a list of role ids from the cursor gotten in driver android
    Cursor cursor = null;
    try{
      List<Integer> ids = new ArrayList<>();
      cursor = this.getRoles();
      while (cursor.moveToNext()) {
        int value = (int) cursor.getLong(cursor.getColumnIndex("ID"));
        ids.add(value);
      }
      return ids;
    // Make sure that database is closed so that it wont be locked.
    } finally {
      if(null != cursor){
        cursor.close();
      }

    }

  }

  /**
   * Get the discount amount of the user.
   * @return the
   * @param userId userid of a user in database
   * @throws SQLException
   * @throws BadInputException
   */
  public BigDecimal getDiscountHelper(int userId) throws SQLException, BadInputException{
    Cursor cursor = null;
    // Get the amount and convert it to a Bigdecimal returning it.
    try{
      cursor = this.getDiscount(userId);
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
      cursor.moveToFirst();
      if(cursor.getCount()>0){
        String result = cursor.getString(cursor.getColumnIndex("AMOUNT"));

        return new BigDecimal(result);
      } else {
        return new BigDecimal(0);
      }
    // Make sure that cursor is closed so bd wont be locked.
    } finally{
      if(null != cursor){
        cursor.close();
      }
    }

  }

  /**
   * get the role name given roleId.
   * @param roleId the roleId
   * @return the role name if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public String getRoleNameHelper(int roleId) throws SQLException, BadInputException {
    // Get and return the role name or throw the needed exception.
    String role = this.getRole(roleId);
    if (role == null){
      throw new BadInputException();
    } else {
      return role;
    }
  }

  /**
   * get the user role id given userId.
   * @param userId the userId
   * @return the role id if successfully selected
   * @throws BadInputException when parameters does not meet requirements
   */
  public int getUserRoleIdHelper(int userId) throws BadInputException {
    // Get the needed user role id and return it or throw an error
    int roleId = this.getUserRole(userId);
    if (roleId >= 1){
      return roleId;
    } else {
      throw new BadInputException();
    }
  }

  /**
   * get all the user id given roleId.
   * @param roleId the roleId
   * @return the list of user id if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public List<Integer> getUsersByRoleHelper(int roleId) throws SQLException, BadInputException {
    Cursor cursor = null;
    // Get the cursor containing all of the users ids associated witha role id and add the users
    // ids to a list which is returned.
    try{
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
      if (insertHelper.verifyRoleId(roleId)) {
        cursor = this.getUsersByRole(roleId);
        List<Integer> userIds = new ArrayList<>();
        while (cursor.moveToNext()) {
          userIds.add((int) cursor.getLong(cursor.getColumnIndex("USERID")));
        }
        return userIds;
      } else {
        throw new BadInputException();
      }
    // Make sure that the cursor closes
    } finally{
      if(null != cursor){
        cursor.close();
      }
    }

  }

  /**
   * . get all the users
   * @return the list of users if successfully selected
   * @throws SQLException when can't get information or close the database
   */
  public List<User> getUsersDetailsHelper() throws SQLException, BadInputException {
    Cursor cursor = null;
    // Using the cursor containing all user ids create the customer and add it to the list, which
    // is to be returned.
    try{
      UserFactory userFactory = new UserFactory(context);
      cursor = this.getUsersDetails();
      User user = null;
      List<User> users = new ArrayList<>();
      while (cursor.moveToNext()) {
        user = userFactory.createUser(cursor);
        users.add(user);
      }
      return users;
    // Make sure cursor is closed so that database is locked.
    } finally{
      if(null != cursor){
        cursor.close();
      }
    }

  }


  /**
   * get all the user id given roleId.
   * @param userId the userId
   * @return the list of user id if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public User getUserDetailsHelper(int userId) throws SQLException, BadInputException {
    Cursor cursor = null;
    // Using the cursor which contains the row with the given user id create the user and return it
    // else throw an exception.
    try{
      UserFactory userFactory = new UserFactory(context);
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
      if (insertHelper.verifyUserId(userId)) {
        cursor = this.getUserDetails(userId);
        User user = null;
        while (cursor.moveToNext()) {
          user = userFactory.createUser(cursor);
        }
        return user;
      } else {
        throw new BadInputException();
      }
    // Make sure cursor is closed so that database is not locked.
    } finally {
      if(null != cursor) {
        System.out.println("Closed userdetialshelper");
        cursor.close();
      }
    }
  }

  /**
   * get the hashed password of the user from the database.
   * @param userId the user id
   * @return the hashed password of the user
   * @throws BadInputException when parameters does not meet requirements
   */
  public String getPasswordHelper(int userId) throws SQLException, BadInputException {
    // Return the users password if the user exists or throw an error.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (insertHelper.verifyUserId(userId)) {
      String password = this.getPassword(userId);
      return password;
    } else {
      throw new BadInputException();
    }
  }

  /**
   * get all the items.
   * @return the list of items if successfully selected
   * @throws SQLException when can't get information or close the database
   */
  public List<Item> getAllItemsHelper() throws SQLException, BadInputException {
    Cursor cursor = null;
    // Get a cursor with all the item rows and use the get item helper to create the item object.
    try{
      List<Item> items = new ArrayList<>();
      cursor = this.getAllItems();
      while (cursor.moveToNext()) {
        int id = (int) cursor.getLong(cursor.getColumnIndex("ID"));
        Item item = this.getItemHelper(id);
        items.add(item);
      }
      cursor.close();
      return items;
    // Make sure cursor is closed so database is not locked.
    } finally {
      if(null != cursor){
        cursor.close();
      }
    }
  }

  /**
   * get all the user id given roleId.
   * @param itemId the itemId
   * @return the corresponding item if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public Item getItemHelper(int itemId) throws SQLException, BadInputException {
    Cursor results = null;
    // get a cursor with only the row containing th item id and use its information to create
    // an item.
    try {
      results = this.getItem(itemId);
      results.moveToFirst();
      Item item = null;
      int idIndex = results.getColumnIndex("ID");
      int nameIndex = results.getColumnIndex("NAME");
      int priceIndex = results.getColumnIndex("PRICE");
      item = new ItemImpl((int) results.getLong(idIndex),
              results.getString(nameIndex), new BigDecimal(results.getString(priceIndex)));
      return item;
    } catch (Exception e) {
      throw new BadInputException();
    // Make sure the cursor is closed so database is not locked.
    } finally {
      if(null != results){
        results.close();
      }

    }
  }

  /**
   * get all the inventory.
   * @return the inventory if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public Inventory getInventoryHelper() throws SQLException, BadInputException {
    Cursor results = null;
    // Create an inventory which contains th stock of all items from teh rows representing
    // inventory.
    try{
      results = this.getInventory();
      Inventory inventory = InventoryImpl.getInstance();
      HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
      while (results.moveToNext()) {
        int itemIdIndex = results.getColumnIndex("ITEMID");
        int quantityIndex = results.getColumnIndex("QUANTITY");
        Item item = getItemHelper((int) results.getLong(itemIdIndex));
        Integer quantity = results.getInt(quantityIndex);
        itemMap.put(item, quantity);
      }
      inventory.setItemMap(itemMap);
      return inventory;
    // Make sure that the cursor is properly closed.
    } finally {
        if(null!= results){
          results.close();
        }
    }
  }

  /**
   * get inventory quantity given itemId.
   * @param itemId the itemId
   * @return the inventory quantity if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public int getInventoryQuantityHelper(int itemId) throws SQLException, BadInputException {
    // verify the parameter and get the quantity if it exists.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (insertHelper.verifyItemId(itemId)) {
      int quantity = this.getInventoryQuantity(itemId);
      return quantity;
    } else {
      throw new BadInputException();
    }

  }

  /**
   * get all the sales.
   * @return the sales log if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public SalesLog getSalesHelper() throws SQLException, BadInputException {
    Cursor results = null;
    // loop through the given cursor with all the sales and get the sale at each row returning a
    // complete saleslog.
    try{
      results = this.getSales();
      SalesLog saleslog = new SalesLogImpl(context);
      while (results.moveToNext()) {
        int idIndex = results.getColumnIndex("ID");
        Sale sale = this.getSaleByIdHelper((int) results.getLong(idIndex));
        if (sale != null) {
          saleslog.addSale(sale);
        }
      }
      return saleslog;
    // Make sure that the cursor is always closed.
    } finally {
      if(null!=results){
        System.out.println("Closed sales helper");
        results.close();
      }
    }

  }

  /**
   * get the sale given saleId.
   * @param saleId the saleId
   * @return the corresponding sale if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public Sale getSaleByIdHelper(int saleId) throws SQLException, BadInputException {
    Cursor results = null;
    // create the sale based on information in the cursor with the properly formatted variables.
    try{
      results = this.getSaleById(saleId);
      Sale sale = null;
      while (results.moveToNext()) {
        int saleIdIndex = results.getColumnIndex("ID");
        int userIdIndex = results.getColumnIndex("USERID");
        int totalPriceIdIndex = results.getColumnIndex("TOTALPRICE");
        sale = new SaleImpl((int)results.getLong(saleIdIndex),
                this.getUserDetailsHelper((int) results.getLong(userIdIndex)),
                new BigDecimal(results.getString(totalPriceIdIndex)));
      }
      this.getItemizedSaleByIdHelper(saleId, sale);
      return sale;
    // Make sure that the cursor is always closed.
    } finally{
      if(null!=results){
        System.out.println("Closed sale by id");
        results.close();
      }
    }

  }

  /**
   * get all the sales given userId.
   * @param userId the userId
   * @return the list of sales if successfully selected
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public List<Sale> getSalesToUserHelper(int userId) throws SQLException, BadInputException {
    Cursor results = null;
    // get the cursor containing the sales rows made up of those which the user made creating a
    // list of sales with the values.
    try{
      DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
      results = this.getSalesToUser(userId);
      if (insertHelper.verifyUserId(userId)) {

        List<Sale> sales = new ArrayList<>();
        while (results.moveToNext()) {
          int saleIdIndex = results.getColumnIndex("ID");
          Sale sale = this.getSaleByIdHelper((int) results.getLong(saleIdIndex));
          sales.add(sale);
        }

        return sales;
      } else {
        throw new BadInputException();
      }
    // Make sure that the cursor is closed at the end of operation.
    } finally {
      if(null!=results){
        results.close();
      }

    }

  }

  /**
   * get itemized sale given saleId.
   * @param saleId the saleId
   * @param sale the sale
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public void getItemizedSaleByIdHelper(int saleId, Sale sale) throws SQLException,
      BadInputException {
    Cursor results = null;
    // Given a sale and saleid add the itemized information form the given cursor to the sale.
    try {
      results = this.getItemizedSaleById(saleId);
      HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
      while (results.moveToNext()) {
        int itemIdIndex = results.getColumnIndex("ITEMID");
        int quantityIndex = results.getColumnIndex("QUANTITY");
        itemMap.put(this.getItemHelper((int) results.getLong(itemIdIndex)),
                (int) results.getLong(quantityIndex));
      }
      sale.setItemMap(itemMap);
    } catch (NullPointerException e) {
      e.printStackTrace();
    // Make sure that the cursor is closed.
    } finally {
      if(null!= results){
        results.close();
      }
    }
  }

  /**
   * get all the user id given roleId.
   * @param salesLog the salesLog
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public void getItemizedSalesHelper(SalesLog salesLog)
      throws SQLException, BadInputException {
    Cursor results = null;
    // Given a saleslog updated each sale such that it will contain the itmeized information.
    try{
      for (Sale e: salesLog.getLog()) {
        HashMap<Item, Integer> itemMap = new HashMap<Item, Integer>();
        results = this.getItemizedSales();
        while (results.moveToNext()) {
          int saleIdIndex = results.getColumnIndex("ID");
          if (e.getId() == (int) results.getInt(saleIdIndex)) {
            int itemIdIndex = results.getColumnIndex("ITEMID");
            int quantityIndex = results.getColumnIndex("QUANTITY");
            itemMap.put(this.getItemHelper((int)results.getLong(itemIdIndex)),
                    (int) results.getLong(quantityIndex));
          }
          e.setItemMap(itemMap);
        }
      }
    // Make sure that the cursor is closed.
    } finally{
      if(null!=results){
        results.close();
      }
    }
  }

  /**
   *  Hashmap of all items sold.
   * @return HashMap<Item, Integer> of all sold items
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public HashMap<Item, Integer> returnItemizedSales()
      throws SQLException, BadInputException {
    // create and return the hashmap which is the itemized sale information.
    HashMap<Item, Integer> itemizedMap = new HashMap<Item, Integer>();
    Cursor results = null;
    try{
      results = this.getItemizedSales();
      while (results.moveToNext()) {
        int itemIdIndex = results.getColumnIndex("ITEMID");
        Item item = findItem(itemizedMap,(int)results.getLong(itemIdIndex));
        int quantityIndex = results.getColumnIndex("QUANTITY");
        if( item == null){
          itemizedMap.put(this.getItemHelper((int) results.getLong(itemIdIndex)),
                  (int) results.getLong(quantityIndex));

        } else {
          itemizedMap.put(item, itemizedMap.get(item) + (int) results.getLong(quantityIndex));
        }
      }
      return itemizedMap;
    // Make sure cursor is closed
    } finally{
      if(null!=results){
        results.close();
      }
    }

  }

  /**
   * check if an item exists
   * @param map map to search through
   * @param itemId item id to look for
   * @return int item id or null
   */
  private Item findItem(HashMap<Item, Integer> map, Integer itemId) {
    // Loop through and see if the itme is part of the key list returning id if found.
    for(Item i: map.keySet()) {
      if(i.getId() == itemId) {
        return i;
      }
    }
    return null;
  }
  
  /**
   * Get the list of accounts belonging to the user.
   * @param userId int user id
   * @return List<Account> of users accounts
   * @throws SQLException erroe in retreaving through SQLE
   * @throws BadInputException databse contains bad values
   */
  public List<Account> getUserAccountsHelper(int userId)
      throws SQLException, BadInputException {
    Cursor results = null;
    // Using the gotten cursor create a list of all the accounts in the database.
    try{
      results = this.getUserAccounts(userId);
      Customer customer = (Customer) getUserDetailsHelper(userId);
      List<Account> accounts = new ArrayList<Account>();
      while (results.moveToNext()) {
        int accountIDIndex = results.getColumnIndex("ID");
        int accountStatusIndex = results.getColumnIndex("ACTIVE");
        Account account = new Account(context,(int) results.getLong(accountIDIndex), customer,
                results.getInt(accountStatusIndex));

        HashMap<Item, Integer> cart =
                getAccountDetailsHelper((int) results.getLong(accountIDIndex));
        for (Item e : cart.keySet()) {
          account.addItem(e, cart.get(e));
        }
        accounts.add(account);
      }
      return accounts;
    // Make sure that the cursor is closed.
    } finally{
      if(null!=results){
        results.close();
      }
    }

  }

  /**
   * Get the hashmap of account cart info.
   * @param accountId int accoutn id
   * @return HashMap<Item, Integer> of users accounts cart
   * @throws SQLException erroe in retreaving through SQLE
   * @throws BadInputException databse contains bad values
   */
  public HashMap<Item, Integer> getAccountDetailsHelper(int accountId) throws SQLException,
      BadInputException {
    // Loop through the account details and with the cursor and make a hashmap of the users cart.
    Cursor results = null;
    try{
      results = this.getAccountDetails(accountId);
      HashMap<Item, Integer> cartMap = new HashMap<Item, Integer>();
      while (results.moveToNext()) {
        int itemIdIndex = results.getColumnIndex("ITEMID");
        int quantityIndex = results.getColumnIndex("QUANTITY");
        Item newItem = (Item) getItemHelper(results.getInt(itemIdIndex));
        cartMap.put(newItem, results.getInt(quantityIndex));
      }
      return cartMap;
    // Make sure that the cursor is closed.
    } finally{
      if(null!=results){
        results.close();
      }
    }

  }

  /**
   * Gets the item id given the items name.
   * @param name name of item
   * @return int the item id
   */
  public int getItemIdByNameHelper(String name){
    Cursor results = null;
    // Loops through all items checking if the name matches if it does then return the item id.
    try {
      results = this.getItemIdByName(name);
      results.moveToFirst();
      int itemIdIndex = results.getColumnIndex("ID");
      return (int) results.getLong(itemIdIndex);
    } finally {
      if (results != null) {
        results.close();
      }
    }
  }
}
