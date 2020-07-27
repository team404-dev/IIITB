package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class StalkingActivity extends AppCompatActivity {

    //Vars
    public String StalkingUid;
    ArrayList<String> questionUidList;
    ArrayList<String> newQuestionUidList;
    ArrayList<QuestionInfo> questionList;

    //Views
    TextView fullNameTV;
    TextView usernameTV;
    ImageView userDPIV;
    ProgressBar progressBar;
    ImageView backButton;
    TextView aboutTV,bioTV,aboutTV1;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    QuestionInSearchProfileAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    //firebase
    DatabaseReference mRef;
    DatabaseReference mRefAns;
    DatabaseReference mRefQues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stalking);

        StalkingUid = getIntent().getStringExtra("User Uid");
        questionUidList = new ArrayList<String>();
        newQuestionUidList = new ArrayList<String>();
        questionList = new ArrayList<QuestionInfo>();

        mRef = FirebaseDatabase.getInstance().getReference("Users");
        mRefAns = FirebaseDatabase.getInstance().getReference("Answers");
        mRefQues = FirebaseDatabase.getInstance().getReference("Questions Asked");

        fullNameTV = findViewById(R.id.fullName);
        usernameTV = findViewById(R.id.username);
        userDPIV = findViewById(R.id.profilePicture);
        progressBar = findViewById(R.id.PPProgressBar);
        aboutTV = findViewById(R.id.aboutTV);
        aboutTV1 = findViewById(R.id.aboutTV1);
        bioTV = findViewById(R.id.bioTV);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout1);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new QuestionInSearchProfileAdapter(questionList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        backButton = findViewById(R.id.backButton);

        showData();

        getQuestionUids();
    //    getQuestions();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionList.clear();
                questionUidList.clear();
                getQuestionUids();
    //            getQuestions();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter.setOnItemClickListener(new QuestionInSearchProfileAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //    Toast.makeText(getContext(), "Extension Opened Successfully!", Toast.LENGTH_SHORT).show();
                String Question = questionList.get(position).getmQuestion();
                String QuestionUidPassed = questionList.get(position).getmUid();

                Intent intent = new Intent(getApplicationContext(),ExtendedQuestionActivity.class);
                intent.putExtra("Question Passed",Question);
                intent.putExtra("Question Uid Passed",QuestionUidPassed);
                intent.putExtra("Stalking Uid",StalkingUid);
                intent.putExtra("from","Stalking Activity");
                startActivity(intent);
            }
        });
    }
    public void showData(){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot ds : snapshot.getChildren()){
                    if (ds.getKey().equals(StalkingUid)){
                        if (ds.hasChild("username")){
                            usernameTV.setText(ds.child("username").getValue().toString());
                        }
                        if (ds.hasChild("fullName")){
                            String fullName = ds.child("fullName").getValue().toString();
                            fullNameTV.setText(fullName);
                            aboutTV.setText(fullName);
                            aboutTV1.setText(fullName);
                        }
                        if (ds.hasChild("realProfilePhoto")){
                        //    new ImgLoader(userDPIV,progressBar).execute(ds.child("realProfilePhoto").getValue().toString());
                            Picasso.get().load(ds.child("realProfilePhoto").getValue().toString()).into(userDPIV);
                            Picasso.get().load(ds.child("realProfilePhoto").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).into(userDPIV, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(ds.child("realProfilePhoto").getValue().toString()).into(userDPIV);
                                }
                            });
                        }
                        if (ds.hasChild("bio")){
                            bioTV.setText(ds.child("bio").getValue().toString());
                        }
                        if (!ds.hasChild("bio")||ds.child("bio").getValue().toString()==null||ds.child("bio").getValue().toString().isEmpty()||ds.child("bio").getValue().toString().trim().equals("")){
                            bioTV.setText(" ");
                        }
                        progressBar.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StalkingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ImgLoader extends AsyncTask<String, Void, Bitmap> {

        ImageView iv;
        ProgressBar pb;
        Bitmap bmp;

        public ImgLoader(ImageView iv) {
            this.iv = iv;
        }
        public ImgLoader(ImageView iv, ProgressBar pb) {
            this.iv = iv;
            this.pb = pb;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pb != null) {
                pb.setVisibility(View.VISIBLE);
                iv.setImageBitmap(null);
            }
        }
        @Override
        protected Bitmap doInBackground(String... ImgUrl) {
            try {
                URL url = new URL(ImgUrl[0]);
                InputStream is = url.openStream();
                bmp = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            if(pb !=null)
                pb.setVisibility(View.GONE);
            super.onPostExecute(bitmap);
        }
    }
    public void getQuestionUids(){
        questionUidList.clear();
        mRefAns.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds1 : snapshot.getChildren()){
                    for (DataSnapshot ds2 : ds1.getChildren()) {
                        if (ds2.hasChild("answersByUid") && ds2.child("answersByUid").getValue().toString().equals(StalkingUid)) {
                        //    Toast.makeText(StalkingActivity.this, ds1.getKey().toString(), Toast.LENGTH_SHORT).show();
                            if (questionUidList.contains(ds1.getKey())){
                                continue;
                            }
                            questionUidList.add(ds1.getKey());
                        }
                    }
                }
        //        Toast.makeText(StalkingActivity.this, String.valueOf(questionUidList.size()), Toast.LENGTH_SHORT).show();
        //    DatabaseReference mRefQues = FirebaseDatabase.getInstance().getReference("Questions");
                mRefQues.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds1 : snapshot.getChildren()){
                            for (DataSnapshot ds2 : ds1.getChildren()){
                                for (int i = 0 ; i<questionUidList.size() ; i++){
                                    if (ds2.getKey().equals(questionUidList.get(i))){
                                        String question="",noOfAns="0",quesUid="";
                                        if (ds2.hasChild("mNoOfAnswers")){
                                            noOfAns = ds2.child("mNoOfAnswers").getValue().toString();
                                        }
                                        if (ds2.hasChild("mQuestion")){
                                            question = ds2.child("mQuestion").getValue().toString();
                                        }
                                        if (ds2.hasChild("mUid")){
                                            quesUid = ds2.child("mUid").getValue().toString();
                                        }
                                        questionList.add(new QuestionInfo(question,Integer.parseInt(noOfAns),quesUid));
                                    }
                                }
                            }
                        }
                        //        Toast.makeText(getApplicationContext(), String.valueOf(questionList.size()), Toast.LENGTH_SHORT).show();
                        Collections.reverse(questionList);
                        adapter.notifyDataSetChanged();
                        refreshLayout.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StalkingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                refreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StalkingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    //    Log.i("size of list = ", String.valueOf(questionUidList.size()));
    }
    /*public void getQuestions(){
        questionList.clear();
        mRefQues.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds1 : snapshot.getChildren()){
                    for (DataSnapshot ds2 : ds1.getChildren()){
                        for (int i = 0 ; i<questionUidList.size() ; i++){
                            if (ds2.getKey().equals(questionUidList.get(i))){
                                String question="",noOfAns="0",quesUid="";
                                if (ds2.hasChild("mNoOfAnswers")){
                                    noOfAns = ds2.child("mNoOfAnswers").getValue().toString();
                                }
                                if (ds2.hasChild("mQuestion")){
                                    question = ds2.child("mQuestion").getValue().toString();
                                }
                                if (ds2.hasChild("mUid")){
                                    quesUid = ds2.child("mUid").getValue().toString();
                                }
                                questionList.add(new QuestionInfo(question,Integer.parseInt(noOfAns),quesUid));
                            }
                        }
                    }
                }
        //        Toast.makeText(getApplicationContext(), String.valueOf(questionList.size()), Toast.LENGTH_SHORT).show();
                Collections.reverse(questionList);
                adapter.notifyDataSetChanged();
                refreshLayout.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refreshLayout.setRefreshing(false);
    }*/
}
