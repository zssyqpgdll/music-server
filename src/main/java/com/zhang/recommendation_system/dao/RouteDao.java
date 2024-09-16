package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.admin.Route;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RouteDao extends BaseMapper<Route> {

    @Select("select a.* from tb_route a, tb_role_route b where b.role_name = #{roleName} and a.id = b.route_id and a.hidden = false and a.pid = -1")
    List<Route> getRoleRouteByRoleName(String roleName);

    @Select("select a.* from tb_route a, tb_role_route b where b.role_name = #{roleName} and a.id = b.route_id and a.hidden = false" +
            " and a.pid = #{pid}")
    List<Route> getChildRoleRouteByRoleName(String roleName, Integer pid);
}
