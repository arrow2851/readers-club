package com.uttej.oraclereadersclub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.uttej.oraclereadersclub.Utils.BottomNavigationViewHelper;

/**
 * Created by Clean on 25-03-2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /*
    adding the bottom navigation view
     */
    private void setupBottomNavigationView(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.enableNavigation(ProfileActivity.this, bottomNavigationView);
    }
}
