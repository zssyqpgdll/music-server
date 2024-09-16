package com.zhang.recommendation_system.controller.admin;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.recommendation_system.dao.SongMapper;
import com.zhang.recommendation_system.pojo.Song;
import com.zhang.recommendation_system.pojo.admin.SearchRequest;
import com.zhang.recommendation_system.service.ISongService;
import com.zhang.recommendation_system.util.RandomUtil;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.httpUtil.SongDetail;
import com.zhang.recommendation_system.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 音乐
 * @author redcomet
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/song")
public class SongController2 {

    @Autowired
    private ISongService service;

    @Autowired
    SongMapper songMapper;

    @PostMapping("/modify")
    public ServerResponse modify(@RequestBody Song record) {
        return service.updateById(record) ? ServerResponse.ofSuccess("更新成功！") : ServerResponse.ofError("更新失败！");
    }

    @GetMapping("/delete/{id}")
    public ServerResponse delete(@PathVariable("id") Integer id) {
        return service.removeById(id) ? ServerResponse.ofSuccess("删除成功！") : ServerResponse.ofError("删除失败！");
    }

    @GetMapping("/geturl/{id}")
    public ServerResponse geturl(@PathVariable("id") Integer id) {
        Song songRes = service.getById(id.toString());
        if (songRes == null) {
            return ServerResponse.ofError("该歌曲不存在！");
        }
        System.out.println(songRes.getIid());
        songRes.setDownUrl(SongDetail.getSongMP3Url(id.toString()));
        return service.updateById(songRes) ? ServerResponse.ofSuccess("更新成功！") : ServerResponse.ofError("更新失败！");
    }

    @GetMapping("/{id}")
    public ServerResponse query(@PathVariable("id") Integer id) {
        return ServerResponse.ofSuccess(service.getById(id));
    }

    @GetMapping("/records/{page}")
    public ServerResponse querys(@PathVariable("page") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        Page<Song> pages = new Page<>(page, limit);
        //根据发布时间从近到远排序
        QueryWrapper<Song> wrapper = new QueryWrapper<Song>()
                .orderByDesc("publishTime");
        IPage<Song> iPage = service.page(pages, wrapper);
        for(int i=0;i<iPage.getSize();i++){
            Song song = iPage.getRecords().get(i);
            song.setDownUrl(SongDetail.getSongMP3Url(song.getIid()));
        }
        return ServerResponse.ofSuccess(iPage);
    }

    @PostMapping({"/search"})
    public ServerResponse search(@RequestBody SearchRequest query, @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<Song> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("publishTime");//根据发布时间从近到远排序
        //根据歌曲名称或者歌手id查询
        wrapper.like(!StringUtils.isEmpty(query.getKeyword()), "song_name", query.getKeyword()).
                or().eq("suid", query.getKeyword());
        Page<Song> pages = new Page<>(page, limit);
        IPage<Song> iPage = service.page(pages, wrapper);
        if (iPage != null) {
            return ServerResponse.ofSuccess(iPage);
        }
        return ServerResponse.ofError("查询不到数据!");
    }

    //添加歌曲
    @PostMapping("/add")
    public ServerResponse add(@RequestBody Song record) {
        // 得到要给10位的随机数字串
        record.setIid(RandomUtil.getNBitRandomDigit(10));
        // 设置创建时间
        record.setPublishTime(System.currentTimeMillis() + "");
        boolean b = service.save(record);
        if (b) {
            return ServerResponse.ofSuccess("添加成功", record);
        }
        return ServerResponse.ofError("添加失败!");
    }

    /* 给前端用的 */
//    @GetMapping({"/fontsearch/{username}"})
//    public ServerResponse fontsearch(@PathVariable(value = "username",required = false) String username, @RequestParam(defaultValue = "1") Integer page,
//                                     @RequestParam(defaultValue = "10") Integer limit) {
//        QueryWrapper<Group> wrapper = new QueryWrapper<>();
//        wrapper.orderByDesc("update_time");
//        wrapper.like(!StringUtils.isEmpty(username), "author", username)
//                .eq("deleted",false);
//        Page<Group> pages = new Page<>(page, limit);
//        IPage<Group> iPage = groupService.page(pages, wrapper);
//        if (page != null) {
//            return ServerResponse.ofSuccess(iPage);
//        }
//        return ServerResponse.ofError("查询不到数据!");
//    }
}

