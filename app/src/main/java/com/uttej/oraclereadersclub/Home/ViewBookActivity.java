package com.uttej.oraclereadersclub.Home;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.Models.User;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.UniversalmageLoader;

import org.w3c.dom.Text;

/**
 * Created by Clean on 28-03-2018.
 */

public class ViewBookActivity extends AppCompatActivity{

    private static final String TAG = "ViewBookActivity";

    private String bookTitle;
    private String bookImagePath;
    private String bookGenres;

    private TextView tvBookTitlePossession;
    private TextView tvOwnerName;
    private TextView tvBookGenres;
    private ImageView ivBookCover;
    private ProgressBar bookCoverLoadingProgress;
    private ImageView ivBackArrow;

    private DatabaseReference databaseReference;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        bookTitle = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_title));
        bookImagePath = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_img_url));
        bookGenres = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_genres));

        tvBookTitlePossession = (TextView) findViewById(R.id.postBookTitle);
        tvOwnerName = (TextView) findViewById(R.id.postOwnerName);
        tvBookGenres = (TextView) findViewById(R.id.postBookGenres);
        ivBookCover = (ImageView) findViewById(R.id.postBookImage);
        ivBackArrow = (ImageView) findViewById(R.id.postBackArrow);
        bookCoverLoadingProgress = (ProgressBar) findViewById(R.id.postBookImageProgress);

        bookGenres.replace('#',' ');
        tvBookGenres.setText("GENRES: " + bookGenres);

        setCurrentBookDetails();

        UniversalmageLoader.setImage(bookImagePath, ivBookCover, bookCoverLoadingProgress, "");

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setCurrentBookDetails(){

        Query query = databaseReference
                .child(getString(R.string.dbname_photos))
                .orderByChild(getString(R.string.field_image_path))
                .equalTo(bookImagePath);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Photo currentPhoto = singleSnapshot.getValue(Photo.class);
                    setOwnerName(currentPhoto);
                    setBookTitleAndPossessionDetails(currentPhoto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setOwnerName(Photo photo){
        Query query = databaseReference
                .child(getString(R.string.dbname_user_accounts))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(photo.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user = singleSnapshot.getValue(User.class);
                    if (user != null) {
                        tvOwnerName.setText("OWNER: " + user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setBookTitleAndPossessionDetails(Photo photo){
        Query query = databaseReference
                .child(getString(R.string.dbname_user_accounts))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(photo.getIn_possession());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user = singleSnapshot.getValue(User.class);
                    if (user != null) {
                        tvBookTitlePossession.setText(bookTitle.toUpperCase()
                                + " is currently with " + user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
