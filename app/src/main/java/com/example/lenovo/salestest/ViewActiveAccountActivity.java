package com.example.lenovo.salestest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.inventory.InventoryImpl;
import com.b07.store.Account;
import com.b07.store.EmployeeInterface;
import com.b07.users.Admin;
import com.b07.users.User;

import java.util.ArrayList;
import java.util.List;

public class ViewActiveAccountActivity extends AppCompatActivity {


    Intent intent;
    int adminId;
    EmployeeInterface adminInterface;
    TextView text;
    DatabaseSelectHelper selectHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_active_account);
        this.intent = getIntent();
        this.adminId = (int) intent.getSerializableExtra("adminId");
        this.adminInterface = new EmployeeInterface(this, InventoryImpl.getInstance());
        this.selectHelper = new DatabaseSelectHelper(this);
    }

    protected void viewAc (View view) {
        try {
            EditText userIdInputField = (EditText) findViewById(R.id.inputCustomerId1);
            int cusId = Integer.parseInt(userIdInputField.getText().toString());
            TextView text = (TextView) findViewById(R.id.textAcAccounts);
            StringBuilder sb = new StringBuilder();
            List<Account> list = new ArrayList<Account>();
            User cus = selectHelper.getUserDetailsHelper(cusId);

            list = adminInterface.getActiveAccounts(cusId);
            sb.append("The Following Account Id is The Active Account Associated with" +
                    " the customer " + cus.getName() +"\n" + "\n");
            for (Account account : list) {
                sb.append("Account Id: " + account.getAccountId() + "\n");
            }
            if (list.size() == 0) {
                sb.append("NO ACTIVE ACCOUNT");
            }

            text.setText(sb.toString());
        } catch (Exception e) {
            Toast.makeText(this, "Whoops! Something Went Wrong. Try Again" +
                    "With A Valid Customer Id", Toast.LENGTH_SHORT).show();
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
