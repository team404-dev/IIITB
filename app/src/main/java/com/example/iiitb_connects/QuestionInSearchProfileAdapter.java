package com.example.iiitb_connects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionInSearchProfileAdapter extends RecyclerView.Adapter<QuestionInSearchProfileAdapter.ViewHolder> {
    ArrayList<QuestionInfo> mQuestionList;
    private QuestionInSearchProfileAdapter.onItemClickListener listener;

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView mQuestionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mQuestionTV = itemView.findViewById(R.id.questionTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(QuestionInSearchProfileAdapter.onItemClickListener listener){
        this.listener = listener;
    }

    public QuestionInSearchProfileAdapter(ArrayList<QuestionInfo> questionList){
        mQuestionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_in_search_profile_card,parent,false);
        ViewHolder qvh = new ViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionInSearchProfileAdapter.ViewHolder holder, int position) {
        QuestionInfo currentItem = this.mQuestionList.get(position);

        holder.mQuestionTV.setText(currentItem.getmQuestion());
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

}
