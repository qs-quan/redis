package com.redis.service;

import com.redis.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-03 9:18
 */
@Service
public class OrderService {


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
}
