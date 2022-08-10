package com.foodback.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
@SpringBootTest
public class MongodbTest {
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testCreateCollection(){
        boolean test = mongoTemplate.collectionExists("emp");
        if(test){
            mongoTemplate.dropCollection("emp");
        }
        mongoTemplate.createCollection("emp");
    }
}
