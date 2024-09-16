package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.TopSongs;
import com.zhang.recommendation_system.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TopSongsMapper extends BaseMapper<TopSongs> {

    @Select("SELECT timestamp FROM record ORDER BY timestamp desc limit 1")
    String get_record_day();

    /**
     * 根据音乐iid查找相似音乐
     *
     * @param iid 音乐id
     * @return 相似音乐id合集，中间使用逗号隔开
     */
    TopSongs queryByIid(@Param("iid") String iid);
}
