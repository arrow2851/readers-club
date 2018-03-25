package com.uttej.oraclereadersclub.AddBooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;

/**
 * Created by Clean on 25-03-2018.
 */

public class AddBooksActivity extends AppCompatActivity {
    private static final String TAG = "AddBooksActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.addBooks);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.requests:
                                Intent toRequests = new Intent(AddBooksActivity.this, RequestsActivity.class);
                                startActivity(toRequests);
                                finish();
                                break;
                            case R.id.booksCatalog:
                                Intent toHome = new Intent(AddBooksActivity.this, HomeActivity.class);
                                startActivity(toHome);
                                finish();
                                break;
                            case R.id.userProfile:
                                Intent toProfile = new Intent(AddBooksActivity.this, ProfileActivity.class);
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
