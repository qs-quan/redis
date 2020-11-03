package com.redis.service;

import com.redis.entity.Order;
import com.redis.template.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 9:18
 */
@Service
public class OrderService {

    @Autowired
    CacheTemplate cacheTemplate;


    @Autowired
    OrderService orderService;

    public Order getOrderById(String id) {
        if (id.equals("1") || id.equals("2") || id.equals("3") || id.equals("4")) {
            Order order = new Order();
            order.setId(id);
            order.setRemark("100台手机");
            return order;
        }

        return null;
    }

    public List<Order> getAll() {

        return new ArrayList<Order>() {{
            add(new Order("1", "100台手机"));
            add(new Order("2", "200台手机"));
            add(new Order("3", "300台手机"));
            add(new Order("4", "400台手机"));
        }};
    }

    public Order getOrderByIdByTemplate(String id) {

        return cacheTemplate.redisFindCache(id, new Supplier<Order>() {
            @Override
            public Order get() {
                return orderService.getOrderById(id);
            }
        });


    }

}
