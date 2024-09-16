package com.zhang.recommendation_system.controller.admin;

import com.zhang.recommendation_system.pojo.admin.RouteVo;
import com.zhang.recommendation_system.service.RouteService;
import com.zhang.recommendation_system.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Route控制器
 * @author redcomet
 * @since 2021-04-25
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/getRoutes/{role}")
    public ServerResponse getRoutesByRole(@PathVariable("role") String roleName) {
        List<RouteVo> routes = routeService.getRoutes(roleName);
        return ServerResponse.ofSuccess(routes);
    }

    @GetMapping("/getRoutes")
    public ServerResponse getRoutes() {
        List<RouteVo> routes = routeService.getRoutes();
        return ServerResponse.ofSuccess(routes);
    }
}

