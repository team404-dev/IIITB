package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //private Context mContext;
    private ProgressBar mProgressbar;
    private TextInputEditText mEmailEditText,mPasswordEditText;
    private TextView mForgotCredentials,mNoAccountYet;
    private Button loginButton;

    String check = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //    getSupportActionBar().setTitle("Dive Right In");

        mProgressbar = findViewById(R.id.progressBar);
        mEmailEditText = findViewById(R.id.emailInput);
        mPasswordEditText = findViewById(R.id.passwordInput);
        mNoAccountYet = findViewById(R.id.noAccountYetTextView);
        //mPleaseWait = findViewById(R.id.pleaseWaitTextView);
        mForgotCredentials = findViewById(R.id.forgotPasswordTextView);
        loginButton = findViewById(R.id.loginButton);
        //mContext = getApplicationContext();
        mProgressbar.setVisibility(View.GONE);
        //mPleaseWait.setVisibility(View.GONE);

        check = getIntent().getStringExtra("check");

        setUpFirebaseAuth();
        init();

        // On clicking on "no account yet"
        mNoAccountYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // On clicking on "forgot password"
        mForgotCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

    }

    // Checking if any string is left empty
    private boolean isStringEmpty(String string){
        if (string.equals("")){
            return true;
        }
        return false;
    }


    //--------------------------------------------------------------------------------------------------
    //------------------------------Firebase Part-------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private void init(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString();
                int counter=0;

                if (isStringEmpty(email)){
                    mEmailEditText.setError("*Field can't be empty!");
                    counter = 1;
                }
                if (isStringEmpty(password)){
                    mPasswordEditText.setError("*Field can't be empty!");
                    counter = 1;
                }
                if (counter == 0){
                    mProgressbar.setVisibility(View.VISIBLE);
                //    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email , password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(String.valueOf(getActivity()), "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        mProgressbar.setVisibility(View.GONE);
                                        //mPleaseWait.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"Authentication Successful",Toast.LENGTH_SHORT).show();
                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
                                        mRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String username1 = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("username").getValue().toString();
                                                String fullName1 = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("fullName").getValue().toString();
                                                MainActivity.sharedPreferences.edit().putString("username", username1).apply();
                                                MainActivity.sharedPreferences.edit().putString("fullName", fullName1).apply();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(String.valueOf(getActivity()), "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        mProgressbar.setVisibility(View.GONE);
                                        //mPleaseWait.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });
        // If the user is loggedIn

        if (mAuth.getCurrentUser() != null ){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void setUpFirebaseAuth(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //User signed In
                    //Log.i("MainActivity","onAuthStateChanged:signedIn:"+user.getUid());
                } else{
                    //User signed Out
                    //Log.i("MainActivity","onAuthStateChanged:signedOut:");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    AlertDialog alert;
    public void showAlert(){
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View v = inflater.inflate(R.layout.reset_password_dialog,null);
        //Initialising Views
        final TextInputEditText resetEmail = v.findViewById(R.id.resetEmail);
        Button sendButton = v.findViewById(R.id.positiveButton);
        Button cancelButton = v.findViewById(R.id.negativeButton);


        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(v).setCancelable(false);
        alert = builder.create();
        alert.show();

        //on Okay click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send reset link
                final String email = resetEmail.getText().toString().trim();
                if (email.isEmpty() || email.equals("") || email == null){
                    Toast.makeText(LoginActivity.this, "Enter Valid email!", Toast.LENGTH_SHORT).show();
                } else{
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LoginActivity.this, "Reset Link sent to " + email, Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }
}