package com.uttej.oraclereadersclub.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Login.LoginActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;
import com.uttej.oraclereadersclub.Utils.GridImageAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "BooksCatalogActivity";
    private int NUM_GRID_COLUMNS = 2;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupFirebaseAuth();

        setupBottomNavigationView();
        tempGridInflator();
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
                                break;
                            case R.id.requests:
                                Intent toRequests = new Intent(HomeActivity.this, RequestsActivity.class);
                                startActivity(toRequests);
                                break;
                            case R.id.userProfile:
                                Intent toProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                                startActivity(toProfile);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    private void tempGridInflator(){
        ArrayList<String> imgURLS = new ArrayList<>();
        imgURLS.add("https://upload.wikimedia.org/wikipedia/commons/b/b4/JPEG_example_JPG_RIP_100.jpg");
        imgURLS.add("http://www.personal.psu.edu/oeo5025/jpg.jpg");
        imgURLS.add("https://upload.wikimedia.org/wikipedia/commons/4/41/Sunflower_from_Silesia2.jpg");
        imgURLS.add("https://upload.wikimedia.org/wikipedia/commons/1/1e/Stonehenge.jpg");

        setupImageGridView(imgURLS);
    }

    /*
        displaying images in a grid view
     */
    private void setupImageGridView(ArrayList<String> imgURLs){
        GridView gridView = (GridView)findViewById(R.id.imagesGrid);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(HomeActivity.this, R.layout.layout_grid_image_view, "", imgURLs);
        gridView.setAdapter(adapter);
    }

    //----------------------------FIREBASE----------------------------------

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUserStatus(user);
            }
        };
    }

    private void checkCurrentUserStatus(FirebaseUser user){
        if(user == null){
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUserStatus(mAuth.getCurrentUser());
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
