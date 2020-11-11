package com.redis.api.zset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-10 16:20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZsetTest {

    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void test() {
        redisTemplate.opsForZSet().add("student", "1", 88);
        redisTemplate.opsForZSet().add("student", "2", 100);
        redisTemplate.opsForZSet().add("student", "3", 59);
        redisTemplate.opsForZSet().add("student", "4", 99.5);
        redisTemplate.opsForZSet().add("student", "5", 68);


        // 排名
        Long rank = redisTemplate.opsForZSet().rank("student", "2");
        System.out.println("100 : rank = " + rank);

        System.out.println("====================");

        // 根据分数区间取值   不带分数
        Set student = redisTemplate.opsForZSet().rangeByScore("student", 50, 60);
        // 根据分数区间取值   带分数
        Set student1 = redisTemplate.opsForZSet().rangeByScoreWithScores("student", 50, 60);
        System.out.println(student);
        System.out.println(student1);

        System.out.println("====================");

        // 根据排名取值（由小到大）  不带分数
        Set student2 = redisTemplate.opsForZSet().range("student", 0, 3);
        // 根据排名取值（由小到大）  带分数
        Set student3 = redisTemplate.opsForZSet().rangeWithScores("student", 0, 3);
        System.out.println(student2);
        System.out.println(student3);

        System.out.println("====================");

        // 60-80分 排名第一的值
        redisTemplate.opsForZSet().rangeByScore("student",60,80,0,1);


        Set student4 = redisTemplate.opsForZSet().rangeByLex("student", new RedisZSetCommands.Range());


        // 总数
        Long card = redisTemplate.opsForZSet().zCard("student");

        // 分数区间内数量
        Long count = redisTemplate.opsForZSet().count("student", 60, 80);

        // 根据排名取值（由大到小）  不带分数
        redisTemplate.opsForZSet().reverseRange("student",0,3);

        // 获取分数
        Double score = redisTemplate.opsForZSet().score("student", "1");
        System.out.println(score);
    }

}
