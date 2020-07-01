package com.example.iiitb_connects;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    //Views
    private TextInputEditText username, fullName, emailId, password;
    private Button loginButton, signupButton;

    //Firebase
    private FirebaseAuth mAuth;

    //sign up result
    private boolean result;

    //shared preferences
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        //init shared preferences
        sharedPreferences = getSharedPreferences("com.example.sampleproject", MODE_PRIVATE);

        //Init firebase
        mAuth = FirebaseAuth.getInstance();

        //Init views
        username = (TextInputEditText) findViewById(R.id.username);
        fullName = (TextInputEditText) findViewById(R.id.fullName);
        emailId = (TextInputEditText) findViewById(R.id.emailId);
        password = (TextInputEditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Signup the user
                createAccount();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take to the login activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(emailId.getText().toString(),
                password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    UserInfo users = new UserInfo(username.getText().toString(),
                            fullName.getText().toString(),
                            emailId.getText().toString());
                    //updating shared preferences
                    sharedPreferences.edit().putString("username", username.getText().toString()).apply();
                    sharedPreferences.edit().putString("fullName", fullName.getText().toString()).apply();
                    //updating in firebase
                    mRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(users);
                    //Take to the main activity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(SignupActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    Log.i("AuthFailureReason", String.valueOf(task.getException()));
                }
            }
        });
    }
}
