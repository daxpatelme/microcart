package com.example.lenovo.salestest;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.b07.database.DatabaseDriverAndroid;

import java.io.File;


public class startActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File("/data/data/com.your.apps.package/databases/inventorymgmt.db");
        if(file.exists()) {
            Intent intent = getIntent();
            intent.setClassName("com.example.lenovo.salestest",
                    "com.example.lenovo.salestest.LogInActivity");
            startActivity(intent);
        } else {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(this);
            Intent intent = getIntent();
            intent.setClassName("com.example.lenovo.salestest",
                    "com.example.lenovo.salestest.InitializeActivity");
            startActivity(intent);
        }
    }
}
