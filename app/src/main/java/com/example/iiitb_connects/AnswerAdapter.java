package com.example.iiitb_connects;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AnsweredByInfo currentItem = this.answeredByList.get(position);
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
        //    holder.answeredByTextView.setText(answerer[0]);
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