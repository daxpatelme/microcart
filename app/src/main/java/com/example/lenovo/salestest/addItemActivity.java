package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.InventoryImpl;
import com.b07.store.EmployeeInterface;
import com.b07.users.Admin;

import java.math.BigDecimal;
import java.sql.SQLException;

public class addItemActivity extends AppCompatActivity {

    Intent intent;
    EmployeeInterface adminInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        this.intent = getIntent();
        this.adminInterface = new EmployeeInterface(this, InventoryImpl.getInstance());
    }

    protected void addItem(View view) {
        try {
        EditText itemNameInput = (EditText) findViewById(R.id.inputItemName);
        String itemName =  itemNameInput.getText().toString();
        EditText userIdInputField = (EditText) findViewById(R.id.inputItemPrice);
        BigDecimal price =  new BigDecimal(userIdInputField.getText().toString());
            adminInterface.addNewItem(itemName, price);
            Toast.makeText(this, "The item has been created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Whoops! Something Went Wrong, " +
                    "Please Try Again With Valid Input", Toast.LENGTH_SHORT).show();
        }
    }

    protected void goAdminView(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.adminActivity");
        startActivity(intent);
    }

    protected void logOut(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.LogInActivity");
        startActivity(intent);
    }

}
