package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.RoleDao;
import com.zhang.recommendation_system.dao.RouteDao;
import com.zhang.recommendation_system.pojo.admin.Meta;
import com.zhang.recommendation_system.pojo.admin.Route;
import com.zhang.recommendation_system.pojo.admin.RouteVo;
import com.zhang.recommendation_system.service.RouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl extends ServiceImpl<RouteDao, Route> implements RouteService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<RouteVo> getRoutes() {
        List<RouteVo> routes = new ArrayList<>();   //返回数据为RouteVo对象列表
        QueryWrapper<Route> query = new QueryWrapper<>();
        query.eq("pid",-1);  //从父菜单开始搜索
        query.eq("hidden",false);
        List<Route> route = baseMapper.selectList(query);
        route.forEach(r->{
            RouteVo vo = new RouteVo();
            BeanUtils.copyProperties(r, vo);
            Meta meta = new Meta();
            meta.setAffix(r.getAffix());
            meta.setHidden(r.getHidden());
            meta.setTitle(r.getTitle());
            meta.setRoles(r.getRoles());
            meta.setIcon(r.getIcon());
            vo.setMeta(meta);
            vo.setChildren(getChildren(r.getId()));
            routes.add(vo);
        });
        return routes;
    }

    /**
     * 根据roleName 获取路由，目前还不支持多级菜单
     * @param roleName
     * @return
     */
    public List<RouteVo> getRoutes(String roleName) {
        List<RouteVo> routes = new ArrayList<>();
        List<Route> route = null;
        if("sadmin".equals(roleName)) {
            QueryWrapper<Route> query = new QueryWrapper<>();
            query.eq("pid",-1);  //从父菜单开始搜索
            query.eq("hidden",false);
            route = baseMapper.selectList(query);
        }else{
            route = baseMapper.getRoleRouteByRoleName(roleName);  //目前还不支持多级菜单
        }
        route.forEach(r->{
            //先给自己设置Meta，以及第一个children节点
            RouteVo vo = new RouteVo();
            BeanUtils.copyProperties(r, vo);
            Meta meta = new Meta();
            meta.setAffix(r.getAffix());
            meta.setHidden(r.getHidden());
            meta.setTitle(r.getTitle());
            meta.setRoles(r.getRoles());
            meta.setIcon(r.getIcon());
            vo.setMeta(meta);
            vo.setChildren(getChildrenWithRole(r.getId(), roleName));
            routes.add(vo);
        });
        return routes;
    }

    public List<RouteVo> getChildren(Integer parent_id){
        List<RouteVo> children = new ArrayList<>();
        QueryWrapper<Route> query2 = new QueryWrapper<>();
        query2.eq("pid",parent_id);
        query2.eq("hidden",false);
        List<Route> _children = baseMapper.selectList(query2);
        _children.forEach(child->{
            RouteVo childVo = new RouteVo();
            BeanUtils.copyProperties(child, childVo);
            Meta meta1 = new Meta();
            meta1.setAffix(child.getAffix());
            meta1.setHidden(child.getHidden());
            meta1.setTitle(child.getTitle());
            meta1.setRoles(child.getRoles());
            meta1.setIcon(child.getIcon());
            childVo.setMeta(meta1);
            children.add(childVo);
        });

        return children;
    }

    public List<RouteVo> getChildrenWithRole(Integer parent_id, String key){
        List<RouteVo> children = new ArrayList<>();
        QueryWrapper<Route> query2 = new QueryWrapper<>();
        List<Route> _children = null;
        if("sadmin".equals(key)) {
            query2.eq("pid",parent_id);
            query2.eq("hidden",false);
            _children = baseMapper.selectList(query2);
        }
        else{
            _children = baseMapper.getChildRoleRouteByRoleName(key, parent_id);
        }

        _children.forEach(child->{
            RouteVo childVo = new RouteVo();
            BeanUtils.copyProperties(child, childVo);
            Meta meta1 = new Meta();
            meta1.setAffix(child.getAffix());
            meta1.setHidden(child.getHidden());
            meta1.setTitle(child.getTitle());
            meta1.setRoles(child.getRoles());
            meta1.setIcon(child.getIcon());
            childVo.setMeta(meta1);
            children.add(childVo);
        });

        return children;
    }
}
