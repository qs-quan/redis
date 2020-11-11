package com.redis.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-11 13:39
 */
@Component
public class Provider {

    @Autowired
    RedisTemplate redisTemplate;

    public static final String QUEUE_NAME = "order_queue";

    public void provider() {
        redisTemplate.opsForList().rightPush(QUEUE_NAME, "order1");
        redisTemplate.opsForList().rightPush(QUEUE_NAME, "order2");
        redisTemplate.opsForList().rightPush(QUEUE_NAME, "order3");
        redisTemplate.opsForList().rightPush(QUEUE_NAME, "order4");
    }
}
