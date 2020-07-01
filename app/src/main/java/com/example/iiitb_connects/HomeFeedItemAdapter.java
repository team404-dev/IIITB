package com.example.iiitb_connects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFeedItemAdapter
        extends RecyclerView.Adapter<HomeFeedItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userDP;
        private TextView clubName;
        private ImageView postMedia;
        private TextView description;
        private ImageView likeButton, unlikedButton, commentButton, infoButton;
        private RelativeLayout descriptionLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDP = itemView.findViewById(R.id.userDP);
            clubName = itemView.findViewById(R.id.clubName);
            postMedia = itemView.findViewById(R.id.postMedia);
            description = itemView.findViewById(R.id.description);
            likeButton = itemView.findViewById(R.id.likeButton);
            unlikedButton = itemView.findViewById(R.id.unlikedButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
        }
    }

    private List<HomeFeedItems> homeFeedItems;

    public HomeFeedItemAdapter(List<HomeFeedItems> homeFeedItems) {
        this.homeFeedItems = homeFeedItems;
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
    public void onBindViewHolder(@NonNull final HomeFeedItemAdapter.ViewHolder holder, int position) {
        HomeFeedItems homeFeedItems = this.homeFeedItems.get(position);

        //Setting up item views
        holder.userDP.setImageResource(homeFeedItems.getUserDP());
        holder.clubName.setText(homeFeedItems.getClubName());
        holder.postMedia.setImageResource(homeFeedItems.getPostMedia());
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeFeedItems.size();
    }
}
