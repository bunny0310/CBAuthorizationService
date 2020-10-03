package com.cb.authorization.database.models;

import org.json.simple.JSONObject;

import java.sql.Timestamp;

public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String schoolName;
    private String companyName;

    public User() {

    }
    public User(int id, Timestamp createdAt, Timestamp updatedAt, String firstName, String lastName, String email, String password, String schoolName, String companyName) {
        super(id, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.schoolName = schoolName;
        this.companyName = companyName;
    }

    public void parseObject(JSONObject object) {
        if(object.containsKey("id")) {
            this.setId((int) object.get("id"));
        }
        if(object.containsKey("createdAt")) {
            this.setCreatedAt((Timestamp) object.get("createdAt"));
        }
        if(object.containsKey("updatedAt")) {
            this.setUpdatedAt((Timestamp)object.get("updatedAt"));
        }
        if(object.containsKey("firstName")) {
            this.setFirstName((String)object.get("firstName"));
        }
        if(object.containsKey("lastName")) {
            this.setLastName((String)object.get("lastName"));
        }
        if(object.containsKey("email")) {
            this.setEmail((String)object.get("email"));
        }
        if(object.containsKey("password")) {
            this.setPassword((String)object.get("password"));
        }
        if(object.containsKey("schoolName")) {
            this.setSchoolName((String)object.get("schoolName"));
        }
        if(object.containsKey("companyName")) {
            this.setCompanyName((String)object.get("companyName"));
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
