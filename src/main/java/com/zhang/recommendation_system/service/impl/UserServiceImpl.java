package com.zhang.recommendation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.recommendation_system.controller.SongController;
import com.zhang.recommendation_system.dao.*;
import com.zhang.recommendation_system.pojo.*;
import com.zhang.recommendation_system.service.IUserService;
import com.zhang.recommendation_system.util.RandomUtil;
import com.zhang.recommendation_system.util.httpUtil.SongDetail;
import com.zhang.recommendation_system.util.result.Result;
import com.zhang.recommendation_system.util.result.ResultUtil;
import com.zhang.recommendation_system.util.timeUtil.MyTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ZhangChaojie
 * @Description: TODO(用户业务层实现类)
 * @date 2021/4/26 19:45
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private TopSongsMapper topSongsMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private TopUsersMapper topUsersMapper;

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", username);
        wrapper.eq("password", password);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public User info(Integer id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Result userLogin(User user) {
        if (user != null) {
            User res = userMapper.queryById(user.getUid());
            // 判空并比较密码，这里密码实际上需要加密，类似使用MD5
            if (res != null && user.getPassword().equals(res.getPassword())) {
                // 将密码隐藏，注册时间格式化
                res.setPassword("*********");
                res.setRegisterTime(MyTimeUtil.timeFormat(res.getRegisterTime()));
                return ResultUtil.success(res);
            } else {
                return ResultUtil.fail(400, user.getName() + "用户不存在");
            }
        }
        return ResultUtil.fail(400, "用户不存在");
    }

    @Override
    public Result addUser(User user) {
        if (user != null) {
            // 得到要给10位的随机数字串
            user.setUid(RandomUtil.getNBitRandomDigit(10));
            User res = userMapper.queryById(user.getUid());
            if (res != null) {
                return ResultUtil.fail(410, "该用户已存在");
            }
            // 这里的密码实际应当加密之后再添加到数据库中
            // 设置注册时间
            user.setRegisterTime(System.currentTimeMillis() + "");
            userMapper.addUser(user);
            user.setPassword("********");
            // 密码隐藏
            return ResultUtil.success(user);
        }
        return ResultUtil.fail(420, "注册失败，请稍后重试！");
    }

    @Override
    public Result updateUser(User user) {
        // 判空
        if (user != null) {
            User res = userMapper.queryById(user.getUid());
            if (res == null) {
                return ResultUtil.fail(430, "该用户不存在，更新失败，请稍后重试！");
            }
            userMapper.updateUser(user);
            user.setPassword("********");
            user.setRegisterTime(MyTimeUtil.timeFormat(res.getRegisterTime()));
            // 更新成功则返回
            return ResultUtil.success(user);
        } else {
            return ResultUtil.fail(430, "更新失败，请稍后再试！");
        }
    }

    @Override
    public Result updatePw(String uid, String oldPw, String newPw) {
        // 判空
        if (uid != null) {
            User res = userMapper.queryById(uid);
            // 判断改用户是否存在
            if (res == null) {
                return ResultUtil.fail(420, "该用户不存在，更新失败，请稍后再试！");
            }
            //判断密码原密码是否正确
            if (res.getPassword().equals(oldPw)) {
                userMapper.updatePs(uid, newPw);
                return ResultUtil.success();
            } else {
                return ResultUtil.fail(420, "原密码错误，修改失败！");
            }
        } else {
            return ResultUtil.fail(430, "更新失败，请稍后再试！");
        }
    }

    @Override
    public Result getUserRecord(String uid) {
        return ResultUtil.success(recordMapper.queryByUid(uid));
    }

    @Override
    public Result getRecommendSongs(String uid) {
        // 获得用户播放记录
        List<Record> records = recordMapper.queryByUid(uid);
        if (records.isEmpty()) {
            // 如果是新用户,没有听歌记录,那就把热门歌曲推荐给他
            List<Song> songList = songMapper.querySongsHot(0, 20);
            // 设置歌曲内容
            for (int i = 0; i < songList.size(); i++) {
                Song song = songList.get(i);
                // 设置音乐时长
                song.setSongTime(MyTimeUtil.millSeconds2time(song.getSongTime()));
                // 格式化发行时间
                song.setPublishTime(MyTimeUtil.timeFormat(song.getPublishTime()));
                // 设置音乐播放链接
                song.setDownUrl(SongDetail.getSongMP3Url(song.getIid()));
            }
            return ResultUtil.success(songList);
//            return ResultUtil.fail(500, "获取数据失败！");
        }
        // 根据播放记录的权重从大到小排序
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Integer.parseInt(o2.getWeight()) - Integer.parseInt(o1.getWeight());
            }
        });
        // 相似歌曲列表
        List<Song> songList = new ArrayList<>();
        // 获得相似音乐
        TopSongs topSongs = topSongsMapper.queryByIid(records.get(0).getIid());
        if (topSongs == null) {
            return ResultUtil.fail(500, "获取相似音乐失败！");
        }
        // 相似歌曲id集合
        List<String> iidList = Arrays.asList(topSongs.getTopSongs().split(","));
        for (String tempId : iidList) {
            Song song = songMapper.queryByIid(tempId);
            // 设置音乐时长
            song.setSongTime(MyTimeUtil.millSeconds2time(song.getSongTime()));
            // 设置音乐播放链接
            song.setDownUrl(SongDetail.getSongMP3Url(song.getIid()));
            songList.add(song);
        }
        // 返回用户的推荐歌曲
        return ResultUtil.success(songList);
    }

    @Override
    public Result getRecommendUsers(String uid) {
        // 相似用户数组
        List<User> userList = new ArrayList<>();
        // 获得相似音乐好友
        TopUsers topUsers = topUsersMapper.queryByUid(uid);
        if (topUsers == null) {
            //如果是新用户,给他推荐跟他同地区的用户
            List<User> areaUser = topUsersMapper.getByArea(uid);
            for (User user : areaUser) {
                // 设置密码和注册时间
                user.setPassword("********");
//                user.setRegisterTime(MyTimeUtil.timeFormat(user.getRegisterTime()));
                userList.add(user);
            }
            return ResultUtil.success(userList);
//            return ResultUtil.fail(500, "获取相似好友失败！");
        }
        // 获得相似用户id
        List<String> uidList = Arrays.asList(topUsers.getTopUsers().split(","));
        for (String s : uidList) {
            User res = userMapper.queryById(s);
            if (res != null) {
                // 设置密码和注册时间
                res.setPassword("********");
                res.setRegisterTime(MyTimeUtil.timeFormat(res.getRegisterTime()));
                userList.add(res);
            }
        }
        // 返回相似用户数据
        return ResultUtil.success(userList);
    }

    @Override
    public User getByUid(String userId){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", userId);
        return baseMapper.selectOne(wrapper);
    }

    //查找实名认证人数，即身份证号不为空
    @Override
    public Integer getIdconfirm() {
        return userMapper.getIdconfirm();
    }
}
