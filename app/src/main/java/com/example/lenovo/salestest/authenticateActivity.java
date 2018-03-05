package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import com.b07.users.Admin;
import com.b07.security.Authenticate;
import com.b07.users.User;

public class authenticateActivity extends AppCompatActivity {

    private Intent intent;
    private DatabaseSelectHelper selectHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        this.intent = getIntent();
        this.selectHelper = new DatabaseSelectHelper(this);
    }

    public void authenticate_(View view) {
        try {
        EditText userIdInputField = (EditText) findViewById(R.id.inputEmployeeId_2);
        int employeeId =  Integer.parseInt(userIdInputField.getText().toString());
        EditText itemNameInput = (EditText) findViewById(R.id.inputEmployeePw);
        String password =  itemNameInput.getText().toString();

            Authenticate au = new Authenticate(this);
            au.authenticate(employeeId,password);
            User admin = selectHelper.getUserDetailsHelper(employeeId);
            if (selectHelper.getRoleNameHelper(admin.getRoleId()).equalsIgnoreCase("Admin")) {
                Toast.makeText(this, "Authenticate Successful: " +
                        admin.getName() + " Is An Admin", Toast.LENGTH_SHORT).show();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Authenticate Failed", Toast.LENGTH_SHORT).show();
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
