package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    //Views
    private TextInputEditText fullNameEditText,usernameEditText,emailEditText,
            phoneNumberEditText,passwordEditText,confirmPasswordEditText;
    private Button btnContinue;
    private TextView loginTextView,pleaseWaitTextView;
    private ProgressBar progressBar;
    private CountryCodePicker ccp;
    TextView timerTV;

    //Strings
    private String email,fullName,username,password,confirmPassword,id;
    public String phoneNumber,phoneNumberForVerif;
    private int counter, anant;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;

    //Shared Preferences
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //    getSupportActionBar().setTitle("Register Yourself");

        //Init SharedPrefernces
        sharedPreferences = getSharedPreferences("com.example.sampleproject", MODE_PRIVATE);


        //Init Views
        fullNameEditText = (TextInputEditText) findViewById(R.id.fullNameInput);
        usernameEditText = (TextInputEditText) findViewById(R.id.usernameInput);
        emailEditText = (TextInputEditText) findViewById(R.id.emailInputRegister);
    //    phoneNumberEditText = (TextInputEditText) findViewById(R.id.phoneNumberInput);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordInputRegister);
        confirmPasswordEditText = (TextInputEditText) findViewById(R.id.confirmPasswordInput);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        pleaseWaitTextView = (TextView) findViewById(R.id.pleaseWaitTextView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        pleaseWaitTextView.setVisibility(View.GONE);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        setUpFirebaseAuth();


        //On clicking "Already have an account? SignIn"
        loginTextView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //On clicking "REGISTER" button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=0;
                email = emailEditText.getText().toString().trim();
                fullName = fullNameEditText.getText().toString().trim();
                username = usernameEditText.getText().toString().trim();
        //        phoneNumber = phoneNumberEditText.getText().toString().trim().replace(" ","");
                password = passwordEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();
                if (isStringEmpty(email)){
                    emailEditText.setError("*Field can't be empty!");
                    counter =1;
                }
                if (isStringEmpty(fullName)){
                    fullNameEditText.setError("*Field can't be empty!");
                    counter =1;
                }
                if (isStringEmpty(username)){
                    usernameEditText.setError("*Field can't be empty!");
                    counter =1;
                }
                /*if (isStringEmpty(phoneNumber)){
                    phoneNumberEditText.setError("*Field can't be empty!");
                    counter =1;
                }*/
                if (isStringEmpty(password)){
                    passwordEditText.setError("*Field can't be empty!");
                    counter =1;
                }
                if (isStringEmpty(confirmPassword)){
                    confirmPasswordEditText.setError("*Field can't be empty!");
                    counter =1;
                }
                if (!password.equals(confirmPassword)){
                    passwordEditText.setError("Passwords don't match!");
                    confirmPasswordEditText.setError("*Passwords don't match!");
                    counter =1;
                }
                /*if (phoneNumber.length() != 10){
                    phoneNumberEditText.setError("*Enter Valid Phone Number!");
                    counter =1;
                }*/
                if (counter == 0){
                    sharedPreferences.edit().putString("username", username).apply();
                    sharedPreferences.edit().putString("fullName", fullName).apply();
                }
                if (counter == 0) {
                    //Continue registration
                    progressBar.setVisibility(View.VISIBLE);
                    pleaseWaitTextView.setVisibility(View.VISIBLE);
                /*    mAuth.createUserWithEmailAndPassword(email,
                            password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
                                //    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();*/
                                UserInfo user = new UserInfo(email,fullName,username,"",password,confirmPassword);
                                progressBar.setVisibility(View.GONE);
                                pleaseWaitTextView.setVisibility(View.GONE);
                                //Take to the OTP Activity
                                Intent intent = new Intent(getApplicationContext(),VerificationActivity.class);
                                intent.putExtra("userInfo",user);
                                Log.i("Anant","Passing intent to verification Activity");
                                startActivity(intent);
                    //        }
                        /*    else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                Log.i("AuthFailureReason", String.valueOf(task.getException()));
                            }
                        }
                    });*/

                }
                else if (counter == 1){
                    Toast.makeText(RegisterActivity.this, "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    } //End of onCreate

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
}