package com.b07.database.helper;

import android.content.Context;
import android.database.Cursor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.User;

public class UserFactory{
  private Context context;


  public UserFactory(Context context){
    this.context = context;
  }

  /**
   * Create a user
   * @param name string name of the person
   * @param age int age of the person
   * @param address string address of the person
   * @param password spring password of a person
   * @param type string type of a person
   * @return int representation of the users new id
   * @throws SQLException on failure to close database
   * @throws DatabaseInsertException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public long createUser(String name, int age, String address,String password, String type)
      throws SQLException, BadInputException, DatabaseInsertException {
    // Initialize a helper so that they can bue used in the method.
    DatabaseInsertHelper insertHelper = new DatabaseInsertHelper(context);
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    // if the user is admin then Create a user with role id admin in the database returning user id.
    if (type == "Admin") {
      long userId = (int) insertHelper.insertNewUserHelper(name, age, address, password);
      for (int e : selectHelper.getRolesHelper())
        if (selectHelper.getRoleNameHelper(e).equals("ADMIN")) {
          insertHelper.insertUserRoleHelper((int) userId, e);
        }
      return userId;
    // if the user is customer then Create a user with role id customer in the database returning
    // user id.
    } else if (type == "Customer") {
      long userId = insertHelper.insertNewUserHelper(name, age, address, password);
      for (int e : selectHelper.getRolesHelper())
        if (selectHelper.getRoleNameHelper(e).equals("CUSTOMER")) {
          insertHelper.insertUserRoleHelper((int) userId, e);
        }
      return userId;
    // Else if a bad id is given then just return -1 to show no user was created
    } else {
      return -1;
    }
  }

  /**
   * Create the user object.
   * @param results the cursor pointing to the user info
   * @return User object containing the given user.
   * @throws SQLException on failure to close database
   * @throws BadInputException on failure to find given information in database
   */
  public User createUser(Cursor results) throws SQLException, BadInputException {
    // Construct a user according to the type.
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    User user = null;
    int userId = results.getColumnIndex("ID");
    int userRoleId = selectHelper.getUserRoleIdHelper(results.getInt(userId));
    // Try get the role from the Role Table.
    String role = selectHelper.getRoleNameHelper(userRoleId);
    int name = results.getColumnIndex("NAME");
    int age = results.getColumnIndex("AGE");
    int address = results.getColumnIndex("ADDRESS");
    // Create either the admin object or customer object and return it.
    switch (role) {
      case "ADMIN":
        user = new Admin(context,(int) results.getLong(userId), results.getString(name),
                results.getInt(age), results.getString(address));
        break;
      case "CUSTOMER":
        user = new Customer(context,(int) results.getLong(userId), results.getString(name),
              results.getInt(age), results.getString(address));
        break;
      default:
        break;
    }
    return user;
  }
}
