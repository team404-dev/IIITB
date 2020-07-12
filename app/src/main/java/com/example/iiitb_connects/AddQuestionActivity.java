package com.example.iiitb_connects;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                if (question.equals("")){
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
                QuestionInfo ques = new QuestionInfo(question,0);
                DatabaseReference mDRef = mRef.push();
                mDRef.setValue(ques);
                progressBar.setVisibility(View.GONE);
                waitTextView.setVisibility(View.GONE);
                Toast toast1 = Toast.makeText(getApplicationContext(),"Question Uploaded Successfully!",Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                toast1.show();
                onBackPressed();
            }
        });
    }
}
