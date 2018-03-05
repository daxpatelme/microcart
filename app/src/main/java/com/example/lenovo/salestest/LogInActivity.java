package com.example.lenovo.salestest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import com.b07.inventory.InventoryImpl;
import com.b07.security.Authenticate;
import com.b07.store.EmployeeInterface;
import com.b07.store.ShoppingCart;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.User;

import java.io.Serializable;
import java.sql.SQLException;

public class LogInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    protected void Login(View view) {
        try {
        EditText userIdInputField = (EditText) findViewById(R.id.inputUserId);
        int userId =  Integer.parseInt(userIdInputField.getText().toString());
        EditText userPwInputField = (EditText) findViewById(R.id.inputPassword);
        String pw = userPwInputField.getText().toString();
        Authenticate au = new Authenticate(this);
        DatabaseSelectHelper dbSelector = new DatabaseSelectHelper(this);
            if (au.authenticate(userId, pw) == true) {
                int roleId = dbSelector.getUserRoleIdHelper(userId);
                if (dbSelector.getRoleNameHelper(roleId).equals("ADMIN")) {
                    InventoryImpl inventory = InventoryImpl.getInstance();
                    Intent intent = new Intent(this, adminActivity.class);
                    intent.putExtra("adminId", userId);
                    startActivity(intent);
                } else if (dbSelector.getRoleNameHelper(roleId).equals("CUSTOMER")) {
                    Intent intent = new Intent(this, userActivity.class);
                    intent.putExtra("cusId", userId);
                    startActivity(intent);
                }
            } else {
                TextView text = (TextView) findViewById(R.id.loginInfo);
                text.setText("User id and password doesn't match");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please log in with a valid" +
                    " user Id and user password.", Toast.LENGTH_SHORT).show();
        }
    }
}

