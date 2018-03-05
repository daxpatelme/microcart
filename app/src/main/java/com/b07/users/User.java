package com.b07.users;

import com.b07.exceptions.BadInputException;

import java.io.Serializable;

/**
 * User implementation as class.
 */
public abstract class User implements Serializable {
  // Create base Fields.
  private int id;
  private String name;
  private int age;
  private String address;
  protected int roleId;

  /**
   * Get the id of the user
   * @return int user id
   */
  public int getId() {
    return this.id;
  }

  /**
   * Set the user id.
   * @param id int user id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get name of user.
   * @return String name of user
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set name of user.
   * @param name String name of user
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the age of the user.
   * @return int age user.
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Set the age of the user.
   * @param age int age user
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Get the role id of the User.
   * @return int role id of user
   */
  public int getRoleId() {
    return this.roleId;
  }

  /**
   * Get the address of the user.
   * @return String address of user
   */
  public String getAddress() {
    return this.address;
  }

  /**
   * Set the address of the user.
   * @param address String user address
   * @throws BadInputException bad input was given for address
   */
  public void setAddress(String address) throws BadInputException {
    if (address.length() > 100 || address.length() == 0) {
      throw new BadInputException();
    } else {
      this.address = address;
    }
  }


}
