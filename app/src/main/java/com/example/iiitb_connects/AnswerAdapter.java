package com.example.iiitb_connects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    Context context;
    ArrayList<AnsweredByInfo> answeredByList;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnsweredByInfo currentItem = this.answeredByList.get(position);
        holder.answerTextView.setText(currentItem.getAnswer());
        DatabaseReference mRef;
    //    mRef = FirebaseDatabase.getInstance().getReference()
        holder.answeredByTextView.setText(currentItem.getAnsweredByName());
    }

    @Override
    public int getItemCount() {
        return answeredByList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView answerTextView;
        TextView answeredByTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
            answeredByTextView = itemView.findViewById(R.id.answeredByTextView);
        }
    }

}