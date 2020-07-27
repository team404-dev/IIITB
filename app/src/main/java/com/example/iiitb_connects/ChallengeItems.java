package com.example.iiitb_connects;

public class ChallengeItems {

    private String clubName;
    private String templateImg;
    private String challengeName;
    private String description;
    private String challengeID;

    public ChallengeItems(String clubName, String templateImg, String challengeName, String description, String challengeID) {
        this.clubName = clubName;
        this.templateImg = templateImg;
        this.challengeName = challengeName;
        this.description = description;
        this.challengeID = challengeID;
    }

    public String getChallengeID() {
        return challengeID;
    }

    public String getClubName() {
        return clubName;
    }

    public String getTemplateImg() {
        return templateImg;
    }

    public String getDescription() {
        return description;
    }

    public String getChallengeName() {
        return challengeName;
    }
}
