package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    Spinner countrySpinner;
    TextInputEditText phoneNumberInput,OTPInput;
    Button btnSendOTP,btnVerifyOTP;
    TextView time,secs;
    ProgressBar progressBar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    String phoneNumber,otp;

    DatabaseReference mRef;
    LinearLayout linearLayout1;

    FirebaseAuth auth;
    private String verificationCode;
    long timeLeftInMillisecs = 60000;

    CountDownTimer countDownTimer;

    UserInfo user;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        user = (UserInfo) getIntent().getSerializableExtra("userInfo");
        Log.i("Anant","Recieved intent in Verification Activity");
        countrySpinner = findViewById(R.id.countrySpinner);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        OTPInput = findViewById(R.id.OTPInput);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        linearLayout1 = findViewById(R.id.linearLayout1);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        time = findViewById(R.id.time);
        secs = findViewById(R.id.secs);
        linearLayout1.setVisibility(View.GONE);
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        countrySpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));

        sharedPreferences = getSharedPreferences("com.example.sampleproject", MODE_PRIVATE);

        StartFirebaseLogin();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeLeftInMillisecs = 60000;
                countDownBegins();
                String num = phoneNumberInput.getText().toString().trim().replace(" ","");
                String countryCode = CountryData.countryAreaCodes[countrySpinner.getSelectedItemPosition()];

                phoneNumber = "+"+countryCode+num;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        VerificationActivity.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks
            }
        });

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                otp=OTPInput.getText().toString().trim().replace(" ","");
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                SigninWithPhone(credential);
            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.VISIBLE);
                countDownBegins();
                String num = phoneNumberInput.getText().toString().trim().replace(" ","");
                String countryCode = CountryData.countryAreaCodes[countrySpinner.getSelectedItemPosition()];

                phoneNumber = "+"+countryCode+num;
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        VerificationActivity.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks
            }
        });
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //    Toast.makeText(VerificationActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(VerificationActivity.this,"Code Sent",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setPhoneNumber(phoneNumber);
                            mRef.child(auth.getCurrentUser().getUid()).setValue(user);
                            sharedPreferences.edit().putString("username", user.getUsername()).apply();
                            sharedPreferences.edit().putString("fullName", user.getFullName()).apply();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(VerificationActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void countDownBegins(){
        countDownTimer = new CountDownTimer(timeLeftInMillisecs,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisecs = millisUntilFinished/1000;
                secs.setVisibility(View.VISIBLE);
                time.setText(String.valueOf(timeLeftInMillisecs));
                time.setEnabled(false);
            }

            @Override
            public void onFinish() {
                time.setText("Resend Code");
                secs.setVisibility(View.GONE);
                time.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
