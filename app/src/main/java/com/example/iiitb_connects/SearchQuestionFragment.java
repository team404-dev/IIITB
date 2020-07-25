
package com.example.iiitb_connects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class SearchQuestionFragment extends Fragment{

    QuestionAdapter adapter;
    ArrayList<QuestionInfo> questionList;

    //Views
    SearchView searchView1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    ImageView plusIV;

    //Firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_question, container, false);


        searchView1 = view.findViewById(R.id.searchViewAnant);
        refreshLayout = view.findViewById(R.id.refreshLayout1);
        questionList = new ArrayList<>();
        plusIV = view.findViewById(R.id.plusIV);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Questions Asked");

    /*    questionList.add(new QuestionInfo("Why is Kartik such a madarchod?",0));
        questionList.add(new QuestionInfo("Why is Kartik such a Randi?",0));
        questionList.add(new QuestionInfo("Why is Anant such a good boy?",0));  */
        recyclerView = view.findViewById(R.id.searchQuestionRecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        adapter = new QuestionAdapter(questionList,getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        retrieveQuestions();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionList.clear();
                retrieveQuestions();
            }
        });

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //    adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                search(newText);
                return false;
            }
        });

        /*adapter.setOnItemClickListener(new QuestionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //    Toast.makeText(getContext(), "Extension Opened Successfully!", Toast.LENGTH_SHORT).show();
                String Question = questionList.get(position).getmQuestion();
                String QuestionUidPassed = questionList.get(position).getmUid();

                Intent intent = new Intent(getContext(),ExtendedQuestionActivity.class);
                intent.putExtra("Question Passed",Question);
                intent.putExtra("Question Uid Passed",QuestionUidPassed);
                intent.putExtra("from","allQuestions");
                startActivity(intent);
            }
        });*/

        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddQuestionActivity.class));
            }
        });

        return view;
    }

    public void retrieveQuestions(){
        //    Toast.makeText(getContext(), "Inside1", Toast.LENGTH_SHORT).show();
        questionList.clear();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String queschan,aansar;
                int aansarInt;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.i("Anant","Raj");
                    for (DataSnapshot ds2 : ds.getChildren()){
                        queschan=null;aansar=null;
                        String Uid = null;
                        Uid = ds2.getKey().toString();
                        if (ds2.hasChild("mQuestion")){
                            queschan = ds2.child("mQuestion").getValue().toString();
                            //    aansar = ds.child(queschan).child("mNoOfAnswers").getValue().toString();
                            //    aansarInt = Integer.parseInt(aansar);
                            questionList.add(new QuestionInfo(queschan,0,Uid));
                        }
                    }
                }
                Collections.reverse(questionList);
                adapter.notifyDataSetChanged();
                refreshLayout.setEnabled(true);
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
        QuestionAdapter adapter = new QuestionAdapter(myList,getActivity());
        recyclerView.setAdapter(adapter);
    }
}
