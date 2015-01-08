package com.openwords.database;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static boolean checkUserName(Session s, String username) {
        int exist = ((Number) s.createCriteria(UserInfo.class)
                .add(Restrictions.eq("username", username))
                .setProjection(Projections.rowCount()
                ).uniqueResult()).intValue();

        return exist <= 0;
    }

    public static boolean checkEmail(Session s, String email) {
        int exist = ((Number) s.createCriteria(UserInfo.class)
                .add(Restrictions.eq("email", email))
                .setProjection(Projections.rowCount()
                ).uniqueResult()).intValue();

        return exist <= 0;
    }

    public static boolean loginUser(Session s, String username, String password) {
        int exist = ((Number) s.createCriteria(UserInfo.class)
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password))
                .setProjection(Projections.rowCount()
                ).uniqueResult()).intValue();

        return exist >= 1;
    }

    public synchronized static int addUser(Session s, String username, String password, String email) {
        UserInfo user = new UserInfo(username, email, password, "", new Date());
        s.save(user);
        s.beginTransaction().commit();
        return user.getUserId();
    }

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
