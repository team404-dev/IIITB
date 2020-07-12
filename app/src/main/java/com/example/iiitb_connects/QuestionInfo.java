package com.example.iiitb_connects;

import android.widget.TextView;

public class QuestionInfo {
    //private int mImageRes;
    private String mQuestion;
    private int mNoOfAnswers;

    //View
    TextView questionTextView;

    public QuestionInfo() {
    }

    public QuestionInfo(String mQuestion, int mNoOfAnswers) {
        this.mQuestion = mQuestion;
        this.mNoOfAnswers = mNoOfAnswers;
    }


    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public int getmNoOfAnswers() {
        return mNoOfAnswers;
    }

    public void setmNoOfAnswers(int mNoOfAnswers) {
        this.mNoOfAnswers = mNoOfAnswers;
    }
}
