package com.uttej.oraclereadersclub.AddBooks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.Login.LoginActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.FirebaseMethods;
import com.uttej.oraclereadersclub.Utils.UniversalmageLoader;

/**
 * Created by Clean on 27-03-2018.
 */

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseMethods firebaseMethods;

    private String mAppend = "";
    private int imageCount = 0;

    private EditText mBookTitle;
    private String imgUrl;

    private Intent intent;

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        initWidgets();
        setupFirebaseAuth();

        firebaseMethods = new FirebaseMethods(NextActivity.this);
    }

    private void initWidgets(){

        mBookTitle = (EditText) findViewById(R.id.nextBookTitle);

        ImageView closeNext = (ImageView) findViewById(R.id.nextBackArrow);
        closeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView confirmShare = (TextView) findViewById(R.id.nextShare);
        confirmShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NextActivity.this, "Attempting to upload your book cover", Toast.LENGTH_SHORT).show();
                String bookTitle = mBookTitle.getText().toString();

                if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    firebaseMethods.uploadNewPhoto(NextActivity.this.getString(R.string.new_photo), bookTitle, imageCount, null, bitmap);
                }

            }
        });

        setImage();
    }


    /*
    gets the image url from incoming intent and displays the chosen image
     */
    private void setImage(){
        intent = getIntent();
        ImageView image = (ImageView)findViewById(R.id.nextImageShare);
//        if image from gallery, which is not yet implemented
//        imgUrl = intent.getStringExtra(getString(R.string.selected_image));
//        UniversalmageLoader.setImage(imgUrl, image, null, mAppend);
        if(intent.hasExtra(getString(R.string.selected_bitmap))){
            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            image.setImageBitmap(bitmap);
        }
    }

    /*  Process to upload photo to Firebase
    Create a data model for photos
    Add properties to Photo objects (genres, image_uri, photo_id, user_id)
    Count the number of photos the user already has
        Upload photo to storage
        insert into global photos and user photos node
     */


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

                }
            }
        };

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageCount = firebaseMethods.getImageCount(dataSnapshot);
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
