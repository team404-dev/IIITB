package com.example.iiitb_connects;

import android.widget.TextView;

public class QuestionInfo {
    //private int mImageRes;
    private String mQuestion;
    private int mNoOfAnswers;
    private String mUid;

    //View
    TextView questionTextView;

    public QuestionInfo() {
    }

    public QuestionInfo(String mQuestion, int mNoOfAnswers , String mUid) {
        this.mQuestion = mQuestion;
        this.mNoOfAnswers = mNoOfAnswers;
        this.mUid = mUid;
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

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}