package com.example.iiitb_connects;

public class QuestionInfo {

    public String questionString;
    public int numberOfAnswers;

    //Constructor
    public QuestionInfo(String questionString, int numberOfAnswers) {
        this.questionString = questionString;
        this.numberOfAnswers = numberOfAnswers;
    }


    //Getter and Setter methods
    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    public int getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(int numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }
}
