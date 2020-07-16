package com.example.iiitb_connects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {

    QuestionInProfileAdapter adapter;
    ArrayList<QuestionInfo> questionList;

    //Views
    SearchView searchView1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;

    //Firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        refreshLayout = view.findViewById(R.id.refreshLayout2);
        questionList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Questions Asked").child(mAuth.getCurrentUser().getUid());

    /*    questionList.add(new QuestionInfo("Why is Kartik such a madarchod?",0));
        questionList.add(new QuestionInfo("Why is Kartik such a Randi?",0));
        questionList.add(new QuestionInfo("Why is Anant such a good boy?",0));  */
        recyclerView = view.findViewById(R.id.searchQuestionRecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        adapter = new QuestionInProfileAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        retrieveQuestions();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveQuestions();
            }
        });

        return view;
    }

    public void retrieveQuestions(){
        //    Toast.makeText(getContext(), "Inside1", Toast.LENGTH_SHORT).show();
        questionList.clear();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String queschan,aansar;
                int aansarInt;
                for (DataSnapshot ds : snapshot.getChildren()){
                    queschan=null;aansar=null;
                    if (ds.hasChild("mQuestion")){
                        queschan = ds.child("mQuestion").getValue().toString();
                        //    aansar = ds.child(queschan).child("mNoOfAnswers").getValue().toString();
                        //    aansarInt = Integer.parseInt(aansar);
                        questionList.add(new QuestionInfo(queschan,0,ds.getKey().toString()));
                        adapter.notifyDataSetChanged();}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Oops...Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setRefreshing(false);
    }

    public void search(String str){
        ArrayList<QuestionInfo> myList = new ArrayList<>();
        for (QuestionInfo q : questionList){
            if (q.getmQuestion().toLowerCase().contains(str.toLowerCase())){
                myList.add(q);
            }
        }
        QuestionAdapter adapter = new QuestionAdapter(myList);
        recyclerView.setAdapter(adapter);
    }

}