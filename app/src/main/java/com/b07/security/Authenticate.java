package com.b07.security;

import java.io.Serializable;
import java.sql.SQLException;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import android.content.Context;


public class Authenticate implements Serializable {
  
  private static boolean authenticated;

  /**
   * Check if the correct password was given for the user.
   * 
   * @param password The string representation of the password.
   * @return A boolean which represents if the password was correct.

   */
  private DatabaseSelectHelper selectHelper;
  private Context context;

  /**
   * create authenticate object with needed context
   * @param context
   */
  public Authenticate(Context context){
    this.context = context;
  }

  /**
   * Check if the user authenticated with the proper password.
   * @param id int id of the user
   * @param password string password of the user.
   * @return boolean if the user is authenticated.
   * @throws BadInputException bad information was passed to select methods.
   */
  public boolean authenticate(int id, String password)
      throws BadInputException {
    DatabaseSelectHelper selectHelper = new DatabaseSelectHelper(context);
    // Check if the string is null, if so return false.
    if (null == password) {
      return false;
    }
    // Check if the inputed password is equal to the users password, avoid sql exception.
    try {
      String hashed = selectHelper.getPasswordHelper(id);
      authenticated = PasswordHelpers.comparePassword(hashed, password);
    } catch (SQLException e) {
      // Print the error message.
      e.printStackTrace();
    } catch (BadInputException e) {
      // Print the error message.
      e.printStackTrace();
      // Return the new value of authenticated
    }
    return authenticated;
  }

  public boolean isAuthenticated() {
    return Authenticate.authenticated;
  }

}
