package com.redis;

import com.redis.queue.DelayQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-11 16:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DelayQueueTest {


    @Autowired
    private DelayQueue delayQueue;


    @Test
    public void test() {

        Thread producer = new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    delayQueue.delay("codehole" + i);
                }
            }
        };

        Thread consumer = new Thread() {
            public void run() {
                delayQueue.loop();
            }
        };

        producer.start();
        consumer.start();
        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
        }
    }

}
