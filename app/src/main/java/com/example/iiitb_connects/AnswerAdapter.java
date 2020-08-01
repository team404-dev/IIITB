package com.example.iiitb_connects;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    Context context;
    ArrayList<AnsweredByInfo> answeredByList;
    DatabaseReference mRefAnswerVotes;

    public AnswerAdapter (Context c, ArrayList<AnsweredByInfo> a){
        context = c;
        answeredByList = a;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.answer_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final AnsweredByInfo currentItem = this.answeredByList.get(position);
        holder.answerTextView.setText(currentItem.getAnswer());
        final String answerUid = currentItem.getAnswerUid();
        final String[] answerer = new String[1];
        answerer[0] = "Loading...Please Wait";
        DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference("Answers");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.hasChild(answerUid)){
                        answerer[0] = ds.child(answerUid).child("answeredByName").getValue().toString();
                        //    Toast.makeText(context, "answerer = "+answerer[0], Toast.LENGTH_SHORT).show();
                        holder.answeredByTextView.setText(answerer[0]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.answeredByTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = currentItem.answersByUid;
                Intent intent = new Intent(context,StalkingActivity.class);
                intent.putExtra("User Uid",uid);
                context.startActivity(intent);
            }
        });

        mRefAnswerVotes = FirebaseDatabase.getInstance().getReference("Answer Votes");

        final long[] upvoteCount = new long[1];
        final long[] downvoteCount = new long[1];

        //Votes Counter
        mRefAnswerVotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Upvotes")){
                    if (snapshot.child("Upvotes").hasChild(currentItem.answerUid)){
                        if (snapshot.child("Upvotes").child(currentItem.answerUid).hasChild("noOfUpvotes")){
                            upvoteCount[0] = Long.parseLong(snapshot.child("Upvotes").child(currentItem.answerUid).child("noOfUpvotes").getValue().toString());
                        }
                    }
                }
                if (snapshot.hasChild("Downvotes")){
                    if (snapshot.child("Downvotes").hasChild(currentItem.answerUid)){
                        if (snapshot.child("Downvotes").child(currentItem.answerUid).hasChild("noOfDownvotes")){
                            downvoteCount[0] = Long.parseLong(snapshot.child("Downvotes").child(currentItem.answerUid).child("noOfDownvotes").getValue().toString());
                        }
                    }
                }
                holder.voteNumTV.setText(String.valueOf(upvoteCount[0]-downvoteCount[0]));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Checking which button-type to show
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(mAuth.getCurrentUser().getUid())
                                && snapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString().equals("1")){
                            holder.upvoteClicked.setVisibility(View.VISIBLE);
                            holder.upvoteUnclicked.setVisibility(View.GONE);
                        } else{
                            holder.upvoteClicked.setVisibility(View.GONE);
                            holder.upvoteUnclicked.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(mAuth.getCurrentUser().getUid())
                                && snapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString().equals("1")){
                            holder.downvoteClicked.setVisibility(View.VISIBLE);
                            holder.downvoteUnclicked.setVisibility(View.GONE);
                        } else{
                            holder.downvoteClicked.setVisibility(View.GONE);
                            holder.downvoteUnclicked.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        //Handling onClick on Voting-Icons
        holder.upvoteUnclicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.upvoteUnclicked.setVisibility(View.GONE);
                holder.upvoteClicked.setVisibility(View.VISIBLE);
                mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                        .child(mAuth.getCurrentUser().getUid()).setValue("1");
                mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                        .child("noOfUpvotes").setValue(upvoteCount[0]+1);
                mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(mAuth.getCurrentUser().getUid())
                                        && snapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString().equals("1")){
                                    holder.downvoteUnclicked.setVisibility(View.VISIBLE);
                                    holder.downvoteClicked.setVisibility(View.GONE);
                                    mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                                            .child(mAuth.getCurrentUser().getUid()).setValue("0");
                                    mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                                            .child("noOfDownvotes").setValue(downvoteCount[0]-1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        holder.upvoteClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.upvoteUnclicked.setVisibility(View.VISIBLE);
                holder.upvoteClicked.setVisibility(View.GONE);
                mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                        .child(mAuth.getCurrentUser().getUid()).setValue("0");
                mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                        .child("noOfUpvotes").setValue(upvoteCount[0]-1);
            }
        });
        holder.downvoteUnclicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.downvoteUnclicked.setVisibility(View.GONE);
                holder.downvoteClicked.setVisibility(View.VISIBLE);
                mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                        .child(mAuth.getCurrentUser().getUid()).setValue("1");
                mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                        .child("noOfDownvotes").setValue(downvoteCount[0]+1);
                mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(mAuth.getCurrentUser().getUid())
                                        && snapshot.child(mAuth.getCurrentUser().getUid()).getValue().toString().equals("1")){
                                    holder.upvoteUnclicked.setVisibility(View.VISIBLE);
                                    holder.upvoteClicked.setVisibility(View.GONE);
                                    mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                                            .child(mAuth.getCurrentUser().getUid()).setValue("0");
                                    mRefAnswerVotes.child("Upvotes").child(currentItem.getAnswerUid())
                                            .child("noOfUpvotes").setValue(upvoteCount[0]-1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        holder.downvoteClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.downvoteUnclicked.setVisibility(View.VISIBLE);
                holder.downvoteClicked.setVisibility(View.GONE);
                mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                        .child(mAuth.getCurrentUser().getUid()).setValue("0");
                mRefAnswerVotes.child("Downvotes").child(currentItem.getAnswerUid())
                        .child("noOfDownvotes").setValue(downvoteCount[0]-1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answeredByList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView answerTextView;
        TextView answeredByTextView;
        ImageView upvoteClicked,upvoteUnclicked,downvoteClicked,downvoteUnclicked;
        TextView voteNumTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
            answeredByTextView = itemView.findViewById(R.id.answeredByTextView);
            upvoteClicked = itemView.findViewById(R.id.upvoteClicked);
            upvoteUnclicked = itemView.findViewById(R.id.upvoteUnclicked);
            downvoteClicked = itemView.findViewById(R.id.downvoteClicked);
            downvoteUnclicked = itemView.findViewById(R.id.downvoteUnclicked);
            voteNumTV = itemView.findViewById(R.id.voteNumTV);
        }
    }

}