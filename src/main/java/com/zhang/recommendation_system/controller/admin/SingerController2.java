package com.zhang.recommendation_system.controller.admin;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.recommendation_system.pojo.Singer;
import com.zhang.recommendation_system.pojo.admin.SearchRequest;
import com.zhang.recommendation_system.service.ISingerService;
import com.zhang.recommendation_system.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 音乐
 * @author redcomet
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/singer")
public class SingerController2 {

    @Autowired
    private ISingerService service;

    @PostMapping("/modify")
    public ServerResponse modify(@RequestBody Singer record) {
        return service.updateById(record) ? ServerResponse.ofSuccess("更新成功！") : ServerResponse.ofError("更新失败！");
    }

    @GetMapping("/delete/{id}")
    public ServerResponse delete(@PathVariable("id") Integer id) {
        return service.removeById(id) ? ServerResponse.ofSuccess("删除成功！") : ServerResponse.ofError("删除失败！");
    }

    @GetMapping("/{id}")
    public ServerResponse query(@PathVariable("id") Integer id) {
        return ServerResponse.ofSuccess(service.getById(id));
    }

    @GetMapping("/records/{page}")
    public ServerResponse querys(@PathVariable("page") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        Page<Singer> pages = new Page<>(page, limit);
        QueryWrapper<Singer> wrapper = new QueryWrapper<Singer>();
        IPage<Singer> iPage = service.page(pages, wrapper);
        return ServerResponse.ofSuccess(iPage);
    }

    @PostMapping({"/search"})
    public ServerResponse search(@RequestBody SearchRequest query, @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<Singer> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(query.getKeyword()), "singer_name", query.getKeyword()).or().eq("suid", query.getKeyword());
        Page<Singer> pages = new Page<>(page, limit);
        IPage<Singer> iPage = service.page(pages, wrapper);
        if (iPage != null) {
            return ServerResponse.ofSuccess(iPage);
        }
        return ServerResponse.ofError("查询不到数据!");
    }

    //添加歌手
    @PostMapping("/add")
    public ServerResponse add(@RequestBody Singer record) {
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

