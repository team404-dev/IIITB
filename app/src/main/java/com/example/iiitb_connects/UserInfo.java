package com.example.iiitb_connects;

import android.net.Uri;

public class UserInfo {

    public String username;
    public String fullName;
    public String emailId;
    public Uri profilePhotoUri;

    public UserInfo(String username, String fullName, String emailId) {
        this.username = username;
        this.fullName = fullName;
        this.emailId = emailId;
    }

}
