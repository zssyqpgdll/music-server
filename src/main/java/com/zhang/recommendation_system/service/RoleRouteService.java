package com.zhang.recommendation_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.recommendation_system.pojo.admin.RoleRoute;
import com.zhang.recommendation_system.pojo.admin.RouteVo;

import java.util.List;

public interface RoleRouteService extends IService<RoleRoute> {

    boolean updateRouteTable(List<RouteVo> routeVos, String key);
}
