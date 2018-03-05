package com.b07.database.helper;

import android.content.Context;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Roles;
import java.math.BigDecimal;
import java.sql.SQLException;

public class DatabaseUpdateHelper extends DatabaseDriverAndroid {
  private Context context;

  public DatabaseUpdateHelper(Context context) {
    super(context);
    this.context = context;
  }
  /**
   * update the role names.
   * @return bool value whether the update occurred.
   * @param name The name of the role. This should come from your enumerator.
   * @param id the roleId
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public boolean updateRoleNameHelper(String name, int id)
      throws SQLException, BadInputException, DatabaseInsertException {
    boolean stringExist = false;
    // Create a inserter helper to use the needed methods and check if input is correct.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    for (Roles e : Roles.class.getEnumConstants()) {
      if (name == e.toString()) {
        stringExist = true;
      }
    }
    // Further checks of correct inputs.
    if (!insertHelper.verifyRoleId(id) || !stringExist ||
            insertHelper.isRoleInDb(name) != -1) {
      throw new BadInputException();
    // Update the user role.
    } else {
      boolean complete = this.updateRoleName(name, id);
      return complete;
    }
  }

  /**
   * update the user names.
   * @return bool value whether the update occurred.
   * @param name The name of the user
   * @param userId the userId
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateUserNameHelper(String name, int userId) throws SQLException,
      BadInputException {
    // Check for bad input and if good update the user name.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (null == name || name.length() < 2 || !insertHelper.verifyUserId(userId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateUserName(name, userId);
      return complete;
    }
  }

  /**
   * update the user age.
   * @return bool value whether the update occurred.
   * @param age The age of the user
   * @param userId the userId
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateUserAgeHelper(int age, int userId) throws SQLException, BadInputException {
    // Create a database object to use and verify if the input is valid, if so update the age.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (age < 1 || !insertHelper.verifyUserId(userId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateUserAge(age, userId);
      return complete;
    }
  }

  /**
   * update the user address.
   * @return bool value whether the update occurred.
   * @param address The address of the user
   * @param userId the userId
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateUserAddressHelper(String address, int userId) throws SQLException,
          BadInputException {
    // Check for bad input with the methods found in insert and if goo update address.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (null == address || address.length() > 100 || !insertHelper.verifyUserId(userId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateUserAddress(address, userId);
      return complete;
    }
  }

  /**
   * update the user role.
   * @return bool value whether the update occurred.
   * @param roleId The roleId
   * @param userId the userId
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateUserRoleHelper(int roleId, int userId) throws SQLException,
      BadInputException {
    // Check if the given information is correct, and if so update the users role
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (!insertHelper.verifyRoleId(roleId) || !insertHelper.verifyUserId(userId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateUserRole(roleId, userId);
      return complete;
    }
  }

  /**
   * update the item name.
   * @return bool value whether the update occurred.
   * @param name The item name
   * @param itemId the item id
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateItemNameHelper(String name, int itemId) throws SQLException,
      BadInputException {
    // Check for proper input and if good update the name of the item.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (null == name || name.length() < 2 || !insertHelper.verifyItemId(itemId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateItemName(name, itemId);
      return complete;
    }
  }

  /**
   * update the item price.
   * @return bool value whether the update occurred.
   * @param price The item price
   * @param itemId the item id
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateItemPriceHelper(BigDecimal price, int itemId) throws SQLException,
      BadInputException {
    // Check for bad input with the inserter methods and if good update the price of an item.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (price.compareTo(new BigDecimal(1)) < -1 || !insertHelper.verifyItemId(itemId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateItemPrice(price, itemId);
      return complete;
    }
  }

  /**
   * update the item name.
   * @return bool value whether the update occurred.
   * @param quantity The item quantity
   * @param itemId the item id
   * @throws SQLException when can't get information or close the database
   * @throws BadInputException when parameters does not meet requirements
   */
  public boolean updateInventoryQuantityHelper(int quantity, int itemId) throws SQLException,
      BadInputException {
    // Check for good input and if good then update the inventory quantity.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    if (quantity < 0 || !insertHelper.verifyItemId(itemId)) {
      throw new BadInputException();
    } else {
      boolean complete = this.updateInventoryQuantity(quantity, itemId);
      return complete;
    }
  }

  /**
   * Update the status of account.
   * @param accountId account to be affected
   * @param active new active level of account
   * @return bool value whether the update occurred.
   */
  public boolean updateAccountStatusHelper(int accountId ,boolean active){
    return updateAccountStatus(accountId, active);
  }

  /**
   * Remove all rows of a cart so that it can be refreshed with correct items.
   * @param accountId id of account to be affected.
   * @return bool value whether the update occurred.
   */
  public boolean removeCart(int accountId){
    return this.deleteCart(accountId);
  }

  /**
   * Remove the discount of the user so that it can be considered claimed.
   * @param userId id of user to lose discount.
   */
  public void removeDiscountHelper(int userId){
    this.removeDiscount(userId);
  }
}
