package com.foodback.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Document("history")
public class QueryHistory {
    @Field(name = "num")
    private String num;

    @Field(name = "account")
    private String account;

    @Field(name = "queryTime")
    private String queryTime;

    @Field(name = "dState")
    private String dState;

    public QueryHistory(){}

    public QueryHistory(String account,String num,String queryTime){
        this.num = num;
        this.account = account;
        this.queryTime = queryTime;
    }
    public QueryHistory(String account,String num,String queryTime,String dState){
        this.num = num;
        this.account = account;
        this.queryTime = queryTime;
        this.dState = dState;
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

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}
