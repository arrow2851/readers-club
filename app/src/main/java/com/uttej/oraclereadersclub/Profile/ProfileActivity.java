package com.uttej.oraclereadersclub.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;

/**
 * Created by Clean on 25-03-2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupBottomNavigationView();
    }

    /*
    adding the bottom navigation view
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.userProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addBooks:
                                Intent toAddBooks = new Intent(ProfileActivity.this, AddBooksActivity.class);
                                startActivity(toAddBooks);
                                finish();
                                break;
                            case R.id.booksCatalog:
                                Intent toHome = new Intent(ProfileActivity.this, HomeActivity.class);
                                startActivity(toHome);
                                finish();
                                break;
                            case R.id.requests:
                                Intent toRequests = new Intent(ProfileActivity.this, RequestsActivity.class);
                                startActivity(toRequests);
                                finish();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                }
        );
    }
}