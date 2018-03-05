package com.b07.database.serialization;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.b07.database.DatabaseDriverAndroid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.ConnectionFailedException;


public class Serialization extends DatabaseDriverAndroid {
  private static final String LOG_TAG = "Dir Problem:";
    private Context context;

  public Serialization(Context context){
    super(context);
    this.context = context;
  }
  
  public String serializeDatabase() throws IOException, SQLException {
    File file = new File(this.context.getExternalFilesDir(null), "database_copy.ser");
    FileOutputStream fis = new FileOutputStream(file.getPath());
    ObjectOutputStream oos = new ObjectOutputStream(fis);
    oos.writeObject(this.dbToHashList());
    fis.close();
    oos.close();
    return file.getAbsolutePath();
  }
  
  private List<HashMap<String, List<String>>> dbToHashList() throws SQLException {
    List<HashMap<String, List<String>>> dbMap = new ArrayList<>();
    dbMap.add(getAccountTable());
    dbMap.add(getAccountSummaryTable());
    dbMap.add(getInventoryTable());
    dbMap.add(getItemizedSalesTable());
    dbMap.add(getItemsTable());
    dbMap.add(getRolesTable());
    dbMap.add(getSalesTable());
    dbMap.add(getUserPwTable());
    dbMap.add(getUserRoleTable());
    dbMap.add(getUsersTable());
    dbMap.add(getDiscountTable());
    return dbMap;
  }
  
