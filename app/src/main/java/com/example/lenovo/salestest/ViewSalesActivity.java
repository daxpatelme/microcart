package com.example.lenovo.salestest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.InventoryImpl;
import com.b07.store.EmployeeInterface;
import com.b07.users.Admin;

import java.sql.SQLException;

public class ViewSalesActivity extends AppCompatActivity {

    private Intent intent;
    private EmployeeInterface adminInterface;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales);
        this.intent = getIntent();
        adminInterface = new EmployeeInterface(this, InventoryImpl.getInstance());
        TextView text = (TextView) findViewById(R.id.textSales);
        try {
            text.setText(adminInterface.viewBooks());
            text.setGravity(Gravity.CENTER_HORIZONTAL);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
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

