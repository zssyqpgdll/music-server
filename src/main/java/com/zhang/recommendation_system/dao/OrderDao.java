package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.Order;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author redcomet
 * @since
 */
@Mapper
@Repository
public interface OrderDao extends BaseMapper<Order> {

    @Select("select name, value from (select  user_id, sum(amount) as value from (select  u.name,o.*  from  tb_order o, user u  where o.user_id = u.uid ) x group by user_id order by value desc) as table1, user where table1.user_id = user.uid order by value desc limit 5")
    List<ChartData> getDepositRank();
}
