package com.foodback.demo.service.Impl;

import com.foodback.demo.Mapper.UserMapper;
import com.foodback.demo.model.*;
import com.foodback.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {
        int i = userMapper.insertUser(user);
        return i;
    }

    @Override
    public boolean accountDetection(String account) {
        //一开始设定没有账号重复
        boolean flag = true;
        List<User> users = userMapper.FindAllUser();
        //LinkedList<User> users = userMapper.queryAllUser();
        for (User user : users) {
            if(account.equals(user.getAccount())){
                //如果有该账号存在修改flag的值
                flag = false;
                break;
            }
        }
        return flag;
    }
    @Override
    public User selectByAccount(String account) {
        User user = userMapper.FindUser(account);
        //User user = userMapper.selectByAccount(account);
        return user;
    }

    @Override
    public int addGoods(UserGoods userGoods) {
        int i = userMapper.insertGoods(userGoods);
        return i;
    }
    @Override
    public PageVo<UserGoods> getUserGoods(String account, Integer current, Integer size) {
        return userMapper.FindUserGoods(account,current,size);
    }

    @Override
    public int addHistory(QueryHistory queryHistory) {
        int i = userMapper.insertHistory(queryHistory);
        return i;
    }

    @Override
    public PageVo<QueryHistory> getQueryHistory(String account,Integer current,Integer size) {
        return userMapper.FindHistory(account,current,size);
    }

    @Override
    public int addDangerUser(DangerUser dangerUser) {
        int i = userMapper.insertDangerUser(dangerUser);
        return i;
    }
}
