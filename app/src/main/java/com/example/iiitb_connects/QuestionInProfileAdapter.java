package com.example.iiitb_connects;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionInProfileAdapter extends RecyclerView.Adapter<QuestionInProfileAdapter.ViewHolder1> implements Filterable {
    private ArrayList<QuestionInfo> mQuestionList;
    private ArrayList<QuestionInfo> mQuestionListFull;
    private onItemClickListener listener;

    public class ViewHolder1 extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewQuestion;
        public TextView mTextViewNoOfAnswers;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.questionLogoImageView);
            mTextViewQuestion = itemView.findViewById(R.id.questionTextView);
            mTextViewNoOfAnswers = itemView.findViewById(R.id.noOfAnswersTextView);

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

    public QuestionInProfileAdapter(ArrayList<QuestionInfo> questionList) {
        mQuestionList = questionList;
        mQuestionListFull = new ArrayList<>(questionList);
    }

    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.own_question_card,parent,false);
        ViewHolder1 qvh = new ViewHolder1(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionInProfileAdapter.ViewHolder1 holder, int position) {
        QuestionInfo currentItem = this.mQuestionList.get(position);

        String qu = currentItem.getmQuestion();
        int questionSize = qu.length();
        if (questionSize >= 65){
            qu = qu.substring(0,66) + "....";
        }

        holder.mTextViewQuestion.setText(qu);
        holder.mTextViewNoOfAnswers.setText(String.valueOf(currentItem.getmNoOfAnswers()));
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

    /*public static CharSequence highlightText(String search, String originalText) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }  */
}
