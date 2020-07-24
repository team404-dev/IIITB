package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnswerActivity extends AppCompatActivity {
    //Views
    TextInputEditText answerEditText;
    Button confirmAnswerButton;
    TextView questionTextView;
    ImageView closeBtn;

    //firebase
    DatabaseReference mRef;
    DatabaseReference mRefName;
    DatabaseReference mRefQues;
    FirebaseAuth mAuth;

    //Vars
    String question;
    String questionUid;
    String userName;
    String userUidForNoOfAns;
    String numberString;
    int numberInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        //Init Views
        answerEditText = findViewById(R.id.answerEditText);
        confirmAnswerButton = findViewById(R.id.confirmAnswerSubmision);
        questionTextView = findViewById(R.id.extendedLayoutQuestionTextView);
        closeBtn = findViewById(R.id.closeBtn);

        //Recieving Intent
        question = getIntent().getStringExtra("Question Passed");
        questionUid = getIntent().getStringExtra("Question Uid Passed");

        questionTextView.setText(question);
        //numOfAnswers = "0";

        userUidForNoOfAns = "";
        numberString = "0";
        numberInt = 0;
        //Init Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Answers");
        mRefName = FirebaseDatabase.getInstance().getReference("Users");
        mRefQues = FirebaseDatabase.getInstance().getReference("Questions Asked");

        //on submitting answer
        confirmAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAnswer();
            }
        });

        //on clicking X image
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    public void confirmAnswer(){
        String answer = answerEditText.getText().toString().trim();
        if (answer.trim().equals(null) || answer.trim().equals("")){
            Toast.makeText(getApplicationContext(),"Empty Answer cannot be added!",Toast.LENGTH_SHORT).show();
            return;
        }
        String userUid = mAuth.getCurrentUser().getUid();
        DatabaseReference mDRef = mRef.child(questionUid).push();

        String a = findUserName();
        //    Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        AnsweredByInfo answerInfo = new AnsweredByInfo(answer,userUid,a,mDRef.getKey());

        mDRef.setValue(answerInfo);
        //    mDRef.child("answeredByName").setValue(a);


        mRefQues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.hasChild(questionUid)){
                        userUidForNoOfAns = ds.getKey();
                        //    Toast.makeText(AnswerActivity.this, userUidForNoOfAns, Toast.LENGTH_LONG).show();
                        numberString = ds.child(questionUid).child("mNoOfAnswers").getValue().toString();
                        numberInt = Integer.parseInt(numberString);
                        numberInt++ ;
                        numberString = String.valueOf(numberInt);
                    }
                }
                mRefQues.child(userUidForNoOfAns).child(questionUid).child("mNoOfAnswers").setValue(numberString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops...Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("userUidForNoOfAns",userUidForNoOfAns);
        Log.i("questionUid",questionUid);
        Log.i("numberString",numberString);

        Toast.makeText(this, "Swipe Down to Refresh!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public void close(){
        onBackPressed();
    }

    public String findUserName (){
        userName = MainActivity.sharedPreferences.getString("fullName","Could not find Username");
        return userName;
    }
}
/////Anant/////