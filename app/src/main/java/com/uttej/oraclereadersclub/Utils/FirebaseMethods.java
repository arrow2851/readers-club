package com.uttej.oraclereadersclub.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uttej.oraclereadersclub.Models.User;
import com.uttej.oraclereadersclub.R;

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

    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mContext = context;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

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

}
