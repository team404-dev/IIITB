package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class CommentsActivity extends AppCompatActivity {

    //Views
    ImageView closeBtn;
    SwipeRefreshLayout refreshLayout;
    RecyclerView commentRCV;
    TextInputEditText comment;
    FloatingActionButton addComment;

    //required strings
    String commentAdded; //comment
    String postId; //postId
    String username; //username
    String userDp; //username

    //list of recycler view items
    private List<CommentItems> commentItemsList;
    private CommentItemAdapter adapter;

    //Firebase
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("posts");
    DatabaseReference mDRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //Init postId
        postId = getIntent().getStringExtra("postId");
        mRef = mRef.child(postId).child("postsInfo");

        username = MainActivity.sharedPreferences.getString("username", "Hacker404"); //init username
        mDRef.child("templateProfilePhoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDp = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); //init userDp

        //Init comment
        closeBtn = findViewById(R.id.closeBtn);
        commentRCV = findViewById(R.id.commentRCV);
        comment = findViewById(R.id.comment);
        addComment = findViewById(R.id.addComment);
        commentRCV = findViewById(R.id.commentRCV);
        refreshLayout = findViewById(R.id.refreshLayout);

        //setting up layout managers
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRCV.setLayoutManager(layoutManager);

        commentItemsList = new ArrayList<>();

        adapter = new CommentItemAdapter(commentItemsList);
        commentRCV.setAdapter(adapter);

        //refresh layout
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentItemsList.clear();
                loadData();
            }
        });

        //load comments
        if(savedInstanceState==null) {
            loadData();
        }

        //onClick
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText()==null || comment.getText().toString().trim().equals(""))
                    Toast.makeText(CommentsActivity.this, "Cannot add empty comment!", Toast.LENGTH_SHORT).show();
                else {
                    //add a comment
                    DatabaseReference mDatabaseRef = mRef.child("comments").push();
                    commentAdded = comment.getText().toString().trim();
                    comment.setText("");
                    mDatabaseRef.child("username").setValue(username);
                    mDatabaseRef.child("userDp").setValue(userDp);
                    mDatabaseRef.child("comment").setValue(commentAdded);
                    commentItemsList.clear();
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        mRef.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    username=null; userDp=null; commentAdded=null;
                    if(ds.hasChild("username")) {
                        username = ds.child("username").getValue().toString();
                    }
                    if(ds.hasChild("userDp")) {
                        userDp = ds.child("userDp").getValue().toString();
                    }
                    if(ds.hasChild("comment")) {
                        commentAdded = ds.child("comment").getValue().toString();
                    }
                    commentItemsList.add(new CommentItems(username, userDp, commentAdded));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refreshLayout.setRefreshing(false);
    }
}
