package com.example.iiitb_connects;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> implements Filterable {
    private ArrayList<QuestionInfo> mQuestionList;
    private ArrayList<QuestionInfo> mQuestionListFull;
    private onItemClickListener listener;
    String userUidForNoOfAns;
    Activity context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewQuestion;
        public TextView mTextViewNoOfAnswers;
        RelativeLayout cardRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.questionLogoImageView);
            mTextViewQuestion = itemView.findViewById(R.id.questionTextView);
            mTextViewNoOfAnswers = itemView.findViewById(R.id.noOfAnswersTextView);
            cardRelativeLayout = itemView.findViewById(R.id.cardRelativeLayout);

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

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public QuestionAdapter(ArrayList<QuestionInfo> questionList , Activity context) {
        mQuestionList = questionList;
        mQuestionListFull = new ArrayList<>(questionList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card,parent,false);
        ViewHolder qvh = new ViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionAdapter.ViewHolder holder, int position) {
        final QuestionInfo currentItem = this.mQuestionList.get(position);

        String qu = currentItem.getmQuestion();
        int questionSize = qu.length();
        if (questionSize >= 142){
            qu = qu.substring(0,143) + "....";
        }

        holder.mTextViewQuestion.setText(qu);
        final String questionUid = currentItem.getmUid();
        final String[] noOfAnswers = new String[1];
        DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference("Questions Asked");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    userUidForNoOfAns = ds.getKey();
                    if (ds.hasChild(questionUid)){
                        noOfAnswers[0] = ds.child(questionUid).child("mNoOfAnswers").getValue().toString();
                    }
                }
                holder.mTextViewNoOfAnswers.setText(noOfAnswers[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cardRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Question = currentItem.getmQuestion();
                String QuestionUidPassed = currentItem.getmUid();

                Intent intent = new Intent(context,ExtendedQuestionActivity.class);
                intent.putExtra("Question Passed",Question);
                intent.putExtra("Question Uid Passed",QuestionUidPassed);
                intent.putExtra("from","allQuestions");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<QuestionInfo> filterdList= new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filterdList.addAll(mQuestionListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (QuestionInfo ques : mQuestionListFull){
                    if(ques.getmQuestion().toLowerCase().contains(filterPattern)){
                        filterdList.add(ques);
                    //    ques.questionTextView.setText(highlightText(filterPattern,ques.getmQuestion()).toString());
                    //    notifyDataSetChanged();
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mQuestionList.clear();
            mQuestionList.addAll((Collection<? extends QuestionInfo>) results.values);
            notifyDataSetChanged();
        }
    };

}
