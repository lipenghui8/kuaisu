package com.foodback.demo.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("chainGoods")
public class UserGoods {
    @Field(name = "num")
    private String num;

    @Field(name = "account")
    private String account;

    public UserGoods(){}

    public UserGoods(String account,String num ){
        this.num = num;
        this.account = account;
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
}
