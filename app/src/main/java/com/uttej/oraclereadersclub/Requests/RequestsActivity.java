package com.uttej.oraclereadersclub.Requests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;

/**
 * Created by Clean on 25-03-2018.
 */

public class RequestsActivity extends AppCompatActivity {
    private static final String TAG = "RequestsActivity";

    private ListView requestsListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        requestsListView = (ListView) findViewById(R.id.requestsListView);


        setupBottomNavigationView();
    }

    /*
    adding the bottom navigation view
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.requests);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addBooks:
                                Intent toAddBooks = new Intent(RequestsActivity.this, AddBooksActivity.class);
                                startActivity(toAddBooks);
                                finish();
                                break;
                            case R.id.booksCatalog:
                                Intent toHome = new Intent(RequestsActivity.this, HomeActivity.class);
                                startActivity(toHome);
                                finish();
                                break;
                            case R.id.userProfile:
                                Intent toProfile = new Intent(RequestsActivity.this, ProfileActivity.class);
                                startActivity(toProfile);
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
