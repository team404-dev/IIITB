package com.example.iiitb_connects;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChallengeItemAdapter
        extends RecyclerView.Adapter<ChallengeItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView challengeIV;
        private TextView challengeName;
        private RelativeLayout challenge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeIV = itemView.findViewById(R.id.challengeIV);
            challengeName = itemView.findViewById(R.id.challengeName);
            challenge = itemView.findViewById(R.id.challenge);
        }
    }

    private List<ChallengeItems> challengeItems;
    private Activity context;

    public ChallengeItemAdapter(List<ChallengeItems> challengeItems, Activity context) {
        this.challengeItems = challengeItems;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ChallengeItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_challenge, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChallengeItemAdapter.ViewHolder holder, int position) {
        final ChallengeItems challengeItems = this.challengeItems.get(position);
        Picasso.get().load(challengeItems.getTemplateImg()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.challengeIV, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(challengeItems.getTemplateImg()).into(holder.challengeIV);
            }
        });
        holder.challengeName.setText(challengeItems.getChallengeName());

        holder.challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(holder.challengeIV, "imageTransition");
                pairs[1] = new Pair<View, String>(holder.challengeName, "nameTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, pairs);
                Intent intent = new Intent(context, ChallengeDetails.class);
                intent.putExtra("templateImg", challengeItems.getTemplateImg());
                intent.putExtra("challengeName", challengeItems.getChallengeName());
                intent.putExtra("clubName", challengeItems.getClubName());
                intent.putExtra("description", challengeItems.getDescription());
                intent.putExtra("challengeID", challengeItems.getChallengeID());
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return challengeItems.size();
    }
}
