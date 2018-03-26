package com.uttej.oraclereadersclub.Models;

/**
 * Created by Clean on 26-03-2018.
 */

public class User {
    private String email;
    private String rollnumber;
    private String user_id;
    private String username;

    public User(String email, String rollnumber, String user_id, String username) {
        this.email = email;
        this.rollnumber = rollnumber;
        this.user_id = user_id;
        this.username = username;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollnumber() {
        return rollnumber;
    }

    public void setRollnumber(String rollnumber) {
        this.rollnumber = rollnumber;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", rollnumber='" + rollnumber + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
