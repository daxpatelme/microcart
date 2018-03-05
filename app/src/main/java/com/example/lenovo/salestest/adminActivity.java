package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//import com.b07.database.serialization.Serialization;
//import com.b07.database.serialization.Deserialization;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.serialization.Serialization;
import com.b07.exceptions.*;

import com.b07.store.EmployeeInterface;
import com.b07.users.Admin;

import java.io.File;
import java.sql.SQLException;

public class adminActivity extends AppCompatActivity {

    Intent intent;
    Admin admin;
    DatabaseSelectHelper dbSelector;
    private final String path = "/storage/emulated/0/Android/data/com.example.lenovo.salestest/files/database_copy.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.intent = getIntent();
        int adminId = (int) intent.getSerializableExtra("adminId");
        dbSelector = new DatabaseSelectHelper(this);
        System.out.println(adminId);
        try{
            this.admin = (Admin) dbSelector.getUserDetailsHelper(adminId);
            //greet admin
            System.out.println(admin.getName());

            TextView text = (TextView) findViewById(R.id.adminView);
            text.setText("Hi " + admin.getName() + " " + "Welcome to the MicroCart Admin Panel");
        } catch(SQLException | BadInputException e){
            e.printStackTrace();
            Toast.makeText(this, "No such admin in DB", Toast.LENGTH_SHORT).show();
        }


    }

    protected void goAddItem(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.addItemActivity");
        startActivity(intent);
    }
    protected void goAuthenticate(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.authenticateActivity");
        startActivity(intent);
    }
    protected void goMakeUser(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.makeUserActivity");
        startActivity(intent);
    }
    protected void goRestock(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.restockActivity");
        startActivity(intent);
    }
    protected void goViewSales(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.ViewSalesActivity");
        startActivity(intent);
    }
    protected void goViewActiveAccount(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.ViewActiveAccountActivity");
        startActivity(intent);
    }
    protected void goViewInactiveAccount(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.ViewInactiveAccountActivity");
        startActivity(intent);
    }

    protected void goCreateAccount(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.makeAccountActivity");
        startActivity(intent);
    }

    //DAX WORKING 12/3/2017 15:52 PM
    protected void goSerializeDatabase(View view) {
        try{
            String location = admin.serializeDatabase();
            System.out.println(location);
            location = "Serialized DB has been stored at: " + location;
            Toast.makeText(this, location, Toast.LENGTH_LONG).show();
        } catch(java.io.IOException | java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    protected void goDeserializeDatabase(View view) {
        try{
            File file = new File(path);
            if(file.exists()){
                // Do something else.
                String location = admin.deserializeDatabase();
                Toast.makeText(this, "Restored DB from: " + location, Toast.LENGTH_SHORT).show();
                intent.setClassName("com.example.lenovo.salestest",
                        "com.example.lenovo.salestest.InitializeActivity");
                startActivity(intent);
                //Toast.makeText(this, location, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "There is no serialized file please press copy database", Toast.LENGTH_LONG).show();
            }

        } catch( ClassNotFoundException | ConnectionFailedException |java.io.IOException | java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    protected void logOut(View view) {
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.LogInActivity");
        startActivity(intent);
    }
}
