package com.zhang.recommendation_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.recommendation_system.config.SysLog;
import com.zhang.recommendation_system.dao.LogDao;
import com.zhang.recommendation_system.dao.OrderDao;
import com.zhang.recommendation_system.dao.SongMapper;
import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.pojo.Singer;
import com.zhang.recommendation_system.pojo.Song;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.admin.ThreeData;
import com.zhang.recommendation_system.service.LogService;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.SysConstant;
import com.zhang.recommendation_system.util.result.Result;
import com.zhang.recommendation_system.util.spark.Sparkdao;
import com.zhang.recommendation_system.util.OSUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spark")
public class SparkController {

    @Autowired
    private Sparkdao dao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserMapper userDao;

    @Autowired
    private SongMapper songDao;

    @Autowired
    private LogService logService;

    private OSUtils osutils;
    /**
     * 大屏数据-统计值
     * @return
     */
    //第三幅图
    @RequestMapping(value = "/dash1", method = RequestMethod.GET)
    public ServerResponse Dash1() {
        Map map = new HashMap();
        // 通过业务表汇总
        map.put("singers", dao.count("singer"));//歌手数量
        map.put("songs", dao.count("song"));//歌曲数量
        map.put("user", dao.count("user"));//用户数量
        map.put("deposits", orderDao.selectCount(null));//订单数量
//        map.put("deposit_rank", orderDao.getDepositRank());//充值总额排行榜列表
        map.put("singer_songs", songDao.getSingerSongs());//发布歌曲数量最多的20个歌手及对应数量
//        System.out.println("歌手发布歌曲数量：" + songDao.getSingerSongs());
//        userDao.getProvinceUsers()
        // 通过日志汇总
        map.put("logins", dao.countLog("login"));//登陆次数
//        map.put("app_logins", dao.countLog("app_login"));
        map.put("favs", dao.countLog("fav"));//收藏次数
        map.put("downloads", dao.countLog("download"));//下载次数
        map.put("plays", dao.countLog("play"));//播放次数
//        map.put("app_plays", dao.countLog("app_play"));
        map.put("idconfirm", dao.countLog("idconfirm"));//实名认证次数

        //系统负载率
        map.put("CpuRatio", osutils.getCpuRatio());

        return ServerResponse.ofSuccess(map);
    }

    @RequestMapping(value = "/dash2", method = RequestMethod.GET)
    public ServerResponse Dash2() {
        Map map = new HashMap();
        // 通过日志汇总
        map.put("deposit_rank", orderDao.getDepositRank());//充值总额排行榜列表,前5
        return ServerResponse.ofSuccess(map);
    }

    //获取歌曲播放次数最多的前10首歌
    @RequestMapping(value = "/playcnt", method = RequestMethod.GET)
    public ServerResponse Playcnt() {
        Map map = new HashMap();
        // 通过日志汇总
        map.put("playcnt_rank", songDao.getPlayCnt());

        return ServerResponse.ofSuccess(map);
    }

    ////用于获取用户来源、省份——第一个图表
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public ServerResponse Provinces() {
        Map map = new HashMap();
        // 通过日志汇总
        map.put("province_users", userDao.getProvinceUsers());

        return ServerResponse.ofSuccess(map);
    }

    //播放与收藏关系
    @RequestMapping(value = "/logins", method = RequestMethod.GET)
    public ServerResponse Logins() throws ParseException {
        Map map = new HashMap();

        //有下载行为的时间，作为x轴数据
        map.put("xData",logService.chartDay(SysConstant.DOWNLOAD));
        map.put("total_action", logService.dayCount());//全部行为
        map.put("downloads",logService.chartCount(SysConstant.DOWNLOAD));//下载消费行为
//        map.put("favs",logService.chartCount(SysConstant.FAV));
        return ServerResponse.ofSuccess(map);
    }

    @RequestMapping(value = "/plays", method = RequestMethod.GET)
    public ServerResponse Plays() throws ParseException {
        Map map = new HashMap();
        //最新的15个登陆日期
        map.put("xData",logService.chartDay(SysConstant.PLAY));
        map.put("plays",logService.chartCount(SysConstant.PLAY));//某一天的播放次数

        return ServerResponse.ofSuccess(map);
    }

    @RequestMapping(value = "/top5singers", method = RequestMethod.GET)
    public ServerResponse Top5singers() throws ParseException {
        Map map = new HashMap();
//        List<String> singers = new ArrayList<>();
        List<String> singers = songDao.getTopSingers();
//        QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
//        for (ThreeData data : datas) {
//            Song song = songDao.queryByIid(data.getName2());
//            singers.add(song.getSongName());
//        }

        map.put("topsingers",singers);

        return ServerResponse.ofSuccess(map);
    }


    @RequestMapping(value = "/getcup", method = RequestMethod.GET)
    public ServerResponse Getcup() throws ParseException {
        Map map = new HashMap();
        map.put("CpuRatio", osutils.getCpuRatio());

        return ServerResponse.ofSuccess(map);
    }
}
