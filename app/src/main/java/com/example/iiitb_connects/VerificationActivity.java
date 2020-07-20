package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

    String phoneNumber;

    FirebaseAuth mAuth;
    DatabaseReference mRef;

    UserInfo user;

    private String verificationId;

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
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        countrySpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[countrySpinner.getSelectedItemPosition()];
                String number = phoneNumberInput.getText().toString().trim().replace(""," ");
                if (number.isEmpty()){
                    phoneNumberInput.setError("Enter Valid Phone No.");
                    phoneNumberInput.requestFocus();
                    return;
                }
                phoneNumber = "+"+code+number;
                sendOTP(phoneNumber);
            }
        });

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = OTPInput.getText().toString().trim().replace(" ","");
                if (code.isEmpty() || code.length()!=6){
                //    Toast.makeText(VerificationActivity.this, "OTP Verification failed!", Toast.LENGTH_SHORT).show();
                    OTPInput.setError("Enter Valid Code!");
                    OTPInput.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    public void sendOTP(String number){
        Log.i("Anant","Sending Otp");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    public  PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Automatic Detection of Code
            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                OTPInput.setTag(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this,"onVerificationFailed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private  void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Saara Kaam
                            user.setPhoneNumber(phoneNumber);
                            mRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
                        } else {
                            Toast.makeText(VerificationActivity.this,"signInWithCredentials unsuccessful"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
