package com.zhang.recommendation_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.recommendation_system.dao.SmsCodeDao;
import com.zhang.recommendation_system.pojo.SmsCode;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.service.IUserService;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.Sms.RandomUtil;
import com.zhang.recommendation_system.util.Sms.SendSms;
import com.zhang.recommendation_system.util.result.Result;
import com.zhang.recommendation_system.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

/**
 * @author redcomet
 * @since 2021-09-20
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsCodeDao dao;

    @Autowired
    private IUserService userService;

    @PostMapping("/sendMsg")
    public Result sendMsg(@RequestBody SmsCode smsCode) throws IOException {
        String code = RandomUtil.generateDigitalString(6);
        String phone = smsCode.getPhone();
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + 1 * 1 * 300 * 1000);//过期时间 5 分钟
        smsCode.setCode(code);
        smsCode.setExpireTime(expireTime);

        int b = dao.insert(smsCode);

        if (b>0) {
            SendSms.getMessageStatus(phone, code);
            return ResultUtil.success(code);
        }
        return ResultUtil.fail(500,"发送失败!");
    }

    @PostMapping("/modifyPass/{uid}")
    public ServerResponse modifyPass(@PathVariable("uid") Integer uid, @RequestBody SmsCode smsCode) throws IOException {
        try {
            QueryWrapper<SmsCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("expire_time");
            queryWrapper.eq("phone", smsCode.getPhone());
            SmsCode code = dao.selectList(queryWrapper).get(0);
            if(!code.getCode().equals(smsCode.getCode()))
                return ServerResponse.ofError("验证码不符!");

        }catch (Exception e){
            return ServerResponse.ofError("验证码校验失败!");
        }

        User user = userService.getById(uid);
        user.setPassword(smsCode.getNote());

        Boolean b = userService.updateById(user);

        if (b) {
            return ServerResponse.ofSuccess("修改密码成功");
        }
        return ServerResponse.ofError("修改密码失败!");
    }
}

