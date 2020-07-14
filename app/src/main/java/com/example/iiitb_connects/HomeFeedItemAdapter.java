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
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomeFeedItemAdapter
        extends RecyclerView.Adapter<HomeFeedItemAdapter.ViewHolder> {

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
        private ProgressBar progressBar;

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
            progressBar = itemView.findViewById(R.id.progressBar);
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

    long likesCount = 0;
    String postId;
    @Override
    public void onBindViewHolder(@NonNull final HomeFeedItemAdapter.ViewHolder holder, final int position) {
        HomeFeedItems homeFeedItems = this.homeFeedItems.get(position);

        postId = homeFeedItems.getPostId();
        mRef.child(postId).child("noOfLikes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    likesCount = Long.parseLong(snapshot.getValue().toString());
                    String text = "Liked by "+snapshot.getValue().toString()+" people";
                    holder.likesCounter.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Setting up item views

        Picasso.with(context).load(homeFeedItems.getUserDP()).into(holder.userDP);
        //new ImgLoader(holder.userDP).execute(homeFeedItems.getUserDP());
        holder.clubName.setText(homeFeedItems.getUsername());
        new ImgLoader(holder.postMedia, holder.progressBar).execute(homeFeedItems.getPostMedia());
        holder.description.setText(homeFeedItems.getDescription());
        //onClick for item buttons
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeButton.setVisibility(View.GONE);
                holder.unlikedButton.setVisibility(View.VISIBLE);
            }
        });
        holder.unlikedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unlikedButton.setVisibility(View.GONE);
                holder.likeButton.setVisibility(View.VISIBLE);
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
                intent.putExtra("postId", postId);
                context.startActivity(intent);
            }
        });

        if(holder.likeButton.getVisibility() == View.VISIBLE) {
            mRef.child(postId).child(uidOfUser).setValue("1");
            mRef.child(postId).child("noOfLikes").setValue(String.valueOf(likesCount+1));
        }
    }

    @Override
    public int getItemCount() {
        return homeFeedItems.size();
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
        protected void onProgressUpdate(Void... values) {
            if(pb!=null)
                pb.setVisibility(View.VISIBLE);
            super.onProgressUpdate(values);
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

}
