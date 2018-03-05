package com.example.lenovo.salestest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.UserFactory;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;

import java.sql.SQLException;

public class firstAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_admin);
    }

    protected void createFirstAdmin(View view) {
        try {
        EditText adminNameInput = (EditText) findViewById(R.id.inputAdminName);
        String adminName =  adminNameInput.getText().toString();
        EditText adminAgeInput = (EditText) findViewById(R.id.inputAdminAge);
        int adminAge =  Integer.parseInt(adminAgeInput.getText().toString());
        EditText adminAddressInput = (EditText) findViewById(R.id.inputAdminAddress);
        String adminAddress =  adminAddressInput.getText().toString();
        EditText adminPwInput = (EditText) findViewById(R.id.inputAdminPw);
        String adminPw = adminPwInput.getText().toString();
        UserFactory factory = new UserFactory(this);
            long userId = factory.createUser(adminName,adminAge,adminAddress,adminPw,"Admin");
            Toast.makeText(this, "The user has been created", Toast.LENGTH_SHORT).show();
            TextView text = (TextView) findViewById(R.id.showFirstAdminInfo);
            text.setText("Your user id is:" + userId);
        } catch (Exception e) {
            Toast.makeText(this, "There was a problem making your admin." +
                    " Please try again with valid inputs", Toast.LENGTH_SHORT).show();
        }
    }

    protected void goBackIni(View view) {
        Intent intent = new Intent(this, InitializeActivity.class);
        startActivity(intent);
    }
}
