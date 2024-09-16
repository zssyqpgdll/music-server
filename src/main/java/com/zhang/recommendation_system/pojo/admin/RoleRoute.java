package com.zhang.recommendation_system.pojo.admin;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RoleRoute 路由菜单
 * @author redcomet
 * @since 2021-04-27
 */

@TableName("tb_role_route")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRoute extends Model<RoleRoute> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleName;

    private Integer routeId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
