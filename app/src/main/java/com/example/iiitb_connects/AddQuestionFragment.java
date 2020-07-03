package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionFragment extends Fragment {

    //Views
    TextInputEditText askAQuestion;
    Button submitQuestion;

    //Vars
    String question;

    //Firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_question,container,false);

        askAQuestion = v.findViewById(R.id.AskAQuestion);
        submitQuestion = v.findViewById(R.id.submitQuestion);
        mRef = FirebaseDatabase.getInstance().getReference();
        question = "";

        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = askAQuestion.getText().toString().trim();
                char lastChar = question.charAt(question.length()-1);
                if (question.equals("")){
                    askAQuestion.setError("Please type type something to add!");
                }
                else{
                    if(lastChar != '?' || lastChar != '!' || lastChar != '.'){
                        question = question + "?";
                    }
                    QuestionInfo ques = new QuestionInfo(question,0);
                    mRef.child("questions_asked").child(mAuth.getCurrentUser().getUid()).setValue(ques);
                }
            }
        });

        return v;
    }
}
