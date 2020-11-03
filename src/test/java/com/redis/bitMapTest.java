package com.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-02 15:27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class bitMapTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void bitMapTest() {
        redisTemplate.opsForValue().setBit("big", 8,false);
//         b"的二进制表示为0110 0010，
//         我们将第7位（从0开始）设置为1，那0110 0011 表示的就是字符“c”，
//         所以最后的字符 “big”变成了“cig”。
        redisTemplate.opsForValue().getBit("big", 7);
    }

    @Autowired
    RedissonClient redisson;

    @Test
    public void bloomFilter() {

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%
        bloomFilter.tryInit(100000000L, 0.03);
        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");

        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456"));//false
        System.out.println(bloomFilter.contains("10086"));//true
    }
}
