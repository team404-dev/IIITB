package com.example.iiitb_connects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChallengeItemAdapter
        extends RecyclerView.Adapter<ChallengeItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView challengeIV;
        private TextView challengeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeIV = itemView.findViewById(R.id.challengeIV);
            challengeName = itemView.findViewById(R.id.challengeName);
        }
    }

    private List<ChallengeItems> challengeItems;

    public ChallengeItemAdapter(List<ChallengeItems> challengeItems) {
        this.challengeItems = challengeItems;
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
    public void onBindViewHolder(@NonNull ChallengeItemAdapter.ViewHolder holder, int position) {
        ChallengeItems challengeItems = this.challengeItems.get(position);
        holder.challengeIV.setImageResource(challengeItems.getChallengeTemplateImg());
        holder.challengeName.setText(challengeItems.getChallengeName());
    }

    @Override
    public int getItemCount() {
        return challengeItems.size();
    }
}
