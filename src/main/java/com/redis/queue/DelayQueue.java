package com.redis.queue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-11 16:16
 */
@Component
public class DelayQueue<T> {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String queueKey = "delay_queue";

    public static final Gson gson = new Gson();

    class TaskItem<T> {
        public String id;
        public T msg;
    }

    // fastjson 序列化对象中存在 generic 类型时，需要使用 TypeReference
    private Type TaskType = new TypeReference<TaskItem<T>>() {
    }.getType();


    public void delay(T msg) {
        TaskItem task = new TaskItem();
        task.id = UUID.randomUUID().toString(); // 分配唯一的 uuid
        task.msg = msg;
        String strMessage = gson.toJson(task);        // 序列化
        redisTemplate.opsForZSet().add(queueKey, strMessage, System.currentTimeMillis() + 5000); // 塞入延时队列 ,5s 后再试
    }

    public void loop() {
        while (!Thread.interrupted()) {
            // 只取一条
            Set values = redisTemplate.opsForZSet().rangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if (values.isEmpty()) {
                try {
                    Thread.sleep(500); // 歇会继续
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            String s = (String) values.iterator().next();
            if (redisTemplate.opsForZSet().remove(queueKey, s) > 0) { // 抢到了
                TaskItem task = gson.fromJson(s, TaskType); // fastjson 反序列化
                this.handleMsg((T) task.msg);
            }
        }
    }

    public void handleMsg(T msg) {
        System.out.println(msg);
    }

}
