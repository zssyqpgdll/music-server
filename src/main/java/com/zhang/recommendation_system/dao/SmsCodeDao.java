package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.SmsCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author redcomet
 * @since 2021-09-20
 */
@Mapper
@Repository
public interface SmsCodeDao extends BaseMapper<SmsCode> {

}
