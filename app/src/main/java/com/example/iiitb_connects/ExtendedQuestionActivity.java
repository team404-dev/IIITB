package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ExtendedQuestionActivity extends AppCompatActivity {
    //Views
    TextView questionTextView;
    Button answerButton;
    RecyclerView answersRecyclerView;
    SwipeRefreshLayout refreshLayout;
    TextView answerSection;

    //firebase
    DatabaseReference mRef;
    DatabaseReference mRefName;
    FirebaseAuth mAuth;

    //Vars
    ArrayList<AnsweredByInfo> answerList;
    AnswerAdapter adapter;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_question);

        questionTextView = findViewById(R.id.extendedLayoutQuestionTextView);
        answerButton = findViewById(R.id.answerButton);
        answersRecyclerView = findViewById(R.id.answersRecyclerView);
        answersRecyclerView.setHasFixedSize(true);
        refreshLayout = findViewById(R.id.refreshLayoutEx);
        answerSection = findViewById(R.id.answerSectionTextView);
        answerSection.setPaintFlags(answerSection.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final String question = getIntent().getStringExtra("Question Passed");
        final String QuestionUidPassed = getIntent().getStringExtra("Question Uid Passed");
        questionTextView.setText(question);
        answerList = new ArrayList<AnsweredByInfo>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        answersRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AnswerAdapter(getApplicationContext(),answerList);
        answersRecyclerView.setAdapter(adapter);


        mRef = FirebaseDatabase.getInstance().getReference("Answers").child(QuestionUidPassed);
        mRefName = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        userName = "";

        retrieveAnswers();
        Collections.reverse(answerList);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                answerList.clear();
                retrieveAnswers();
            }
        });


        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AnswerActivity.class);
                intent.putExtra("Question Passed",question);
                intent.putExtra("Question Uid Passed",QuestionUidPassed);
                startActivity(intent);
            }
        });
    }

    public void retrieveAnswers(){
        answerList.clear();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String answerString = "null", answererNameString = "Loading...Please Wait" ,answererUidString = "null";
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.hasChild("answer")){
                        answerString = ds.child("answer").getValue().toString();
                    }
                    if (ds.hasChild("answeredByName")){
                        answererNameString = ds.child("answeredByName").getValue().toString();
                    }
                    if (ds.hasChild("answersByUid")){
                        answererUidString = ds.child("answersByUid").getValue().toString();
                    }
                    String a = findUserName();
                    answerList.add(new AnsweredByInfo(answerString,answererUidString,answererNameString));
                    ds.getRef().child("answeredByName").setValue(a);
                }
            //    Collections.reverse(answerList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops...Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setRefreshing(false);
        refreshLayout.setEnabled(true);
    }

    public String findUserName (){
        DatabaseReference mDrefName = mRefName.child(mAuth.getCurrentUser().getUid());
        mDrefName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("fullName")){
                    userName = snapshot.child("fullName").getValue().toString();
                }
                else {userName="";}

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops...Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return userName;
    }
}