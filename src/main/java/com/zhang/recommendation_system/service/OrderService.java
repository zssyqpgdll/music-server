package com.zhang.recommendation_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.recommendation_system.pojo.Order;

public interface OrderService extends IService<Order> {


    void pay(String userId, String orderId);

    void cancel(String userId, String orderId);
}
