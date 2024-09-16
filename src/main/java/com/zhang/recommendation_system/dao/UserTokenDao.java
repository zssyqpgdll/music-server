package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.UserToken;
import org.apache.ibatis.annotations.Select;

public interface UserTokenDao extends BaseMapper<UserToken> {

    @Select("select  *  from  tb_user_token where token = #{token} ")
    UserToken selectByToken(String token);

    @Select("select  *  from  tb_user_token where user_id = #{userId} ")
    UserToken selectByUserId(Integer userId);
}
