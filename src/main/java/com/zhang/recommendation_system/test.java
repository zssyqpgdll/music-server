package com.zhang.recommendation_system;

import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.util.timeUtil.MyTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class test {
    @Autowired
    private UserMapper userMapper;

    public void recommend(){
//        String record_day = userMapper.get_record_day();
//        System.out.println(MyTimeUtil.timeFormat(record_day));
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