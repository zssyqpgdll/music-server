package com.zhang.recommendation_system.pojo.admin;

import lombok.Data;

import java.util.List;

@Data
public class RoleVo extends Role {
    private List<RouteVo> routes;
}
