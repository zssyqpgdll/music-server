package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.RoleDao;
import com.zhang.recommendation_system.pojo.admin.Role;
import com.zhang.recommendation_system.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
}
