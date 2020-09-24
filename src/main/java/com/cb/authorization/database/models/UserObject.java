package com.cb.authorization.database.models;

public class UserObject {

    private String hash;
    private User user;

    public UserObject(String email, User user) {
        this.hash = hash;
        this.user = user;
    }

    public UserObject() {

    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
