package com.example.iiitb_connects;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UserInfo implements Serializable {
    public String email,fullName,username;
    public boolean isVerified;

    public UserInfo(String email, String fullName, String username ,boolean isVerified) {
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.isVerified = isVerified;
    }

    public UserInfo() {
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.isVerified = isVerified;
    }
    ////----------------------------------Getter and Setter---------------------------------------
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
