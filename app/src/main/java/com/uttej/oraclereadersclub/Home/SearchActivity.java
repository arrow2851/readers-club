package com.uttej.oraclereadersclub.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uttej.oraclereadersclub.AddBooks.AddBooksActivity;
import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.BookListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Clean on 29-03-2018.
 */

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EditText searchQuery;
    private ListView searchResultsListView;
    private List<Photo> booksList;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQuery = (EditText) findViewById(R.id.searchQuery);
        searchResultsListView = (ListView) findViewById(R.id.searchResultsListView);

        initTextListener();
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initTextListener(){
        booksList = new ArrayList<>();

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userEnteredQuery = searchQuery.getText().toString().toLowerCase(Locale.getDefault()).trim();
                searchForMatch(userEnteredQuery);
            }
        });
    }

    private void searchForMatch(String keyword){
        booksList.clear();
        Log.d(TAG, "searchForMatch: " + keyword);
        if(keyword.length() == 0){

        }
        else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference
                    .child(getString(R.string.dbname_photos))
                    .orderByChild(getString(R.string.field_book_title))
                    .equalTo(keyword);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: datasnapshot for query: " + singleSnapshot);
                        booksList.add(singleSnapshot.getValue(Photo.class));
                        updateBooksList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateBooksList(){
        bookListAdapter = new BookListAdapter(SearchActivity.this, R.layout.layout_book_list_item, booksList);

        searchResultsListView.setAdapter(bookListAdapter);

        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(SearchActivity.this, ViewBookActivity.class);
                intent.putExtra(SearchActivity.this.getString(R.string.book_title), booksList.get(position).getBook_title());
                intent.putExtra(SearchActivity.this.getString(R.string.book_img_url), booksList.get(position).getImage_path());
                intent.putExtra(SearchActivity.this.getString(R.string.book_genres), booksList.get(position).getGenres());
                startActivity(intent);
            }
        });

    }
}
