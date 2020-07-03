package com.example.iiitb_connects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    Context context;
    ArrayList<QuestionInfo> questions;

    public QuestionsAdapter (Context c, ArrayList<QuestionInfo> q){
        context = c;
        questions = q;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.question_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionInCard.setText(questions.get(position).getQuestionString());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView questionInCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionInCard = itemView.findViewById(R.id.questionInCard);
        }
    }
}
