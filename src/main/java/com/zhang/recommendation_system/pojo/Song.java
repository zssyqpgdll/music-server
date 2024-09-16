package com.zhang.recommendation_system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhangChaojie
 * @Description: TODO(歌曲实体类)
 * @date 2021/4/25 20:18
 */
@TableName("song")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Song extends Model<Song> {

    @TableId(value="id" , type = IdType.AUTO)
    private Integer id;
    // 歌曲id
    private String iid;
    private String suid;
    // 歌曲名称
    private String songName;
    // 歌手名称
    @TableField(exist = false)
    private String singerName;
    // 所在专辑
    private String album;
    // 播放次数
    private String playcnt;
    // 歌曲url
    private String songUrl;
    // 播放url
    private String downUrl;
    // 时长
    private String songTime;
    // 图片url
    @TableField(value = "picUrl")
    private String picUrl;
    // 发行时间
    @TableField(value = "publishTime")
    private String publishTime;
}
