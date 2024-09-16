package com.zhang.recommendation_system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.recommendation_system.pojo.Song;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import com.zhang.recommendation_system.pojo.admin.ThreeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SongMapper extends BaseMapper<Song> {

    @Select("SELECT singer.singer_name as name from (SELECT suid, sum(table1.playcnt) as value FROM (SELECT * from song WHERE suid != '00000000')as table1 group by suid ORDER BY value DESC limit 5) as table2, singer WHERE table2.suid = singer.suid ORDER BY value DESC")
    List<String> getTopSingers();

    @Select("select  song_name as name1,album as name2,playcnt as name3  from  song order by playcnt desc  limit 10 ")
    List<ThreeData> getPlayCnt();

    @Select("select singer_name as name, value from (select suid, count(*) as value from song where suid != '00000000' group by suid order by value desc LIMIT 20) as table1, singer where table1.suid = singer.suid order by value desc")
    List<ChartData> getSingerSongs();

    /**
     * 添加新歌曲
     *
     * @param song 歌曲iid，String类型
     * @return Song实例
     */
    void addSong(Song song);

    /**
     * 根据歌曲id删除歌曲，将删除标志置为0
     *
     * @param iid 歌曲id
     */
    void deleteSong(@Param("iid") String iid);

    /**
     * 更新歌曲
     *
     * @param song 新的歌曲信息
     */
    void updateSong(Song song);

    /**
     * 根据歌曲id查找歌曲信息
     *
     * @param iid 歌曲iid，String类型
     * @return Song实例
     */
    Song queryByIid(@Param("iid") String iid);

    /**
     * 根据歌曲名查询歌曲，模糊查询
     *
     * @param keyWord 搜索关键字
     * @return 查询结果
     */
    List<Song> queryByKeyword(@Param("keyword") String keyWord, @Param("currIndex") int currIndex, @Param("pageSize") int pageSize);

    /**
     * 获得指定条数的音乐信息，根据热度（播放次数）高低排序，范围是：(currentPage-1)*pageSize后的pageSize个数据
     *
     * @param currentIndex 当前页面
     * @param pageSize     页面大小
     * @return pageSize个结果
     */
    List<Song> querySongsHot(@Param("currIndex") int currentIndex, @Param("pageSize") int pageSize);

    /**
     * 获得指定条数的音乐信息，根据发行时间高低排序，范围是：(currentPage-1)*pageSize后的pageSize个数据
     *
     * @param currentIndex 当前页面
     * @param pageSize     页面大小
     * @return pageSize个结果
     */
    List<Song> querySongsNew(@Param("currIndex") int currentIndex, @Param("pageSize") int pageSize);

    /**
     * 查询所有歌曲信息
     *
     * @return 歌曲实例数组，List<Song>类型
     */
    List<Song> queryAll();

}
