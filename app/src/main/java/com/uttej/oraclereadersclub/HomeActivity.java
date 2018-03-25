package com.uttej.oraclereadersclub;

import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home/BooksCatalogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /*
    adding the bottom navigation view
     */
    private void setupBottomNavigationView(){
        BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView)findViewById(R.id.bottomNavigationBar);
    }
}
