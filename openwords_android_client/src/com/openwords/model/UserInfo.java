package com.openwords.model;

import com.google.gson.Gson;

public class UserInfo {

    private int userId;
    private String userName, pass;
    private long lastLoginTime;
    // Add last user
    private int last_userid;
    // Add variable for learning module language id
    private int lang_id;
    
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
    
    public UserInfo(int lang_id, int userId, String userName, String pass, long lastLoginTime) {
        this.userId = userId;
        this.userName = userName;
        this.pass = pass;
        this.lastLoginTime = lastLoginTime;
        this.lang_id = lang_id;
    }
    
    public UserInfo(int last_userid, int lang_id, int userId, String userName, String pass, long lastLoginTime) {
        this.userId = userId;
        this.userName = userName;
        this.pass = pass;
        this.lastLoginTime = lastLoginTime;
        this.lang_id = lang_id;
        this.setLast_userid(last_userid);
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

	public int getLang_id() {
		return lang_id;
	}

	public void setLang_id(int lang_id) {
		this.lang_id = lang_id;
	}

	public int getLast_userid() {
		return last_userid;
	}

	public void setLast_userid(int last_userid) {
		this.last_userid = last_userid;
	}
}
