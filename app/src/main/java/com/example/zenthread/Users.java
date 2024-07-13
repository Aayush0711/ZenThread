package com.example.zenthread;

import androidx.annotation.NonNull;

public class Users {
    String profilepic;
    String mail;
    String userName;
    String password;
    String userId;
    String status;

    String lastMessage;

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Users(String userId, String userName, String mail, String password, String profilepic, String status){
        this.userId= userId;
        this.userName= userName;
        this.mail= mail;
        this.password= password;
        this.profilepic= profilepic;
        this.status= status;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

}
