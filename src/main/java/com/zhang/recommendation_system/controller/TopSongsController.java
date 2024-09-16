package com.zhang.recommendation_system.controller;

import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.service.ITopSongsService;
import com.zhang.recommendation_system.util.timeUtil.MyTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * @author ZhangChaojie
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2021/5/1 15:28
 */
@RestController
public class TopSongsController {
    @Autowired
    private ITopSongsService iTopSongsService;

    public void recommend(){
        String record_day = iTopSongsService.get_record_day();
        System.out.println(MyTimeUtil.timeFormat(record_day));
        System.out.println(MyTimeUtil.timeFormat(System.currentTimeMillis() + ""));
//        Process proc;
//        try {
//            proc = Runtime.getRuntime().exec("python D:\\毕设\\music\\update_recommend.py");// 执行py文件
//            //用输入输出流来截取结果
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            proc.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
