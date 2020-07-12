package com.example.iiitb_connects;

public class HomeFeedItems {

    private String userDP;
    private String username;
    private String Img;
    private String description;
    private String postId;

    public HomeFeedItems(String userDP, String username, String Img, String description, String postId) {
        this.userDP = userDP;
        this.username = username;
        this.Img = Img;
        this.description = description;
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserDP() {
        return userDP;
    }

    public String getPostMedia() {
        return Img;
    }

    public String getClubName() {
        return username;
    }

    public String getDescription() {
        return description;
    }
}
