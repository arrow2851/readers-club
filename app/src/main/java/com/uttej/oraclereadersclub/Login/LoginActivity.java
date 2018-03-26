package com.uttej.oraclereadersclub.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uttej.oraclereadersclub.Home.HomeActivity;
import com.uttej.oraclereadersclub.R;

/**
 * Created by Clean on 25-03-2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mUsername, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar)findViewById(R.id.loggingUserInProgressBar);
        mProgressBar.setVisibility(View.GONE);

        mUsername = (EditText) findViewById(R.id.loginName);
        mPassword = (EditText) findViewById(R.id.loginPassword);
        mContext = LoginActivity.this;

        setupFirebaseAuth();
        init();
    }

    //----------------------------FIREBASE----------------------------------


    private void init(){
        //initialise the button for loggin in
        Button loginBotton = (Button) findViewById(R.id.loginButton);
        loginBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if(email.equals("") || password.equals("")){
                    Toast.makeText(mContext,"Fill out your email and password",Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressBar.setVisibility(View.VISIBLE);

                    //sign in existing users with firebase
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //checking whether account is verified through verification link sent
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        try{
                                            if(user.isEmailVerified()){
                                                Log.d(TAG, "Email is verified");
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(mContext,"Your email is not yet verified \n Check your inbox for the link.",Toast.LENGTH_LONG).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
                                        }catch (NullPointerException e){
                                            Log.e(TAG, "onComplete: Null pointer exception: " + e.getMessage());
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

//        if user wants to create a new account, go to register activity
        TextView goSignUp = (TextView)findViewById(R.id.loginToRegister);
        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        if user logged in, go to homeactivity
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
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
