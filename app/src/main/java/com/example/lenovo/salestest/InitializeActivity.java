package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.InventoryImpl;
import com.b07.store.EmployeeInterface;

import java.math.BigDecimal;
import java.sql.SQLException;


public class InitializeActivity extends AppCompatActivity {

    public InitializeActivity() throws SQLException, BadInputException {
    }

    private EmployeeInterface adminInterface;
    private DatabaseInsertHelper insertHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        try {
            insertHelper = new DatabaseInsertHelper(this);
            insertHelper.insertRoleHelper("ADMIN");
            insertHelper.insertRoleHelper("CUSTOMER");
        } catch (DatabaseInsertException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }

    }

    protected void addItemType(View view){
        try {
            EditText itemNameInput = (EditText) findViewById(R.id.initializeItem);
            String itemName =  itemNameInput.getText().toString();
            EditText userIdInputField = (EditText) findViewById(R.id.initializePrice);
            BigDecimal price =  new BigDecimal(userIdInputField.getText().toString());
            adminInterface = new EmployeeInterface(this, InventoryImpl.getInstance());
            adminInterface.addNewItem(itemName, price);
            Toast.makeText(this,"The item has been added",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"There was an error adding your item," +
                    " please try again.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void goFirstAdmin(View view){
        Intent intent = new Intent(this, firstAdminActivity.class);
        startActivity(intent);

    }

    protected void goLogin(View view){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
