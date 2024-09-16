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
 * @Description: TODO(歌手实体类 )
 * @date 2021/4/25 20:18
 */
@TableName("singer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Singer extends Model<Singer> {
    @TableId(value="id",  type = IdType.AUTO)
    private Integer id;
    // 歌手id
    private String suid;
    // 歌手名称
    @TableField(value = "singer_name")
    private String sname;
    // 歌手url
    @TableField(value = "singer_url")
    private String surl;
}
