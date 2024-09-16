package com.zhang.recommendation_system.controller.admin;


import com.zhang.recommendation_system.service.ISingerService;
import com.zhang.recommendation_system.service.ISongService;
import com.zhang.recommendation_system.service.IUserService;
import com.zhang.recommendation_system.service.LogService;
import com.zhang.recommendation_system.service.impl.TokenService;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redcomet
 * @since 2021-11-17
 */
@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private IUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISongService songService;
    @Autowired
    private ISingerService singerService;
    @Autowired
    private LogService logService;

    @GetMapping("/panel")//面板统计
    public ServerResponse panelData() {
        Map map = new HashMap();
        map.put("users",userService.count());//统计用户数
        map.put("logs",userService.getIdconfirm());//实名认证人数
        map.put("three",songService.count());//统计歌曲数
        map.put("four",singerService.count());//统计歌手数
        return ServerResponse.ofSuccess(map);
    }

    @GetMapping("/loginData")
    public ServerResponse loginData() throws ParseException {

        Map map = new HashMap();
        map.put("xData",logService.chartDay(SysConstant.LOGIN));//最近登陆的几个日期，最多15个日期
        map.put("expectedData",logService.chartCount(SysConstant.LOGIN));//对应的每一天的登陆数
        return ServerResponse.ofSuccess(map);
    }
}