  /**
   * 0
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getAccountTable() throws SQLException{
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    String sql = "SELECT * FROM ACCOUNT;";
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> idList = new ArrayList<>();
    List<String> userIdList = new ArrayList<>();
    List<String> activeList = new ArrayList<>();
    HashMap<String, List<String>> accountMap = new HashMap<>();

    while(cursor.moveToNext()) {
      idList.add(cursor.getString(cursor.getColumnIndex("ID")));
      userIdList.add(cursor.getString(cursor.getColumnIndex("USERID")));
      activeList.add(cursor.getString(cursor.getColumnIndex("ACTIVE")));
    }
    accountMap.put("ID", idList);
    accountMap.put("USERID", userIdList);
    accountMap.put("ACTIVE", activeList);
    cursor.close();
    return accountMap;
  }
  
  /**
   * 1
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getAccountSummaryTable() throws SQLException{
    Cursor cursor = null;
    try{
      String sql = "SELECT * FROM ACCOUNTSUMMARY;";
      SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
      cursor = sqLiteDatabase.rawQuery(sql, null);
      List<String> accountIdList = new ArrayList<>();
      List<String> itemIdList = new ArrayList<>();
      List<String> quantityList = new ArrayList<>();
      HashMap<String, List<String>> accountMap = new HashMap<>();

      while(cursor.moveToNext()) {
        accountIdList.add(cursor.getString(cursor.getColumnIndex("ACCTID")));
        itemIdList.add(cursor.getString(cursor.getColumnIndex("ITEMID")));
        quantityList.add(cursor.getString(cursor.getColumnIndex("QUANTITY")));
      }
      accountMap.put("ACCTID", accountIdList);
      accountMap.put("ITEMID", itemIdList);
      accountMap.put("QUANTITY", quantityList);
      cursor.close();
      return accountMap;
    } finally {
      if (null != cursor) {
        cursor.close();
      }
    }

  }
  
  /**
   * 2
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getInventoryTable() throws SQLException{
    Cursor cursor = null;
    try{
      String sql = "SELECT * FROM INVENTORY;";
      SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
      cursor = sqLiteDatabase.rawQuery(sql, null);
      List<String> itemIdList = new ArrayList<>();
      List<String> quantityList = new ArrayList<>();
      HashMap<String, List<String>> map = new HashMap<>();

      while(cursor.moveToNext()) {
        itemIdList.add(cursor.getString(cursor.getColumnIndex("ITEMID")));
        quantityList.add(cursor.getString(cursor.getColumnIndex("QUANTITY")));
      }
      map.put("ITEMID", itemIdList);
      map.put("QUANTITY", quantityList);
      return map;
    } finally {
        if(null!=cursor){
          cursor.close();
        }
      }
  }
  
  /**
   * 3
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getItemizedSalesTable() throws SQLException{
    String sql = "SELECT * FROM ITEMIZEDSALES;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> saleIdList = new ArrayList<>();
    List<String> itemIdList = new ArrayList<>();
    List<String> quantityList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      saleIdList.add(cursor.getString(cursor.getColumnIndex("SALEID")));
      itemIdList.add(cursor.getString(cursor.getColumnIndex("ITEMID")));
      quantityList.add(cursor.getString(cursor.getColumnIndex("QUANTITY")));
    }
    map.put("SALEID", saleIdList);
    map.put("ITEMID", itemIdList);
    map.put("QUANTITY", quantityList);
    cursor.close();
    return map;
  }
  
  /**
   * 4
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getItemsTable() throws SQLException{
    String sql = "SELECT * FROM ITEMS;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> idList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> priceList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      idList.add(cursor.getString(cursor.getColumnIndex("ID")));
      nameList.add(cursor.getString(cursor.getColumnIndex("NAME")));
      priceList.add(cursor.getString(cursor.getColumnIndex("PRICE")));
    }
    map.put("ID", idList);
    map.put("NAME", nameList);
    map.put("PRICE", priceList);
    cursor.close();
    return map;
  }
  
  /**
   * 5
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getRolesTable() throws SQLException{
    String sql = "SELECT * FROM ROLES;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> idList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      idList.add(cursor.getString(cursor.getColumnIndex("ID")));
      nameList.add(cursor.getString(cursor.getColumnIndex("NAME")));
    }
    map.put("ID", idList);
    map.put("NAME", nameList);
    cursor.close();
    return map;
  }
  
  /**
   * 6
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getSalesTable() throws SQLException{
    String sql = "SELECT * FROM SALES;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> idList = new ArrayList<>();
    List<String> userIdList = new ArrayList<>();
    List<String> totalPriceList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      idList.add(cursor.getString(cursor.getColumnIndex("ID")));
      userIdList.add(cursor.getString(cursor.getColumnIndex("USERID")));
      totalPriceList.add(cursor.getString(cursor.getColumnIndex("TOTALPRICE")));
    }
    map.put("ID", idList);
    map.put("USERID", userIdList);
    map.put("TOTALPRICE", totalPriceList);
    cursor.close();
    return map;
  }
  
  /**
   * 7
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getUserPwTable() throws SQLException{
    String sql = "SELECT * FROM USERPW;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> userIdList = new ArrayList<>();
    List<String> passList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      userIdList.add(cursor.getString(cursor.getColumnIndex("USERID")));
      passList.add(cursor.getString(cursor.getColumnIndex("PASSWORD")));
    }
    map.put("USERID", userIdList);
    map.put("PASSWORD", passList);
    cursor.close();
    return map;
  }
  
  /**
   * 8
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getUserRoleTable() throws SQLException{
    String sql = "SELECT * FROM USERROLE;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> userIdList = new ArrayList<>();
    List<String> roleIdList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      userIdList.add(cursor.getString(cursor.getColumnIndex("USERID")));
      roleIdList.add(cursor.getString(cursor.getColumnIndex("ROLEID")));
    }
    map.put("USERID", userIdList);
    map.put("ROLEID", roleIdList);
    cursor.close();
    return map;
  }
  
  /**
   * 9
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getUsersTable() throws SQLException{
    String sql = "SELECT * FROM USERS;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> idList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> ageList = new ArrayList<>();
    List<String> addressList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      idList.add(cursor.getString(cursor.getColumnIndex("ID")));
      System.out.print(idList.toString());
      nameList.add(cursor.getString(cursor.getColumnIndex("NAME")));
      ageList.add(cursor.getString(cursor.getColumnIndex("AGE")));
      addressList.add(cursor.getString(cursor.getColumnIndex("ADDRESS")));
    }
    map.put("ID", idList);
    map.put("NAME", nameList);
    map.put("AGE", ageList);
    map.put("ADDRESS", addressList);
    cursor.close();
    return map;
  }

  /**
   * 10.
   * @return
   * @throws SQLException
   */
  private HashMap<String, List<String>> getDiscountTable() throws SQLException{
    String sql = "SELECT * FROM DISCOUNTS;";
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
    List<String> userIdList = new ArrayList<>();
    List<String> discountList = new ArrayList<>();
    HashMap<String, List<String>> map = new HashMap<>();

    while(cursor.moveToNext()) {
      userIdList.add(cursor.getString(cursor.getColumnIndex("USERID")));
      discountList.add(cursor.getString(cursor.getColumnIndex("AMOUNT")));
    }
    map.put("USERID", userIdList);
    map.put("AMOUNT", discountList);
    cursor.close();
    return map;
  }
}
