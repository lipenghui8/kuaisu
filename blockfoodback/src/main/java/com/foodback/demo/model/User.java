package com.foodback.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Document("user")
public class User {
//    private int id;

    @Field(name = "username")
    private String username;

    @Field(name = "account")
    private String account;

    @Field(name = "password")
    private String password;

    @Field(name = "worktype")
    private String worktype;

    @Field(name = "avator")
    private String avator;

    @Field(name = "location")
    private String location;

    @Field(name = "email")
    private String email;

    public User() { }

    public User(String username, String account, String password, String worktype, String location, String email) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.worktype = worktype;
        this.location = location;
        this.email = email;
    }

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
