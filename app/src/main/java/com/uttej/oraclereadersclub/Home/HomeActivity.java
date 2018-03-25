package com.uttej.oraclereadersclub.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Home/BooksCatalogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupBottomNavigationView();
    }

    /*
    adding the bottom navigation view
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.booksCatalog);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addBooks:
                                Intent toAddBooks = new Intent(HomeActivity.this, AddBooksActivity.class);
                                startActivity(toAddBooks);
                                finish();
                                break;
                            case R.id.requests:
                                Intent toRequests = new Intent(HomeActivity.this, RequestsActivity.class);
                                startActivity(toRequests);
                                finish();
                                break;
                            case R.id.userProfile:
                                Intent toProfile = new Intent(HomeActivity.this, ProfileActivity.class);
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
