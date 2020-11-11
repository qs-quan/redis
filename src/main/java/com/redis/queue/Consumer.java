package com.redis.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-11 13:39
 */
@Component
public class Consumer {

    @Autowired
    RedisTemplate redisTemplate;


    public static final String QUEUE_NAME = "order_queue";

    public void comsumer() {
        /**
         *     消息消费者有一个问题存在，即需要不停的调用lpop方法查看List中是否有待处理消息。
         *    每调用一次都会发起一次连接，这会造成不必要的浪费。也许你会使用Thread.sleep()等方法让消费者线程隔一段时间再消费，但这样做有两个问题：
         *
         *         1）、如果生产者速度大于消费者消费速度，消息队列长度会一直增大，时间久了会占用大量内存空间。
         *
         *         2）、如果睡眠时间过长，这样不能处理一些时效性的消息，睡眠时间过短，也会在连接上造成比较大的开销。
         *
         *
         */
        while (true) {
            String message = (String) redisTemplate.opsForList().leftPop(QUEUE_NAME);
            System.out.println("comsumer + 读取完成==========");
            if(message != null){
                System.err.println(message);
            }
        }
    }


    public void comsumer1() {
        while (true) {
            // 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
            String message = (String) redisTemplate.opsForList().leftPop(QUEUE_NAME,10,TimeUnit.SECONDS);
            System.out.println("comsumer1 + 读取完成==========");
            if(message != null){
                System.err.println(message);
            }
        }
    }

}
