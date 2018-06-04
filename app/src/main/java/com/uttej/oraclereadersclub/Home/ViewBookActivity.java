package com.uttej.oraclereadersclub.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.Login.LoginActivity;
import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.Models.Request;
import com.uttej.oraclereadersclub.Models.User;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.UniversalmageLoader;

import org.w3c.dom.Text;

/**
 * Created by Clean on 28-03-2018.
 */

public class ViewBookActivity extends AppCompatActivity{

    private static final String TAG = "ViewBookActivity";

    //info about the current book selection
    private String bookTitle;
    private String bookImagePath;
    private String bookGenres;
    private String bookId;
    private String ownerId;
    private String inPossessionId;

    private TextView tvBookTitleAndPossession;
    private TextView tvOwnerName;
    private TextView tvBookGenres;
    private ImageView ivBookCover;
    private ProgressBar bookCoverLoadingProgress;
    private ImageView ivBackArrow;
    private Button requestBookButton;

    private DatabaseReference databaseReference;

    private boolean isBookAvailableToBorrow;
    private boolean currentUserBookRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        //setting up widgets
        tvOwnerName                 = (TextView) findViewById(R.id.postOwnerName);
        tvBookTitleAndPossession    = (TextView) findViewById(R.id.postBookTitleAndPossession);
        tvBookGenres                = (TextView) findViewById(R.id.postBookGenres);
        ivBookCover                 = (ImageView) findViewById(R.id.postBookImage);
        ivBackArrow                 = (ImageView) findViewById(R.id.postBackArrow);
        bookCoverLoadingProgress    = (ProgressBar) findViewById(R.id.postBookImageProgress);
        requestBookButton           = (Button) findViewById(R.id.requestBookButton);

        //get information about the book from the previous activity
        bookTitle       = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_title));
        bookImagePath   = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_img_url));
        bookGenres      = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_genres));
        bookId          = getIntent().getStringExtra("book_id");
        ownerId         = getIntent().getStringExtra("owner_id");
        inPossessionId  = getIntent().getStringExtra("in_possession_id");

        Log.d(TAG, "onCreate: IDs " + ownerId + " " + inPossessionId);

        //initialize firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //display information
        setOwnerName(ownerId);
        setInPossesionName(inPossessionId);
        tvBookGenres.setText("Genres: " + bookGenres.replace('#', ' '));

        //load the book image
        UniversalmageLoader.setImage(bookImagePath, ivBookCover, bookCoverLoadingProgress, "");

        //check if book is available to borrow
        if(ownerId.equals(inPossessionId))
            isBookAvailableToBorrow = true;
        else
            isBookAvailableToBorrow = false;

        //check if user already requested the book
        currentUserBookRequest = false;
        checkRequests();

        //set widgets 'on click' listeners
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        requestBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBookAvailableToBorrow){
                    if(currentUserBookRequest){
                        Toast.makeText(ViewBookActivity.this,"Your request is being cancelled.", Toast.LENGTH_SHORT).show();
                        cancelRequest();
                    }
                    else {
                        if(ownerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            Toast.makeText(ViewBookActivity.this, "You cannot request to borrow a book which you own.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addRequest();
                        }
                    }
                }
                else {
                    Toast.makeText(ViewBookActivity.this,"Sorry. The book was lent to another user.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    ---------------------------------SETTING USERNAMES---------------------------------
    private void setOwnerName(String userKey){
        Query query = databaseReference
                .child(getString(R.string.dbname_user_accounts))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    tvOwnerName.setText(singleSnapshot.getValue(User.class).getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setInPossesionName(String userKey){
        Query query = databaseReference
                .child(getString(R.string.dbname_user_accounts))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(userKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    tvBookTitleAndPossession.setText(
                            "Book " +
                            bookTitle +
                            " is in possession of " +
                            singleSnapshot.getValue(User.class).getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    --------------------------END SETTING USERNAMES----------------------------------
    private void checkRequests(){
        Query query = databaseReference
                .child(getString(R.string.dbname_photos))
                .child(bookId)
                .child(getString(R.string.field_requests));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid()
                            .equals(singleSnapshot.getValue(Request.class).getUser_id())){
                        setTrueCurrentUserRequestActive();
                        return;
                    }
                }
                setFalseCurrentUserRequestActive();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //called if the user already requested the book
    private void setTrueCurrentUserRequestActive(){
        currentUserBookRequest = true;
        requestBookButton.setText("Cancel Request");
    }

    private void setFalseCurrentUserRequestActive(){
        currentUserBookRequest = false;
        requestBookButton.setText("Request This Book");
    }

    private void addRequest(){
        String newRequestID = databaseReference.push().getKey();
        Request request = new Request();
        request.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.child(getString(R.string.dbname_photos))
                .child(bookId)
                .child(getString(R.string.field_requests))
                .child(newRequestID)
                .setValue(request);

        Toast.makeText(ViewBookActivity.this,"The request is logged. You will be notified when the owner agrees to lend.", Toast.LENGTH_SHORT).show();

        setTrueCurrentUserRequestActive();
    }

    private void cancelRequest(){
        Query query = databaseReference
                .child(getString(R.string.dbname_photos))
                .child(bookId)
                .child(getString(R.string.field_requests));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    String KeyID = singleSnapshot.getKey();

                    if(singleSnapshot.getValue(Request.class).getUser_id()
                            .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        databaseReference.child(getString(R.string.dbname_photos))
                                .child(bookId)
                                .child(getString(R.string.field_requests))
                                .child(KeyID)
                                .removeValue();
                        Toast.makeText(ViewBookActivity.this,"Your request has been cancelled.", Toast.LENGTH_SHORT).show();
                        setFalseCurrentUserRequestActive();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

