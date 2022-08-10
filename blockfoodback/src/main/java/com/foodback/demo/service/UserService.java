package com.foodback.demo.service;

import com.foodback.demo.model.*;

import java.util.List;

public interface UserService {
    public int register(User user);
    public boolean accountDetection(String account);
    public User selectByAccount(String account);
    public int addGoods(UserGoods userGoods);
    public int addHistory(QueryHistory queryHistory);
    public PageVo<QueryHistory> getQueryHistory(String account, Integer current, Integer size);
    public PageVo<UserGoods> getUserGoods(String account,Integer current,Integer size);
    public int addDangerUser(DangerUser dangerUser);

}
