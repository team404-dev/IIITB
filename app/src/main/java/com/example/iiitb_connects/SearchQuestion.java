package com.example.iiitb_connects;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchQuestion extends Fragment {
    //Views
    Button addANewQuestion;
    RecyclerView questionsRecyclerView;

    //Firebase
    DatabaseReference mRef;

    //Vars
    ArrayList<QuestionInfo> list;
    QuestionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_question, container, false);
        Toast.makeText(getContext(), "SearchQuestion Entered", Toast.LENGTH_SHORT).show();
        addANewQuestion = view.findViewById(R.id.addANewQuestion);
        questionsRecyclerView = view.findViewById(R.id.questionsRecyclerView);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<QuestionInfo>();

        mRef = FirebaseDatabase.getInstance().getReference().child("questions_asked");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                for (DataSnapshot snapshot:snapshot1.getChildren() ){
                    QuestionInfo ques = snapshot.getValue(QuestionInfo.class);
                    list.add(ques);
                }
                adapter = new QuestionsAdapter(getContext(),list);
                questionsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    /*    addANewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout,new AddQuestionFragment())
                        .commit();
            }
        });  */
        //    Log.i("Anant","Raj");
        return view;
    }
}