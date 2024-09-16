/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2021 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.zhang.recommendation_system.controller.app;

import com.zhang.recommendation_system.config.SysLog;
import com.zhang.recommendation_system.config.app.ServiceResultEnum;
import com.zhang.recommendation_system.config.app.TokenToUser;
import com.zhang.recommendation_system.controller.app.service.ApiLoginService;
import com.zhang.recommendation_system.controller.app.util.Result;
import com.zhang.recommendation_system.controller.app.util.ResultGenerator;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.UserVo;
import com.zhang.recommendation_system.pojo.admin.UserLoginRequest;
import com.zhang.recommendation_system.util.SysConstant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserAPI {

    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    @Autowired
    private ApiLoginService apiLoginService;

    @PostMapping("/user/login")
    @SysLog(value= SysConstant.APP_LOGIN)
    public Result<String> login(@RequestBody  UserLoginRequest userLoginRequest) {
        String loginResult = apiLoginService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        logger.info("login api,loginName={},loginResult={}", userLoginRequest.getUsername(), loginResult);

        //登录成功
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == SysConstant.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/user/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToUser User loginMallUser) {
        Boolean logoutResult = apiLoginService.logout(Long.valueOf(loginMallUser.getUid()));

        logger.info("logout api,loginMallUser={}", loginMallUser.getUid());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }

    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody  User mallUserRegisterParam) {
        String registerResult = apiLoginService.register(mallUserRegisterParam.getUid(), mallUserRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getUid(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") User mallUserUpdateParam, @TokenToUser User loginMallUser) {
        Boolean flag = apiLoginService.updateUserInfo(mallUserUpdateParam, loginMallUser.getId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @GetMapping("/user/info")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<UserVo> getUserDetail(@TokenToUser User user) {
        UserVo mallUserVO = new UserVo(); //可以根据登录信息去关联出更多的user信息，所以用vo对象
        BeanUtils.copyProperties(user, mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }

    @GetMapping("/user/play")
    @SysLog(value= SysConstant.APP_PLAY)
    public Result play(@TokenToUser User user) {
       return ResultGenerator.genSuccessResult("OK");
    }
//    @GetMapping("/user/info/{uid}")
//    public ServerResponse info2(@PathVariable("uid") Integer uid) {
//        User user = userService.info(uid);
//        if (user != null){
//            return ServerResponse.ofSuccess(user);
//        }
//        return ServerResponse.ofError("查询失败!");
//    }
}
