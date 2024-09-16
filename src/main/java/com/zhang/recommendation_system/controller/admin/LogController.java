package com.zhang.recommendation_system.controller.admin;


import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.recommendation_system.pojo.admin.Log;
import com.zhang.recommendation_system.service.LogService;
import com.zhang.recommendation_system.service.impl.TokenService;
import com.zhang.recommendation_system.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author redcomet
 * @since 2021-04-19
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/modify")
    public ServerResponse modify(@RequestBody Log record) {
        return logService.updateById(record) ? ServerResponse.ofSuccess("更新成功！") : ServerResponse.ofError("更新失败！");
    }

    @GetMapping("/delete/{id}")
    public ServerResponse delete(@PathVariable("id") Integer id) {
        return logService.removeById(id) ? ServerResponse.ofSuccess("删除成功！") : ServerResponse.ofError("删除失败！");
    }

    @GetMapping("/{id}")
    public ServerResponse query(@PathVariable("id") Integer id) {
        return ServerResponse.ofSuccess(logService.getById(id));
    }

    //按页查询日志数据
    @GetMapping("/logs/{page}")
    public ServerResponse querys(@PathVariable("page") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        Page<Log> pages = new Page<>(page, limit);
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>().eq("deleted",false);//没有被删除的日志
        IPage<Log> iPage = logService.page(pages, wrapper);
        return ServerResponse.ofSuccess(iPage);
    }

    @GetMapping("/logs")
    public ServerResponse querys2() {
        Page<Log> pages = new Page<>(1, 1000);
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>().eq("deleted",false);//没有被删除的日志
        IPage<Log> iPage = logService.page(pages, wrapper);
        return ServerResponse.ofSuccess(iPage);
    }

    //日志搜索
    @GetMapping({"/search/{keyword}","/search/"})
    public ServerResponse search(@PathVariable(value = "keyword",required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(!StringUtils.isEmpty(keyword), "args", keyword).or().eq("opt", keyword)
                .eq("deleted",false);
        Page<Log> pages = new Page<>(1, 1000);
        IPage<Log> iPage = logService.page(pages, wrapper);
        if (iPage != null) {
            return ServerResponse.ofSuccess(iPage);
        }
        return ServerResponse.ofError("查询不到数据!");
    }
}

