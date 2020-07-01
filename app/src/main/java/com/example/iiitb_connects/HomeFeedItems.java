package com.example.iiitb_connects;

public class HomeFeedItems {

    private int userDP;
    private String clubName;
    private int postMedia;
    private String description;

    public HomeFeedItems(int userDP, String clubName, int postMedia, String description) {
        this.userDP = userDP;
        this.clubName = clubName;
        this.postMedia = postMedia;
        this.description = description;
    }

    public int getUserDP() {
        return userDP;
    }

    public int getPostMedia() {
        return postMedia;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDescription() {
        return description;
    }
}
