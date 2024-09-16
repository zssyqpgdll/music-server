package com.zhang.recommendation_system.pojo.admin;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Role
 * @author redcomet
 * @since 2021-04-17
 */

@TableName("tb_role")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role extends Model<Role> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("`key`")
    private String key;

    private String name;  /*权限中文名*/

    private String description;

    private String menus;

    private String funcs;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
