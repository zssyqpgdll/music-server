package com.zhang.recommendation_system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单
 * 会费
 * @author redcomet
 * @since 2021-10-22
 */

@TableName("tb_order")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order extends Model<Order> {

    private static final long serialVersionUID=1L;

    public static final Integer STAT_PAY = 1;
    public static final Integer STAT_CANCEL = 2;

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @JsonProperty(value = "user_id")
    private String userId;

    private Integer groupId;

    private String reason;

    private Double amount;

    private Integer status;

    private String type;

    private String remark;

    private String phone;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
