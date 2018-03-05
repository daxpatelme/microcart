package com.b07.database.serialization;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.exceptions.ConnectionFailedException;

public class Deserialization extends DatabaseDriverAndroid {
    private final String path = "/storage/emulated/0/Android/data/com.example.lenovo.salestest/files/database_copy.ser";
    private final Context context;
    DatabaseDriverAndroid myDb;
    SQLiteDatabase sqLiteDatabase;

    public Deserialization(Context context){
        super(context);
        this.context = context;
        myDb = new DatabaseDriverAndroid(context);
        sqLiteDatabase = this.getWritableDatabase();
    }

    public String deserializeDatabase() throws IOException, SQLException, ConnectionFailedException, ClassNotFoundException {
        myDb.onUpgrade(sqLiteDatabase,1,1);
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<HashMap<String, List<String>>> dbMap =  (List<HashMap<String, List<String>>>) ois.readObject();
        hashListToDb(dbMap);
        fis.close();
        ois.close();
        return path;
    }

    private void hashListToDb(List<HashMap<String, List<String>>> dbMap) throws SQLException {
        setAccountTable(dbMap.get(0));
        setAccountSummaryTable(dbMap.get(1));
        setInventoryTable(dbMap.get(2));
        setItemizedSalesTable(dbMap.get(3));
        setItemsTable(dbMap.get(4));
        setRolesTable(dbMap.get(5));
        setSalesTable(dbMap.get(6));
        setUserPwTable(dbMap.get(7));
        setUserRoleTable(dbMap.get(8));
        setUsersTable(dbMap.get(9));
        setDiscountTable(dbMap.get(10));
    }

    /**
     * 0
     * @param map
     * @throws SQLException
     */
    private void setAccountTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ID").size()) {
            contentValues = new ContentValues();
            contentValues.put("ID", map.get("ID").get(i));
            contentValues.put("USERID", map.get("USERID").get(i));
            contentValues.put("ACTIVE", map.get("ACTIVE").get(i));
            i++;
            sqLiteDatabase.insert("ACCOUNT", null, contentValues);
        }

        sqLiteDatabase.close();
    }

    /**
     * "
     * @param map
     * @throws SQLException
     */
    private void setAccountSummaryTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ACCTID").size()) {
            contentValues = new ContentValues();
            contentValues.put("ACCTID", map.get("ACCTID").get(i));
            contentValues.put("ITEMID", map.get("ITEMID").get(i));
            contentValues.put("QUANTITY", map.get("QUANTITY").get(i));
            i++;
            sqLiteDatabase.insert("ACCOUNTSUMMARY", null, contentValues);
        }

        sqLiteDatabase.close();
    }

    /**
     * "
     * @param map
     * @throws SQLException
     */
    private void setInventoryTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ITEMID").size()) {
            contentValues = new ContentValues();
            contentValues.put("ITEMID", map.get("ITEMID").get(i));
            contentValues.put("QUANTITY", map.get("QUANTITY").get(i));
            sqLiteDatabase.insert("INVENTORY", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * "
     * @param map
     * @throws SQLException
     */
    private void setItemizedSalesTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ITEMID").size()) {
            contentValues =  new ContentValues();
            contentValues.put("SALEID", map.get("SALEID").get(i));
            contentValues.put("ITEMID", map.get("ITEMID").get(i));
            contentValues.put("QUANTITY", map.get("QUANTITY").get(i));
            sqLiteDatabase.insert("ITEMIZEDSALES", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 4
     * @param map
     * @throws SQLException
     */
    private void setItemsTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues ;
        int i = 0;
        while(i < map.get("ID").size()) {
            contentValues =  new ContentValues();
            contentValues.put("ID", map.get("ID").get(i));
            contentValues.put("NAME", map.get("NAME").get(i));
            contentValues.put("PRICE", map.get("PRICE").get(i));
            sqLiteDatabase.insert("ITEMS", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 5
     * @param map
     * @throws SQLException
     */
    private void setRolesTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ID").size()) {
            contentValues  = new ContentValues();
            contentValues.put("ID", map.get("ID").get(i));
            contentValues.put("NAME", map.get("NAME").get(i));
            sqLiteDatabase.insert("ROLES", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 6
     * @param map
     * @throws SQLException
     */
    private void setSalesTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ID").size()) {
            contentValues  = new ContentValues();
            contentValues.put("ID", map.get("ID").get(i));
            contentValues.put("USERID", map.get("USERID").get(i));
            contentValues.put("TOTALPRICE", map.get("TOTALPRICE").get(i));
            sqLiteDatabase.insert("SALES", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 7
     * @param map
     * @throws SQLException
     */
    private void setUserPwTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("USERID").size()) {
            contentValues  = new ContentValues();
            contentValues.put("USERID", map.get("USERID").get(i));
            contentValues.put("PASSWORD", map.get("PASSWORD").get(i));
            sqLiteDatabase.insert("USERPW", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 8
     * @param map
     * @throws SQLException
     */
    private void setUserRoleTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("USERID").size()) {
            contentValues  = new ContentValues();
            contentValues.put("USERID", map.get("USERID").get(i));
            contentValues.put("ROLEID", map.get("ROLEID").get(i));
            sqLiteDatabase.insert("USERROLE", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

    /**
     * 9
     * @param map
     * @throws SQLException
     */
    private void setUsersTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues;
        int i = 0;
        while(i < map.get("ID").size()) {
            contentValues  = new ContentValues();
            contentValues.put("ID", map.get("ID").get(i));
            contentValues.put("NAME", map.get("NAME").get(i));
            contentValues.put("AGE", map.get("AGE").get(i));
            contentValues.put("ADDRESS", map.get("ADDRESS").get(i));
            sqLiteDatabase.insert("USERS", null, contentValues);
            i++;
        }
        sqLiteDatabase.close();
    }

    private void setDiscountTable(HashMap<String, List<String>> map) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues;
        int i = 0;
        while(i < map.get("USERID").size()) {
            contentValues =  new ContentValues();
            contentValues.put("USERID", map.get("USERID").get(i));
            contentValues.put("AMOUNT", map.get("AMOUNT").get(i));
            sqLiteDatabase.insert("DICOUNTS", null, contentValues);
            i++;
        }

        sqLiteDatabase.close();
    }

}
