package com.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-02 13:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void stringApi(){
        redisTemplate.opsForValue().set("k1","v1");
        Object v1 = redisTemplate.opsForValue().get("k1");
        System.out.println(v1);
    }
}
