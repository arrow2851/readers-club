package com.uttej.oraclereadersclub.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.Login.LoginActivity;
import com.uttej.oraclereadersclub.Models.User;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;
import com.uttej.oraclereadersclub.Utils.FirebaseMethods;

import org.w3c.dom.Text;

/**
 * Created by Clean on 25-03-2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    FirebaseMethods firebaseMethods;
    
    private TextView mSignout;
    private TextView profileUsername;
    private TextView profileRollnumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupFirebaseAuth();
        firebaseMethods = new FirebaseMethods(ProfileActivity.this);
        initWidgets();

        setupBottomNavigationView();
    }

    void initWidgets(){
        profileUsername = (TextView)findViewById(R.id.profileName);
        profileRollnumber = (TextView)findViewById(R.id.profileRollnumber);



        mSignout = (TextView) findViewById(R.id.profileSignout);
        mSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });
    }

    void setProfileWidgets(User user){
        profileUsername.setText(user.getUsername());
        profileRollnumber.setText(user.getRollnumber());
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

//    ------------------------FIREBASE------------------------

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrive user information from the database
                setProfileWidgets(firebaseMethods.getUserInformation(dataSnapshot));

                //retrieve images from the user
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}