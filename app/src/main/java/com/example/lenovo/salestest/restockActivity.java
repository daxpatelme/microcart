package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.store.EmployeeInterface;
import com.b07.users.Admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class restockActivity extends AppCompatActivity {

    private Intent intent;
    private EmployeeInterface adminInterface;
    DatabaseSelectHelper dbSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);
        this.intent = getIntent();
        this.adminInterface = new EmployeeInterface(this, InventoryImpl.getInstance());
        displayItems();
    }

    protected void displayItems(){
        try{
            dbSelector = new DatabaseSelectHelper(this);
        TextView text = (TextView) findViewById(R.id.textShowItems);
        List<Item> items = new ArrayList<Item>();
        items = dbSelector.getAllItemsHelper();
        StringBuilder sb = new StringBuilder();
        sb.append("The following items are in stock:"+ "\n");
        for (Item item : items) {
            int quantity = dbSelector.getInventoryQuantityHelper(item.getId());
            sb.append("Item: " + item.getName() + " Price: " + item.getPrice() + " Quantity in Stock: " + quantity + "\n");
        }
        text.setText(sb.toString());}
        catch (Exception e){e.printStackTrace();}

    }

    protected void restock(View view) {
        try {
        EditText itemNameInput = (EditText) findViewById(R.id.inputRestockItem);
        String itemName =  itemNameInput.getText().toString();
        EditText quantityInput = (EditText) findViewById(R.id.inputRestockItemQuantity);
        int quantity =  Integer.parseInt(quantityInput.getText().toString());
            adminInterface.restockInventory(itemName, quantity);
            Toast.makeText(this, "The item has been restocked", Toast.LENGTH_SHORT).show();
            displayItems();
        } catch (Exception e) {
            Toast.makeText(this, "Please restock with a valid item name and quantity", Toast.LENGTH_SHORT).show();
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
