package com.example.iiitb_connects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomeFeedItemAdapter
        extends RecyclerView.Adapter<HomeFeedItemAdapter.ViewHolder> {

    //Firebase
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Likes");
    String uidOfUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userDP;
        private TextView clubName;
        private TextView likesCounter;
        private ImageView postMedia;
        private TextView description;
        private ImageView likeButton, unlikedButton, commentButton, infoButton;
        private RelativeLayout descriptionLayout;
        private RelativeLayout postTitles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDP = itemView.findViewById(R.id.userDP);
            clubName = itemView.findViewById(R.id.clubName);
            likesCounter = itemView.findViewById(R.id.likesCounter);
            postMedia = itemView.findViewById(R.id.postMedia);
            description = itemView.findViewById(R.id.description);
            likeButton = itemView.findViewById(R.id.likeButton);
            unlikedButton = itemView.findViewById(R.id.unlikedButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            postTitles = itemView.findViewById(R.id.postTitles);
        }
    }

    private List<HomeFeedItems> homeFeedItems;
    Context context;

    public HomeFeedItemAdapter(List<HomeFeedItems> homeFeedItems, Context context) {
        this.homeFeedItems = homeFeedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeFeedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_feed, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeFeedItemAdapter.ViewHolder holder, final int position) {
        final HomeFeedItems homeFeedItems = this.homeFeedItems.get(position);
        final long[] likesCount = new long[1];

        mRef.child(homeFeedItems.getPostId()).child("noOfLikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                likesCount[0] = Long.parseLong(snapshot.getValue().toString());
                String text = "Liked by "+ likesCount[0] +" people";
                holder.likesCounter.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child(homeFeedItems.getPostId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uidOfUser) && snapshot.child(uidOfUser).getValue().toString().equals("1")) {
                    holder.likeButton.setVisibility(View.VISIBLE);
                    holder.unlikedButton.setVisibility(View.GONE);
                } else {
                    holder.likeButton.setVisibility(View.GONE);
                    holder.unlikedButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Setting up item views
        setUserInfo(homeFeedItems.getUid(), holder.userDP, holder.clubName);
        /*if (homeFeedItems.getUserDP() != null){
            Picasso.get().load(homeFeedItems.getUserDP()).into(holder.userDP);
        }
        holder.clubName.setText(homeFeedItems.getUsername());*/
        Picasso.get().load(homeFeedItems.getPostMedia()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.postMedia, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(homeFeedItems.getPostMedia()).into(holder.postMedia);
                    }
                });
        holder.description.setText(homeFeedItems.getDescription());

        //onClick for item buttons
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeButton.setVisibility(View.GONE);
                holder.unlikedButton.setVisibility(View.VISIBLE);
                mRef.child(homeFeedItems.getPostId()).child(uidOfUser).setValue("0");
                mRef.child(homeFeedItems.getPostId()).child("noOfLikes").setValue(likesCount[0]-1);
            }
        });
        holder.unlikedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unlikedButton.setVisibility(View.GONE);
                holder.likeButton.setVisibility(View.VISIBLE);
                mRef.child(homeFeedItems.getPostId()).child(uidOfUser).setValue("1");
                mRef.child(homeFeedItems.getPostId()).child("noOfLikes").setValue(likesCount[0]+1);
            }
        });
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.descriptionLayout.getVisibility() == View.GONE)
                    holder.descriptionLayout.setVisibility(View.VISIBLE);
                else
                    holder.descriptionLayout.setVisibility(View.GONE);
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes to comment section
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postId", homeFeedItems.getPostId());
                context.startActivity(intent);
            }
        });
        holder.postTitles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StalkingActivity.class);
                intent.putExtra("User Uid",homeFeedItems.getUid());
                context.startActivity(intent);
            }
        });
    }

    private void setUserInfo(String uid, final ImageView userDP, final TextView clubName) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(uid!=null) {
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    if (snapshot.hasChild("username")) {
                        clubName.setText(snapshot.child("username").getValue().toString());
                    }
                    if (snapshot.hasChild("templateProfilePhoto") && snapshot.child("templateProfilePhoto").getValue() != null) {
                        Picasso.get().load(snapshot.child("templateProfilePhoto").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).into(userDP, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(snapshot.child("templateProfilePhoto").getValue().toString()).into(userDP);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return homeFeedItems.size();
    }

}
