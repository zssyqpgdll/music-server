package com.zhang.recommendation_system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tb_sms_code")
@AllArgsConstructor
@NoArgsConstructor
public class SmsCode {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    private String code;

    private Date expireTime;

    private String note;
}