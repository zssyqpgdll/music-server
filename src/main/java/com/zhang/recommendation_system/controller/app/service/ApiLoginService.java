package com.zhang.recommendation_system.controller.app.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.recommendation_system.config.app.GHException;
import com.zhang.recommendation_system.config.app.ServiceResultEnum;
import com.zhang.recommendation_system.controller.app.util.NumberUtil;
import com.zhang.recommendation_system.controller.app.util.SystemUtil;
import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.dao.UserTokenDao;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.UserToken;
import com.zhang.recommendation_system.util.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class ApiLoginService {

    @Autowired
    private UserMapper dao;

    @Autowired
    private UserTokenDao userTokenDao;

    public String login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", username);
        wrapper.eq("uid", username);
        wrapper.eq("password", password);
        User user = dao.selectOne(wrapper);
        if (user != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getId());
            UserToken mallUserToken = userTokenDao.selectByUserId(Integer.valueOf(user.getUid()));
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (mallUserToken == null) {
                mallUserToken = new UserToken();
                mallUserToken.setUserId(Long.valueOf(user.getUid()));
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (userTokenDao.insert(mallUserToken) > 0) {
                    //新增成功后返回
                    return token;
                }
            } else {
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //更新
                if (userTokenDao.updateById(mallUserToken) > 0) {
                    //修改成功后返回
                    return token;
                }
            }
        }

        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    private String getNewToken (String timeStr, Integer userId){
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    public Boolean updateUserInfo(User mallUser, Integer userId) {
        User user = dao.selectById(userId);
        if (user == null) {
            GHException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setName(mallUser.getName());
        if(!StringUtils.isEmpty(mallUser.getPassword()))
            user.setPassword(mallUser.getPassword());
        //若密码为空字符，则表明用户不打算修改密码，使用原密码保存
//        if (!MD5Util.MD5Encode("", "UTF-8").equals(mallUser.getPasswordMd5())){
//            user.setPasswordMd5(mallUser.getPasswordMd5());
//        }
        user.setDes(mallUser.getDes());
        if (dao.updateById(user) > 0) {
            return true;
        }
        return false;
    }

    public Boolean logout(Long userId) {
        return userTokenDao.deleteById(userId) > 0;
    }


    public String register(String loginName, String password) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", loginName);
        if (dao.selectList(query) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User registerUser = new User();
        registerUser.setUid(loginName);
        registerUser.setName(loginName);
        registerUser.setDes(SysConstant.USER_INTRO);
        if(StringUtils.isEmpty(password))
            password = "123456";
        registerUser.setPassword(password);
        if (dao.insert(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
}
