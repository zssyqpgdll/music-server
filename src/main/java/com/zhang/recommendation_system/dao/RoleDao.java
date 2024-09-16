package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.admin.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author redcomet
 * @since 2021-04-17
 */
@Mapper
@Repository
public interface RoleDao extends BaseMapper<Role> {

}
