package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.dao.SingerMapper;
import com.zhang.recommendation_system.pojo.Singer;
import com.zhang.recommendation_system.service.ISingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhangChaojie
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2021/4/26 23:08
 */
@Service
public class SingerServiceImpl  extends ServiceImpl<SingerMapper, Singer>  implements ISingerService {
    @Autowired
    SingerMapper singerMapper;
    @Override
    public String queryUidByName(String name) {
        return singerMapper.queryByName(name);
    }
}
