package com.openwords.model;

import com.google.gson.Gson;

public class UserInfo {

    private int userId;
    private String userName, pass;
    private long lastLoginTime;

    public UserInfo(String userName, String pass) {
        this.userName = userName;
        this.pass = pass;
    }

    public UserInfo(String userName, String pass, long lastLoginTime) {
        this.userName = userName;
        this.pass = pass;
        this.lastLoginTime = lastLoginTime;
    }

    public UserInfo(int userId, String userName, String pass, long lastLoginTime) {
        this.userId = userId;
        this.userName = userName;
        this.pass = pass;
        this.lastLoginTime = lastLoginTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}