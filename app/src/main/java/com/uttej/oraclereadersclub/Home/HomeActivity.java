package com.uttej.oraclereadersclub.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Login.LoginActivity;
import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.Models.Request;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;
import com.uttej.oraclereadersclub.Utils.GridImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "BooksCatalogActivity";
    private int NUM_GRID_COLUMNS = 2;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageView mSearchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSearchIcon = (ImageView) findViewById(R.id.homeActivitySearchIcon);
        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        setupFirebaseAuth();

        setupBottomNavigationView();
        populateGridView();
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

    private void populateGridView(){
        Log.d(TAG, "populateGridView: Inside");
        final ArrayList<Photo> photos = new ArrayList<>();
        final ArrayList<String> imgURLs = new ArrayList<String>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference
                .child(getString(R.string.dbname_photos));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>)ds.getValue();

                    photo.setBook_title(objectMap.get(getString(R.string.field_book_title)).toString());
                    photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
                    photo.setPhoto_id(objectMap.get(getString(R.string.field_book_id)).toString());
                    photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                    photo.setGenres(objectMap.get(getString(R.string.field_genres)).toString());
                    photo.setIn_possession(objectMap.get(getString(R.string.field_in_possession)).toString());

                    List<Request> requestList = new ArrayList<Request>();
                    for(DataSnapshot dsnapshot: ds.child(getString(R.string.field_requests)).getChildren()){
                        Request request = new Request();
                        request.setUser_id(dsnapshot.getValue(Request.class).getUser_id());
                        requestList.add(request);
                    }
                    photo.setRequests(requestList);
                    photos.add(photo);

                }
                for(int i = 0; i < photos.size(); i++){
                    imgURLs.add(photos.get(i).getImage_path());
                }

                setupImageGridView(imgURLs, photos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
        displaying images in a grid view
     */
    private void setupImageGridView(ArrayList<String> imgURLs, final ArrayList<Photo> photos){
        GridView gridView = (GridView)findViewById(R.id.imagesGrid);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/2;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(HomeActivity.this, R.layout.layout_grid_image_view, "", imgURLs);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, ViewBookActivity.class);
                intent.putExtra("book_id", photos.get(position).getPhoto_id());
                intent.putExtra(HomeActivity.this.getString(R.string.book_title), photos.get(position).getBook_title());
                intent.putExtra(HomeActivity.this.getString(R.string.book_img_url), photos.get(position).getImage_path());
                intent.putExtra(HomeActivity.this.getString(R.string.book_genres), photos.get(position).getGenres());
                intent.putExtra("owner_id", photos.get(position).getUser_id());
                intent.putExtra("in_possession_id", photos.get(position).getIn_possession());
                startActivity(intent);
            }
        });
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
