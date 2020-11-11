package com.redis;

import com.redis.queue.Consumer;
import com.redis.queue.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-11 13:45
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class QueueTest {

    @Autowired
    private Provider provider;

    @Autowired
    private Consumer consumer;

    @Test
    public void provider() {
        provider.provider();
    }

    @Test
    public void consumer() {
//        consumer.comsumer();
        consumer.comsumer1();
    }


}
