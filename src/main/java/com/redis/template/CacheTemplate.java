package com.redis.template;

import com.redis.bloom.BloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 14:45
 */
@Service
public class CacheTemplate {

    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    RedissonClient redisson;

    @Autowired
    RedisTemplate redisTemplate;


    public <T> T redisFindCache(String id, Supplier<T> supplier) {
        if (!bloomFilter.mightContain(id)) {
            return null;
        }

        RLock fairLock = redisson.getFairLock(id);
        try {
            T ret = (T) redisTemplate.opsForValue().get(id);
            if (ret != null) {
                if (ret instanceof NullValue) {
                    return null;
                } else {
                    System.err.println("get order from redis");
                    return ret;
                }
            }

            fairLock.lock(1, TimeUnit.SECONDS);

            ret = (T) redisTemplate.opsForValue().get(id);
            if (ret != null) {
                if (ret instanceof NullValue) {
                    return null;
                } else {
                    System.err.println("get order from redis");
                    return ret;
                }
            } else {
                System.err.println("get order from db");
                T dbret = supplier.get();
                if (dbret != null) {
                    redisTemplate.opsForValue().set(id, dbret);
                } else {
                    redisTemplate.opsForValue().set(id, new NullValue());
                }
                return dbret;
            }


        } catch (Exception e) {
            // 数据库兜底
            System.err.println("get order from db");
            T dbret = supplier.get();
            return dbret;
        } finally {
            fairLock.unlock();
        }

    }
}
