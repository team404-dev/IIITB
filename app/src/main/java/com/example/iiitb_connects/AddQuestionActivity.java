package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddQuestionActivity extends AppCompatActivity {
    //Views
    ImageView closeBtn;
    Button submitQuestion;
    TextInputEditText askAQuestionEditText;
    ProgressBar progressBar;
    TextView waitTextView;

    //Vars
    String question;

    //firebase
    DatabaseReference mRef;
    DatabaseReference mRefAns;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        //Init Views
        closeBtn = (ImageView) findViewById(R.id.closeBtn);
        submitQuestion = (Button) findViewById(R.id.submitQuestion);
        askAQuestionEditText = (TextInputEditText) findViewById(R.id.AskAQuestion);
        progressBar = findViewById(R.id.progressBarCircular);
        waitTextView = findViewById(R.id.pleaseWaitText);
        progressBar.setVisibility(View.GONE);
        waitTextView.setVisibility(View.GONE);

        //Vars
        question = "";

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Questions Asked").child(mAuth.getCurrentUser().getUid());
    //    mRefAns = FirebaseDatabase.getInstance().getReference("Answers");
        closebutton();
        submitYourQuestion();

    }

    public void closebutton(){
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void submitYourQuestion(){
        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = askAQuestionEditText.getText().toString().trim();
                if (question.equals("") || question.equals(null)){
                    Toast toast = Toast.makeText(AddQuestionActivity.this, "Type a Question!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                waitTextView.setVisibility(View.VISIBLE);
                String last = question.substring(question.length()-1);
                if (!last.equals("?") && !last.equals("!") && !last.equals(".")){
                    question = question + "?";
                }
                DatabaseReference mDRef = mRef.push();
                QuestionInfo ques = new QuestionInfo(question,0,mDRef.getKey());
        //        mRefAns.child(mDRef.getKey()).child("Number of Answers").setValue("0");
                mDRef.setValue(ques);
                progressBar.setVisibility(View.GONE);
                waitTextView.setVisibility(View.GONE);
                Toast toast1 = Toast.makeText(getApplicationContext(),"Question Uploaded Successfully! /n Swipe Down to Refresh",Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                toast1.show();
                onBackPressed();
            }
        });
    }
}
