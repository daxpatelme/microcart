package com.b07.users;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.database.serialization.Deserialization;
import com.b07.database.serialization.Serialization;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.ConnectionFailedException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Item;
import com.b07.store.Account;
import com.b07.store.Sale;
import com.b07.store.SalesLog;

/**
 * Class representation of the admin
 */
public class Admin extends User {
  // Initialize the base fields
  private Context context;
  private DatabaseSelectHelper selectHelper;
  private Serialization ser;
  private Deserialization deSer;
  SQLiteDatabase sqLiteDatabase;

  /**
   * Construct the Admin.
   * @param context context which is passed from activity
   * @param id int id of the user
   * @param name String name of the user
   * @param age int age of teh user
   * @param address String address of user
   * @throws SQLException on sql error
   * @throws BadInputException on failure to find given information in database
   */
  public Admin(Context context, int id, String name, int age, String address) throws SQLException,
      BadInputException {
    // Set all variables.
    this.context = context;
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId();
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
      if (selectHelper.getRoleNameHelper(roleId).equalsIgnoreCase(Roles.ADMIN.toString())) {
        this.roleId = roleId;
      }
    }
  }

  /**
   * Get the serialized database.
   * @return string serialized database
   * @throws IOException IO error
   * @throws SQLException SQL error
   */
  public String serializeDatabase() throws IOException, SQLException {
    ser = new Serialization(context);
    return ser.serializeDatabase();
  }

  /**
   * Deserializes the database_copy.ser file, clears the current db content,
   * and reconstructs the database after clearing it.
   * @throws IOException io error
   * @throws SQLException on sql error
   */

  public String deserializeDatabase() throws IOException, SQLException, ClassNotFoundException, ConnectionFailedException {
    deSer = new Deserialization(context);
    return deSer.deserializeDatabase();
  }


}
