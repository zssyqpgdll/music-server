package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.admin.RoleRoute;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleRouteDao extends BaseMapper<RoleRoute> {

}
