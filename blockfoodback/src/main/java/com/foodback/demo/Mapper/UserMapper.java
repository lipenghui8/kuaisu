package com.foodback.demo.Mapper;

import com.foodback.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserMapper {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Integer insertUser(User user){
        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
        return 1;
    }

    public List<User> FindAllUser(){
        List<User> userList = mongoTemplate.findAll(User.class);
        return userList;
    }

    public User FindUser(String conditions){
        Query query = new Query(Criteria.where("account").is(conditions));
        return mongoTemplate.findOne(query,User.class);
    }

    public Integer insertGoods(UserGoods userGoods){
        mongoTemplate.insert(userGoods);
        return 1;
    }
    public Integer insertHistory(QueryHistory queryHistory){
        mongoTemplate.insert(queryHistory);
        return 1;
    }

    public PageVo<QueryHistory> FindHistory(String account, Integer current, Integer size){
        Query query = new Query(Criteria.where("account").is(account));
        PageVo<QueryHistory> queryVo = new PageVo<QueryHistory> ();
        queryVo.setTotal(mongoTemplate.count(query,QueryHistory.class));
        queryVo.setDataList(mongoTemplate.find(query.skip((current-1)*size).limit(size),QueryHistory.class));
        queryVo.setCurrent(current);
        queryVo.setSize(size);

        return queryVo;
    }

    public PageVo<UserGoods> FindUserGoods(String account,Integer current,Integer size){
        Query query = new Query(Criteria.where("account").is(account));
        PageVo<UserGoods> userGoodsVo = new PageVo<UserGoods>();
        userGoodsVo.setTotal(mongoTemplate.count(query,UserGoods.class));
        userGoodsVo.setDataList(mongoTemplate.find(query.skip((current-1)*size).limit(size),UserGoods.class));
        userGoodsVo.setCurrent(current);
        userGoodsVo.setSize(size);
        return userGoodsVo;
    }

    public Integer insertDangerUser(DangerUser dangerUser){
        mongoTemplate.insert(dangerUser);
        return 1;
    }

    public List<DangerUser> FindAllDangerUsers(){
        List<DangerUser> userList = mongoTemplate.findAll(DangerUser.class);
        return userList;
    }

}
