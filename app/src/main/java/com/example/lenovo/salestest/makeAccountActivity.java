package com.example.lenovo.salestest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.store.Account;

import java.sql.SQLException;
import java.util.List;

public class makeAccountActivity extends AppCompatActivity {

    Intent intent;
    DatabaseInsertHelper insertHelper;
    DatabaseSelectHelper selectHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        this.intent = getIntent();
    }

    protected void makeAccount(View view) {
        try {
        TextView text = (TextView) findViewById(R.id.showCusAccountInfo);
        EditText cusIdInput = (EditText) findViewById(R.id.inputCusId);
        int cusId = Integer.parseInt(cusIdInput.getText().toString());
            List<Account> accountList = null;
            selectHelper = new DatabaseSelectHelper(this);
            accountList = selectHelper.getUserAccountsHelper(cusId);
            int accountCount = 0;
            for (Account i : accountList) {
                if (i.getActive() == 1) {
                    accountCount += 1;
                    text.setText("The customer already has an active account: Account id is " + i.getAccountId());
                }
            }
            if (accountCount == 0){
                long cusAccountId = 0;
                insertHelper = new DatabaseInsertHelper(this);
                cusAccountId = insertHelper.insertAccountHelper(cusId);
                text.setText("Your account id is:" + cusAccountId);
                Toast.makeText(this, "The account has been created", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "The Account Could Not Be Created." +
                    " Try Again With A Valid Customer Id", Toast.LENGTH_SHORT).show();
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
