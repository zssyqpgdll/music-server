package com.zhang.recommendation_system.dao;

import com.zhang.recommendation_system.pojo.TopUsers;
import com.zhang.recommendation_system.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TopUsersMapper {
    /**
     * 根据用户id查找用户的相似好友
     * @param uid 用户id
     * @return 相似好友id结合，中间使用逗号隔开
     */
    TopUsers queryByUid(@Param("uid") String uid);

    @Select("select * from user where area = (SELECT area FROM `user` where uid = #{uid}) and uid != #{uid} limit 10")
    List<User> getByArea(@Param("uid") String uid);
}
