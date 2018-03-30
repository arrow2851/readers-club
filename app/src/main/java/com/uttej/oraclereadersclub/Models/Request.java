package com.uttej.oraclereadersclub.Models;

/**
 * Created by Clean on 30-03-2018.
 */

public class Request {

    private String user_id;

    public Request(String user_id) {
        this.user_id = user_id;
    }

    public Request(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Request{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
