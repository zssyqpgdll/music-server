package com.zhang.recommendation_system.pojo.admin;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Route 路由
 * @author redcomet
 * @since 2021-04-25
 */

@TableName("tb_route")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Route extends Model<Route> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String title;  /*中文名*/

    private String name;   /*组件名*/

    private String roles;

    private String icon;

    private String component;

    private String path;

    private Boolean affix;

    private Boolean hidden;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
