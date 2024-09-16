package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.OrderDao;
import com.zhang.recommendation_system.pojo.Order;
import com.zhang.recommendation_system.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {



    @Override
    public void pay(String userId, String orderId) {
        Order order = baseMapper.selectById(orderId);
        order.setStatus(Order.STAT_PAY);//设置订单状态为成功
        baseMapper.updateById(order);//更新订单
    }

    @Override
    public void cancel(String userId, String orderId) {
        Order order = baseMapper.selectById(orderId);
        order.setStatus(Order.STAT_CANCEL);//设置订单状态为取消
        baseMapper.updateById(order);
    }
}
