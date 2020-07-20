package com.example.iiitb_connects;

public class AnsweredByInfo {
    String answer;
    String answersByUid;
    String answeredByName;
    String answerUid;

    public AnsweredByInfo(String answer, String answersByUid , String answeredByName , String answerUid) {
        this.answer = answer;
        this.answersByUid = answersByUid;
        this.answeredByName = answeredByName;
        this.answerUid = answerUid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswersByUid() {
        return answersByUid;
    }

    public void setAnswersByUid(String answersByUid) {
        this.answersByUid = answersByUid;
    }

    public String getAnsweredByName() {
        return answeredByName;
    }

    public void setAnsweredByName(String answeredByName) {
        this.answeredByName = answeredByName;
    }

    public String getAnswerUid() {
        return answerUid;
    }

    public void setAnswerUid(String answerUid) {
        this.answerUid = answerUid;
    }
}