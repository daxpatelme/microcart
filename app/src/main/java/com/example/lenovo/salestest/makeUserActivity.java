package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.UserFactory;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.users.Admin;

import java.sql.SQLException;

public class makeUserActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_user);
        this.intent = getIntent();
    }

    protected void makeUser(View view) {
        try {
        EditText adminNameInput = (EditText) findViewById(R.id.inputName);
        String name =  adminNameInput.getText().toString();
        EditText adminAgeInput = (EditText) findViewById(R.id.inputAge);
        int age =  Integer.parseInt(adminAgeInput.getText().toString());
        EditText adminAddressInput = (EditText) findViewById(R.id.inputAddress);
        String address =  adminAddressInput.getText().toString();
        EditText adminPwInput = (EditText) findViewById(R.id.inputPassword);
        String pw = adminPwInput.getText().toString();

        UserFactory factory = new UserFactory(this);
            RadioButton cusRadioButton = (RadioButton) findViewById(R.id.radioCus);
            RadioButton adminRadioButton = (RadioButton) findViewById(R.id.radioAdmin);
            if(cusRadioButton.isChecked()){
                long userId = factory.createUser(name,age,address,pw,"Customer");
                Toast.makeText(this, "The User Has Been Created", Toast.LENGTH_SHORT).show();
                TextView text = (TextView) findViewById(R.id.showUserInfo);
                text.setText("Your user id is:" + userId);
            } else if (adminRadioButton.isChecked()){
                long userId = factory.createUser(name,age,address,pw,"Admin");
                Toast.makeText(this, "The User Has Been Created", Toast.LENGTH_SHORT).show();
                TextView text = (TextView) findViewById(R.id.showUserInfo);
                text.setText("Your user id is:" + userId);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong, Try Again With Valid Input", Toast.LENGTH_SHORT).show();
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
