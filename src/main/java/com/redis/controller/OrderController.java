package com.redis.controller;

import com.redis.bloom.BloomFilter;
import com.redis.entity.Order;
import com.redis.service.OrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 9:09
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    OrderService orderService;


    /**
     * 代码存在问题：
     * 缓存穿透
     * 缓存击穿
     * 缓存崩塌
     *
     * @param id
     * @return
     */
    @RequestMapping("/getOrderById")
    @ResponseBody
    public Order getOrderById(String id) {

        try {
            Order order = (Order) redisTemplate.opsForValue().get(id);
            if (order != null) {
                System.err.println("get order from redis");
                return order;
            }

            System.err.println("get order from db");
            order = orderService.getOrderById(id);
            redisTemplate.opsForValue().set(id, order);
            return order;
        } catch (Exception e) {
            // 数据库兜底
            System.err.println("get order from db");
            Order dbOrder = orderService.getOrderById(id);
            return dbOrder;
        }

    }


    /**
     * 缓存穿透解决：缓存空对象
     * 代码简单，占用redis内存
     *
     * @param id
     * @return
     */
    @RequestMapping("/getOrderById1")
    @ResponseBody
    public Order getOrderById1(String id) {

        try {
            Object order = redisTemplate.opsForValue().get(id);
            if (order != null) {
                if (order instanceof NullValue) {
                    System.err.println("get order from redis : 查询无结果");
                    return null;
                } else {
                    System.err.println("get order from redis");
                    return (Order) order;
                }

            }

            Order dbOrder = orderService.getOrderById(id);
            if (dbOrder != null) {
                System.err.println("get order from db");
                redisTemplate.opsForValue().set(id, dbOrder);
            } else {
                System.err.println("get order from db : 查询无结果");
                redisTemplate.opsForValue().set(id, new NullValue());
            }
            return dbOrder;
        } catch (Exception e) {
            // 数据库兜底
            System.err.println("get order from db");
            Order dbOrder = orderService.getOrderById(id);
            return dbOrder;
        }

    }


    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    RedissonClient redisson;

    @PostConstruct
    private void init() {
        List<Order> all = orderService.getAll();
        all.forEach(order -> bloomFilter.add(order.getId()));
    }

    /**
     * 缓存穿透解决：布隆过滤器
     * 代码维护困难 新增数据时 bloomFilter要新增，删除大量数据时 要新建bloomFilter
     *
     * @param id
     * @return
     */
    @RequestMapping("/getOrderById2")
    @ResponseBody
    public Order getOrderById2(String id) {
        if (!bloomFilter.mightContain(id)) {
            return new Order(id, "不要攻击我，数据库没这条数据");
        }

        RLock fairLock = redisson.getFairLock(id);
        try {
            Object order = redisTemplate.opsForValue().get(id);
            if (order != null) {
                if (order instanceof Order) {
                    System.err.println("get order from redis");
                    return (Order) order;
                } else {
                    return new Order(id, "get order from redis : 查询无结果");
                }
            }

            fairLock.lock(1, TimeUnit.SECONDS);

            order = redisTemplate.opsForValue().get(id);
            if (order != null) {
                if (order instanceof Order) {
                    System.err.println("get order from redis");
                    return (Order) order;
                } else {
                    return new Order(id, "get order from redis : 查询无结果");
                }
            } else {
                System.err.println("get order from db");
                Order dbOrder = orderService.getOrderById(id);
                if (dbOrder != null) {
                    redisTemplate.opsForValue().set(id, dbOrder);
                } else {
                    redisTemplate.opsForValue().set(id, new NullValue());
                }
                return dbOrder;
            }


        } catch (Exception e) {
            // 数据库兜底
            System.err.println("get order from db");
            Order dbOrder = orderService.getOrderById(id);
            return dbOrder;
        } finally {
            fairLock.unlock();
        }

    }


    /**
     * 缓存穿透解决：布隆过滤器
     * 代码维护困难 新增数据时 bloomFilter要新增，删除大量数据时 要新建bloomFilter
     *
     * @param id
     * @return
     */
    @RequestMapping("/getOrderByIdByTemplate")
    @ResponseBody
    public Order getOrderByIdByTemplate(String id) {
        return orderService.getOrderByIdByTemplate(id);
    }

}
