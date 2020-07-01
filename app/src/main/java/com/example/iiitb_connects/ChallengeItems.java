package com.example.iiitb_connects;

public class ChallengeItems {

    private int challengeTemplateImg;
    private String challengeName;

    public ChallengeItems(int challengeTemplateImg, String challengeName) {
        this.challengeTemplateImg = challengeTemplateImg;
        this.challengeName = challengeName;
    }

    public int getChallengeTemplateImg() {
        return challengeTemplateImg;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
