package com.b07.users;

import android.content.Context;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;

/**
 * Customer reprehension as class.
 */
public class Customer extends User {
  // Initialize fields.
  private final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(10);
  private BigDecimal discount;
  private Context context;

  /**
   * create customer just to pass context.
   * @param context context passed from activity
   */
  public Customer(Context context) {
    this.context = context;
  }

  /**
   * Construct the user.
   * @param context context which is passed from activity
   * @param id int id of the user
   * @param name String name of the user
   * @param age int age of teh user
   * @param address String address of user
   * @throws SQLException on sql error
   * @throws BadInputException on failure to find given information in database
   */
  public Customer(Context context, int id, String name, int age, String address) throws SQLException,
     BadInputException {
    this.context = context;
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId();
    setDiscount();
  }

  /**
   * Set the role Id for the customer
   * @throws SQLException on sql error
   * @throws BadInputException on failure to find given information in database
   */
  private void setRoleId() throws SQLException, BadInputException {
    // Find the customer role id and set it to be the customers.
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    List<Integer> roleIds = selectHelper.getRolesHelper();
    for (int roleId : roleIds) {
      if (selectHelper.getRoleNameHelper(roleId).equalsIgnoreCase(Roles.CUSTOMER.toString())) {
        this.roleId = roleId;
      }
    }
  }

  /**
   * Set the users discount.
   * @throws SQLException on sql error
   * @throws BadInputException on failure to find given information in database
   */
  private void setDiscount() throws SQLException, BadInputException {
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    this.discount = selectHelper.getDiscountHelper(this.getId());
  }

  /**
   * Get the current discount for user.
   * @return BigDecimal discount for user.
   */
  public BigDecimal getDiscount(){
    return this.discount;
  }

  /**
   * Give the user a discount.
   * @throws SQLException on sql error
   * @throws DatabaseInsertException on insertion error
   * @throws BadInputException on failure to find given information in database
   */
  public void insertDiscount()throws SQLException, BadInputException, DatabaseInsertException {
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    insertHelper.insertDiscountHelper(this.getId(), this.DEFAULT_DISCOUNT);
  }
}
