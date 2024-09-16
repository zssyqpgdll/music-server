package com.zhang.recommendation_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.recommendation_system.pojo.admin.Route;
import com.zhang.recommendation_system.pojo.admin.RouteVo;

import java.util.List;

public interface RouteService extends IService<Route> {
    List<RouteVo> getRoutes();

    List<RouteVo> getRoutes(String roleName);
}
