package com.example.iiitb_connects;

public class ChallengeItems {

    private String clubName;
    private String templateImg;
    private String challengeName;
    private String description;

    public ChallengeItems(String clubName, String templateImg, String challengeName, String description) {
        this.clubName = clubName;
        this.templateImg = templateImg;
        this.challengeName = challengeName;
        this.description = description;
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
