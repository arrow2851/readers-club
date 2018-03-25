package com.uttej.oraclereadersclub.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.uttej.oraclereadersclub.AddBooksActivity;
import com.uttej.oraclereadersclub.R;

/**
 * Created by Clean on 25-03-2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addBooks:
                        Intent toAddBooks = new Intent(context, AddBooksActivity.class);
                        context.startActivity(toAddBooks);
                        break;
                    case R.id.booksCatalog:
                        Intent toHome = new Intent(context, AddBooksActivity.class);
                        context.startActivity(toHome);
                        break;
                    case R.id.requests:
                        Intent toRequests = new Intent(context, AddBooksActivity.class);
                        context.startActivity(toRequests);
                        break;
                    case R.id.userProfile:
                        Intent toUserProfile = new Intent(context, AddBooksActivity.class);
                        context.startActivity(toUserProfile);
                        break;
                }
                return false;
            }
        });
    }


}
