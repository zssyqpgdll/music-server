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
 * @Description: TODO(用户实体类)
 * @date 2021/4/25 20:17
 */
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends Model<User> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 用户id
    private String uid;
    // 用户名
    private String name;
    // 用户密码
    private String password;
    // 用户性别
    private String gender;
    // 用户年龄
    private int age;
    // 用户所在地区
    private String area;
    // 用户注册时间
    @TableField(value="registerTime")
    private String registerTime;
    // 用户的自我简介
    private String des;

    // 身份证号码 识别用
    private String idno;

    // 手机号 短信验证码用
    private String phone;

    // 钱包余额
    private Double bal;

    // 短信验证码
    @TableField(exist = false)
    private String code;
}
