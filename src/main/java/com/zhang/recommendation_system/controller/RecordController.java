package com.zhang.recommendation_system.controller;

import com.alibaba.fastjson.JSON;
import com.zhang.recommendation_system.pojo.Record;
import com.zhang.recommendation_system.service.IRecordService;
import com.zhang.recommendation_system.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangChaojie
 * @Description: TODO(播放记录)
 * @date 2021/5/21 23:47
 */
@RestController
public class RecordController {
    @Autowired
    IRecordService iRecordService;

    /**
     * 添加播放记录
     * @param record
     * @return
     */
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Result addRecord(@RequestBody Record record) {
        System.out.println(record);
        return iRecordService.addRecord(record);
    }

    /**
     * 获得用户的播放记录
     * @param uid
     * @return
     */
    @RequestMapping(value = "/getRecordSong", method = RequestMethod.POST)
    public Result getRecord(@RequestBody String uid) {
        System.out.println(uid);
        String uid_str = JSON.parseObject(uid).get("uid").toString();
        return iRecordService.queryRecordByUid(uid_str);
    }

    //user-record页面
    @RequestMapping(value = "/getRecordSong1", method = RequestMethod.POST)
    public Result getRecord1(@RequestBody String uid) {
        System.out.println(uid);
        String uid_str = JSON.parseObject(uid).get("uid").toString();
        // 获得当前页面的整数
        int num = Integer.parseInt(JSON.parseObject(uid).get("currentPage").toString());
        int currIndex = (num - 1) * 10;
        return iRecordService.queryRecordByUid1(uid_str, currIndex, 10);
    }
}
