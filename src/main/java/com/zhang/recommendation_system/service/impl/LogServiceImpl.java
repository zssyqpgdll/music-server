package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.LogDao;
import com.zhang.recommendation_system.pojo.admin.ChartData;
import com.zhang.recommendation_system.pojo.admin.Log;
import com.zhang.recommendation_system.service.LogService;
import com.zhang.recommendation_system.util.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {

    @Autowired
    private LogService logService;

    @Override
    public List<Integer> chartCount(String  opt) throws ParseException {
        List<Integer> values = new ArrayList<>();
        List<String> days = baseMapper.chart(opt);
        for(String _day : days){
            QueryWrapper<Log> query = new QueryWrapper<>();
            query.like("DATE_FORMAT(create_time,'%Y-%m-%d')",_day);
            query.eq("opt", opt);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
//            query.setCreateTime(simpleDateFormat.parse(_day));
            values.add(baseMapper.selectCount(query));//计算有多少条数据（当天有多少条播放数据）
        }
        return values;
    }

    @Override
    public List<String> chartDay(String opt) throws ParseException {
        return  baseMapper.chart(opt);
    }

    @Override
    public List<ChartData> chartData(String type) {
        if("GROUP_MAN".equals(type))
            return baseMapper.groupManCount();
        else if("ROLE_MAN".equals(type))
            return baseMapper.roleManCount();
        else
            return new ArrayList<>();
    }

    @Override
    public List<Integer> dayCount() throws ParseException {
        List<Integer> values = new ArrayList<>();
        List<String> days = logService.chartDay(SysConstant.DOWNLOAD);
        for(String _day : days){
            QueryWrapper<Log> query = new QueryWrapper<>();
            query.like("DATE_FORMAT(create_time,'%Y-%m-%d')",_day);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
//            query.setCreateTime(simpleDateFormat.parse(_day));
            values.add(baseMapper.selectCount(query));//计算有多少条数据（当天有多少条数据）

//            System.out.println("\n\n\n\n\n\n\n\n\n\n某一天总行为" + baseMapper.selectCount(query) + "\n\n\n\n\n\n\n\n\n\n\n");

        }
        return values;
    }
}
