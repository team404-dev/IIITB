package com.example.iiitb_connects;

public class HomeFeedItems {

    private String userDP;
    private String username;
    private String Img;
    private String description;
    private String postId;
    private String uid;

    /*public HomeFeedItems(String username, String userDP, String Img, String description, String postId) {
        this.username = username;
        this.userDP = userDP;
        this.Img = Img;
        this.description = description;
        this.postId = postId;
    }*/

    public HomeFeedItems(String uid, String Img, String description, String postId) {
        this.uid = uid;
        this.Img = Img;
        this.description = description;
        this.postId = postId;
    }

    /*public String getUserDP() {
        return userDP;
    }

    public String getUsername() {
        return username;
    }*/

    public String getUid() {
        return uid;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostMedia() {
        return Img;
    }

    public String getDescription() {
        return description;
    }
}
