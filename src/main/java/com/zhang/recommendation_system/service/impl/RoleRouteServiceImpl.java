package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.RoleRouteDao;
import com.zhang.recommendation_system.dao.RouteDao;
import com.zhang.recommendation_system.pojo.admin.RoleRoute;
import com.zhang.recommendation_system.pojo.admin.Route;
import com.zhang.recommendation_system.pojo.admin.RouteVo;
import com.zhang.recommendation_system.service.RoleRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleRouteServiceImpl extends ServiceImpl<RoleRouteDao, RoleRoute> implements RoleRouteService {

    @Autowired
    private RouteDao routeDao;

    @Override
    public boolean updateRouteTable(List<RouteVo> routeVos, String key) {
        QueryWrapper<RoleRoute> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", key);
        baseMapper.delete(wrapper);
        routeVos.forEach(routeVo -> {
            RoleRoute rr = new RoleRoute();
            rr.setRoleName(key);
            rr.setRouteId(routeVo.getId());
            baseMapper.insert(rr);
            //这个仅针对一级菜单的情况使用
            QueryWrapper<Route> childQuery = new QueryWrapper<>();
            childQuery.eq("pid", routeVo.getId());
            List<Route> children = routeDao.selectList(childQuery);
            children.forEach(child->{
                RoleRoute rr2 = new RoleRoute();
                rr2.setRoleName(key);
                rr2.setRouteId(child.getId());
                baseMapper.insert(rr2);
            });
        });
        return true;
    }
}
