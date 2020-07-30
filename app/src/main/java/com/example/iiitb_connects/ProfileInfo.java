package com.example.iiitb_connects;

public class ProfileInfo {
    String userUid;
    String username;
    String fullName;
    String userDP;

    public ProfileInfo() {
        //Empty Constructor
    }

    public ProfileInfo(String userUid, String username, String fullName ,String userDP) {
        this.userUid = userUid;
        this.username = username;
        this.fullName = fullName;
        this.userDP = userDP;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserDP() {
        return userDP;
    }

    public void setUserDP(String userDP) {
        this.userDP = userDP;
    }


}
