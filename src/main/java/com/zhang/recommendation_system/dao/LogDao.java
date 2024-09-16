package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import com.zhang.recommendation_system.pojo.admin.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author redcomet
 * @since 2021-04-17
 */
@Mapper
public interface LogDao extends BaseMapper<Log> {

//    @Select("select d from (select  distinct DATE_FORMAT(create_time,'%Y-%m-%d') as d   " +
//            "from  tb_log where opt = #{opt} order by create_time desc limit 15) as tt order by d")
    //找到最新的15个日期
    @Select("select * from (select distinct DATE_FORMAT(create_time,'%Y-%m-%d') as d from  tb_log " +
            "where opt = #{opt} order by d desc limit 15) as table1 order by d")
    List<String> chart(String opt);

    @Select("select  remark as name,count(*) as value from (" +
            "select  g.remark,u.realname  from  tb_group g, tb_user u where u.group_id = g.id" +
            ") a" +
            " group by remark order by value desc")
    List<ChartData> groupManCount();

    @Select("select  name,count(*) as value from (" +
            "select  r.name,u.realname  from  tb_role r, tb_user u where u.roles = r.key" +
            ") a" +
            " group by name  order by value desc")
    List<ChartData> roleManCount();

    @Select("select * from (select distinct DATE_FORMAT(create_time,'%Y-%m-%d') as d from  tb_log order by d desc limit 15) as table1 order by d")
    List<String> chart_dayCount();
}
