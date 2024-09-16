package com.zhang.recommendation_system.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zhang.recommendation_system.pojo.Admin;
import com.zhang.recommendation_system.pojo.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: 15760
 * @Date: 2020/3/24
 * @Descripe: Token下发
 */

@Service
public class TokenService {

    /**
     * 验证学生
     * @param student
     * @return
     */
//    public String getToken(Student student) {
//        Date start = new Date();
//        // 一小时有效时间
//        long currentTime = System.currentTimeMillis() + 60* 60 * 500;
//        Date end = new Date(currentTime);
//        String token = "";
//
//        token = JWT.create().withAudience(student.getId().toString()).withIssuedAt(start).withExpiresAt(end)
//                .sign(Algorithm.HMAC256(student.getPassword()));
//        return token;
//    }

    /**
     * 验证管理员
     * @param user
     * @return
     */
    public String getToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 500;
        Date end = new Date(currentTime);
        String token = "";
        //创建用户token
        token = JWT.create().withAudience(user.getId().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public String getToken1(Admin admin) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60* 60 * 500;
        Date end = new Date(currentTime);
        String token = "";
        //创建用户token
        token = JWT.create().withAudience(admin.getName().toString()).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(admin.getPassword()));
        return token;
    }
}
