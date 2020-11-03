package com.redis.bloom;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-02 15:18
 */
@Service
public class BloomFilter implements InitializingBean {

    @Autowired
    RedissonClient redisson;

    public static RBloomFilter<String> bloomFilter = null;


    public void add(String id) {
        bloomFilter.add(id);
    }

    public boolean mightContain(String id) {
        return bloomFilter.contains(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bloomFilter = redisson.getBloomFilter("orderList");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%
        bloomFilter.tryInit(100000000L, 0.03);
    }
}
