package com.uttej.oraclereadersclub.AddBooks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.Profile.ProfileActivity;
import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Requests.RequestsActivity;
import com.uttej.oraclereadersclub.Utils.Permissions;

/**
 * Created by Clean on 25-03-2018.
 */

public class AddBooksActivity extends AppCompatActivity {

    private static final String TAG = "AddBooksActivity";

    private static final int VERIFY_PERMISSION_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 5;

    Button mLaunchCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        if(checkPermissionsArray(Permissions.PERMISSIONS)){

         }
        else{
            verifyPermissions(Permissions.PERMISSIONS);
        }

        initLaunchCamera();
        setupBottomNavigationView();
    }

//    ------------------------CAMERA-----------------------------

    public void initLaunchCamera(){
        mLaunchCamera = (Button)findViewById(R.id.addBooksLaunchCamera);
        mLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions(Permissions.CAMERA_PERMISSION)){
                    Intent CameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(CameraIntent, CAMERA_REQUEST_CODE);
                }
                else{
                    Toast.makeText(AddBooksActivity.this, "Permission not granted to access camera. Please grant permission before continuing", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(AddBooksActivity.this, NextActivity.class);
            intent.putExtra(getString(R.string.selected_bitmap), bitmap);
            startActivity(intent);
        }
    }

    //    --------------------------PERMISSIONS----------------------------

    public void verifyPermissions(String[] permissions){
        ActivityCompat.requestPermissions(
                AddBooksActivity.this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] Permissions){
        for(int i = 0; i < Permissions.length; i++){
            String check = Permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission){
        int permissionReuquest = ActivityCompat.checkSelfPermission(AddBooksActivity.this, permission);
        if(permissionReuquest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else {
            return true;
        }
    }

//    ------------------------------BOTTOM NAVIGATION BAR--------------------------

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
