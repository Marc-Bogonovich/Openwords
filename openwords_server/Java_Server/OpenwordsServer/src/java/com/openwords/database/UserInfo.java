package com.openwords.database;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "user_info")
public class UserInfo {

    private int userId;
    private String username, email, password, lastLocation;
    private Date lastLogin;

    public UserInfo() {
    }

    public UserInfo(String username, String email, String password, String lastLocation, Date lastLogin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        this.lastLogin = lastLogin;
    }

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "user_name")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "last_location")
    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    @Column(name = "last_login")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

}
