package com.example.iiitb_connects;

public class CommentItems {

    private String username;
    private String userDp;
    private String comments;

    public CommentItems(String username, String userDp, String comments) {
        this.username = username;
        this.userDp = userDp;
        this.comments = comments;
    }

    public String getUserDp() {
        return userDp;
    }

    public String getUsername() {
        return username;
    }

    public String getComments() {
        return comments;
    }
}
