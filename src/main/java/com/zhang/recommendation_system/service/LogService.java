package com.zhang.recommendation_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import com.zhang.recommendation_system.pojo.admin.Log;

import java.text.ParseException;
import java.util.List;

public interface LogService extends IService<Log> {
    List<Integer> chartCount(String opt) throws ParseException;

    List<String> chartDay(String opt) throws ParseException;

    List<ChartData> chartData(String type);

    //查询tb_log表中某一天的全部行为个数
    List<Integer> dayCount() throws ParseException;
}
