package com.zhang.recommendation_system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.util.StringUtils;
import com.zhang.recommendation_system.config.SysLog;
import com.zhang.recommendation_system.dao.SmsCodeDao;
import com.zhang.recommendation_system.pojo.SmsCode;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.service.IUserService;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.SysConstant;
import com.zhang.recommendation_system.util.result.Result;
import com.zhang.recommendation_system.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangChaojie
 * @Description: TODO(普通用户的Controller层代码)
 * @date 2021/4/26 20:02
 */
@RestController
public class UserController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private SmsCodeDao smsCodeDao;

    /**
     * 用户登录
     *
     * @param user 前端数据封装成用户，查询数据验证
     * @return 成功或者失败的提示信息，json格式
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @SysLog(value= SysConstant.LOGIN)
    // 使用@RequestBody，前端传过来的数据必须是json格式
    public Result userLogin(@RequestBody User user) {
        System.out.println(user.toString());
        return iUserService.userLogin(user);
    }

    /**
     * 用户注册
     *
     * @param user 前端数据封装成用户，添加到数据库中
     * @return 成功或者失败的提示信息，json格式
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @SysLog(value= SysConstant.REGISTER)
    public Result addUser(@RequestBody User user) {
        System.out.println(user);
        // 验证验证码的逻辑
        String code = user.getCode();
        String phone = user.getPhone();
        if(!StringUtils.isNullOrEmpty(code) && !StringUtils.isNullOrEmpty(phone)){
            QueryWrapper<SmsCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("expire_time");
            queryWrapper.eq("phone", phone);
            SmsCode codeInDB = smsCodeDao.selectList(queryWrapper).get(0);
            if(!codeInDB.getCode().equals(code))
                return  ResultUtil.fail(500,"验证码不符!");
        }
        return iUserService.addUser(user);
    }

    /**
     * 相似音乐好友
     *
     * @param uid 前端传过来的用户id
     * @return 相似音乐好友数据，json格式
     */
    @RequestMapping(value = "/recommendUsers", method = RequestMethod.POST)
    public Result getUserRecommendUsers(@RequestBody String uid) {
        System.out.println("好友推荐，当前用户id：" + uid);
        String uid_str = JSON.parseObject(uid).get("uid").toString();
        return iUserService.getRecommendUsers(uid_str);
    }

    /**
     * 音乐推荐
     *
     * @param uid 前端传过来的用户id
     * @return 相似音乐好友数据，json格式
     */
    @RequestMapping(value = "/recommendSongs", method = RequestMethod.POST)
    public Result getUserRecommendSongs(@RequestBody String uid) {
        System.out.println("音乐推荐，当前用户id：" + uid);
        String uid_str = JSON.parseObject(uid).get("uid").toString();
        return iUserService.getRecommendSongs(uid_str);
    }

    /**
     * 更新用户信息
     * @param user 要更新的用户信息
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result updateUser(@RequestBody User user) {
        System.out.println(user);
        return iUserService.updateUser(user);
    }

    /**
     * 更新密码
     * @param userInfo 用户部分信息，包括用户id，旧密码，新密码
     * @return
     */
    @RequestMapping(value = "/updatePw",method = RequestMethod.POST)
    public Result updatePs(@RequestBody String userInfo) {
        System.out.println(userInfo);
        // 解析json数据
        JSONObject user_json = JSON.parseObject(userInfo);
        String uid = user_json.getString("uid");
        String oldPw = user_json.getString("oldPw");
        String newPw = user_json.getString("newPw");
        // 更新密码
        return iUserService.updatePw(uid, oldPw, newPw);
    }

    // 实名认证
    @RequestMapping(value = "/user/idconfirm",method = RequestMethod.POST)
    @SysLog(value= SysConstant.IDCONFIRM)
    public ServerResponse idconfirm(@RequestBody User user) {
        String uid = user.getUid();
        String idno = user.getIdno();
        QueryWrapper<User> query =  new QueryWrapper();
        query.eq("uid", uid);
        User u = iUserService.getOne(query);
        u.setIdno(idno);
        iUserService.updateById(u);
        // 更新密码
        return ServerResponse.ofSuccess("认证成功");
    }

    // 扣除余额
    @RequestMapping(value = "/user/downloadMusic",method = RequestMethod.POST)
    @SysLog(value= SysConstant.DOWNLOAD)
    public ServerResponse downloadMusic(@RequestBody User user) {
        String uid = user.getUid();
        QueryWrapper<User> query =  new QueryWrapper();
        query.eq("uid", uid);
        User u = iUserService.getOne(query);
        Double bal = u.getBal();
        if(bal>1.0){
            u.setBal(bal-1.0);
            iUserService.updateById(u);
            return ServerResponse.ofSuccess("可以下载");
        }else{
            return ServerResponse.ofError("余额仅剩"+bal+"元，无法下载，请先充值");
        }
    }

    @RequestMapping(value = "/user/refresh",method = RequestMethod.POST)
    public ServerResponse refreshUserInfo(@RequestBody User user) {
        return ServerResponse.ofSuccess(iUserService.getById(user.getId()));
    }

    @RequestMapping(value = "/user/play",method = RequestMethod.GET)
    @SysLog(value= SysConstant.PLAY)
    public ServerResponse play() {
        return ServerResponse.ofSuccess("ok..");
    }
}
