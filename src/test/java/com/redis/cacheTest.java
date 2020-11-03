package com.redis;

import com.redis.controller.OrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 11:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class cacheTest {


    public static final int threadNum = 100;
    public static CountDownLatch countDownLatch = new CountDownLatch(threadNum);

    @Autowired
    OrderController orderController;

    @Test
    public void getOrder() throws Exception {

        for (int i = 0; i < threadNum; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    orderController.getOrderById2("4");
                }
            }).start();

            countDownLatch.countDown();
        }
        Thread.currentThread().join();
    }

    @Test
    public void getOrderByTemplate() throws Exception {

        for (int i = 0; i < threadNum; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    orderController.getOrderByIdByTemplate("1");
                }
            }).start();

            countDownLatch.countDown();
        }
        Thread.currentThread().join();
    }


}
