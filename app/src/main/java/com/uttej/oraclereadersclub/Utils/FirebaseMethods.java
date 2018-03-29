package com.uttej.oraclereadersclub.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.Models.User;
import com.uttej.oraclereadersclub.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Clean on 26-03-2018.
 */

public class FirebaseMethods {

    private static final String TAG = "Firebase Methods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private StorageReference mStorageReference;

    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void registerNewUser(final String username, final String rollNumber, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //send verification email
                            sendVerificationEmail();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            userID = mAuth.getCurrentUser().getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.d(TAG, "username check " + username + " datasnapshot " + dataSnapshot);
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
            user.setUsername(ds.getValue(User.class).getUsername());
            if(user.getUsername().equals(username)){
                Log.d(TAG, "user already exists");
                return true;
            }
        }
        return false;
    }

    public boolean checkIfRollNumberExists(String rollnumber, DataSnapshot dataSnapshot){

        User user = new User();

        for(DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
            user.setRollnumber(ds.getValue(User.class).getRollnumber());
            if(user.getRollnumber().equals(rollnumber)){
                Log.d(TAG, "roll number already exists");
                return true;
            }
        }
        return false;
    }

    public void addNewUser(String email, String rollnumber, String username){
        User user = new User(email, rollnumber, userID, username);

        mDatabaseReference.child(mContext.getString(R.string.dbname_user_accounts))
                .child(userID)
                .setValue(user);
    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }else{
                                Toast.makeText(mContext, "Couldn't send the verification email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public User getUserInformation(DataSnapshot dataSnapshot){

        User user = new User();
        user.setUser_id(userID);
        user.setEmail("");

        for(DataSnapshot ds: dataSnapshot.getChildren()){
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_accounts))){

                try{

                    user.setRollnumber(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getRollnumber()
                    );

                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );

                }catch (NullPointerException e){
                    Log.e(TAG, "caught exception while retrieving data: " + e.getMessage());
                }
            }
        }
        return user;
    }

    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for(DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count++;
        }
        return count;
    }

     public void uploadNewPhoto(String photoType, final String caption, int count, final String imgURL, Bitmap bitmap){

         FilePaths filePaths = new FilePaths();
         String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

         if(photoType.equals(mContext.getString(R.string.new_photo)))
         {
             StorageReference storageReference = mStorageReference
                     .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count+1));

             //convert umage to bitmap. This function is used if the image is selected from galery, which is not implemented yet.
             if(bitmap == null) {
                 bitmap = ImageManager.getBitmap(imgURL);
             }

             byte[] bytes = ImageManager.getBytesFromBitmap(bitmap, 100);

             UploadTask uploadTask = null;
             uploadTask = storageReference.putBytes(bytes);

             uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                     Toast.makeText(mContext, "Photo Upload Success", Toast.LENGTH_SHORT).show();

                     addPhotoToDatabase(caption, firebaseUrl.toString());

                     Intent intent = new Intent(mContext, HomeActivity.class);
                     mContext.startActivity(intent);
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(mContext, "Photo Upload Failed", Toast.LENGTH_SHORT).show();
                 }
             }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                     double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                     if(progress - 20 > mPhotoUploadProgress){
                         Toast.makeText(mContext, "Photo Upload: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                         mPhotoUploadProgress = progress;
                     }
                 }
             });
         }
     }

     private void addPhotoToDatabase(String caption, String url){

         String genres = StringManipulation.getGenres(caption);
         String bookTitle = StringManipulation.getBookTitle(caption);
         String newPhotoKey = mDatabaseReference
                 .child(mContext.getString(R.string.dbname_user_photos))
                 .push()
                 .getKey();
         Photo photo = new Photo();
         photo.setBook_title(bookTitle);
         photo.setDate_created(getTimeStamp());
         photo.setImage_path(url);
         photo.setGenres(genres);
         photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
         photo.setPhoto_id(newPhotoKey);

         //now insert into database
         mDatabaseReference
                 .child(mContext.getString(R.string.dbname_user_photos))
                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                 .child(newPhotoKey)
                 .setValue(photo);

         mDatabaseReference
                 .child(mContext.getString(R.string.dbname_photos))
                 .child(newPhotoKey)
                 .setValue(photo);
     }

     private String getTimeStamp(){
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
         return sdf.format(new Date());
     }

}
