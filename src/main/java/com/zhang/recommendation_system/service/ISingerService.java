package com.zhang.recommendation_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.recommendation_system.pojo.Singer;

public interface ISingerService extends IService<Singer> {
    /**
     * 根据歌手姓名查找歌手id
     * @param name 歌手姓名
     * @return
     */
    String queryUidByName(String name);
}
