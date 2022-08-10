package com.foodback.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("dangerUser")
public class DangerUser {
    @Field(name = "num")
    private String num;

    @Field(name = "account")
    private String account;

    @Field(name = "location")
    private String location;

    public DangerUser(){}

    public DangerUser(String account,String num,String location ){
        this.num = num;
        this.account = account;
        this.location = location;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
