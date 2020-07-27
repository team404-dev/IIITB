package com.example.iiitb_connects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    //SwipeRefreshLayout refreshLayout;
    RecyclerView commentRCV;
    TextInputEditText comment;
    FloatingActionButton addComment;

    //required strings
    String commentAdded; //comment
    String postId; //postId
    String username; //username
    String userDp; //username

    String getUsername, getUserDp;

    //list of recycler view items
    private List<CommentItems> commentItemsList;
    private CommentItemAdapter adapter;

    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mRef;
    DatabaseReference mDRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //init firebase
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Comments");
        String Uid = mAuth.getCurrentUser().getUid();
        mDRef = FirebaseDatabase.getInstance().getReference("Users").child(Uid);

        //Init postId
        postId = getIntent().getStringExtra("postId");
        mRef = mRef.child(postId);

        getUsername = MainActivity.sharedPreferences.getString("username", "Hacker404"); //init username

        //init userDp
        mDRef.child("templateProfilePhoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null)
                getUserDp = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Init comment
        closeBtn = findViewById(R.id.closeBtn);
        commentRCV = findViewById(R.id.commentRCV);
        comment = findViewById(R.id.comment);
        addComment = findViewById(R.id.addComment);
        commentRCV = findViewById(R.id.commentRCV);
        //refreshLayout = findViewById(R.id.refreshLayout);

        //setting up layout managers
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRCV.setLayoutManager(layoutManager);

        commentItemsList = new ArrayList<>();

        adapter = new CommentItemAdapter(commentItemsList, CommentsActivity.this);
        commentRCV.setAdapter(adapter);

        //refresh layout
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentItemsList.clear();
                loadData();
            }
        });*/

        //load comments
        if(savedInstanceState==null) {
            loadData();
        }

        //onClick
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText()==null || comment.getText().toString().trim().equals(""))
                    Toast.makeText(CommentsActivity.this, "Cannot add empty comment!", Toast.LENGTH_SHORT).show();
                else {
                    //add a comment
                    DatabaseReference mDatabaseRef = mRef.push();
                    commentAdded = comment.getText().toString().trim();
                    comment.setText("");
                    mDatabaseRef.setValue(new commentInfo(getUsername, getUserDp, commentAdded));
                    commentItemsList.clear();
                    loadData();
                }
            }
        });
    }

    public class commentInfo {
        public String username;
        public String userDp;
        public String comment;

        public commentInfo(String username, String userDp, String comment) {
            this.username = username;
            this.userDp = userDp;
            this.comment = comment;
        }
    }

    private void loadData() {
        commentItemsList.clear();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    commentRCV.scrollToPosition(commentItemsList.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //refreshLayout.setRefreshing(false);
    }
}
