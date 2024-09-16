package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    //从用户表中聚合area字段前3个字一样的用户，area字段的前3个字作为name，聚合的行数作为value，最后按照value值从大到小排序
    @Select("SELECT left(area,3) as name,count(*) as value FROM `user` " +
            "group by left(area,3) order by  value desc")
    List<ChartData> getProvinceUsers();

    //查找实名认证人数，即身份证号不为空
    @Select("select count(*) from user where idno != ''")
    Integer getIdconfirm();

    /**
     * 根据用户id查找用户
     * @param uid 用户id
     * @return 用户实例，User类型
     */
    User queryById(@Param("uid") String uid);

    /**
     * 查找所有用户信息
     * @return 用户实例集合，List<User>类型
     */
    List<User> queryAll();

    /**
     * 添加用户
     * @param user 需要添加的用户
     */
    void addUser(User user);

    /**
     * 更新用户信息
     * @param user 新的用户信息
     */
    void updateUser(User user);

    /**
     * 更新密码
     * @param uid 用户id
     * @param newPs 新密码
     */
    void updatePs(@Param("uid") String uid, @Param("newPs") String newPs);
}
