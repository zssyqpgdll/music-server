package com.zhang.recommendation_system.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.recommendation_system.config.SysLog;
import com.zhang.recommendation_system.dao.AdminMapper;
import com.zhang.recommendation_system.dao.UserMapper;
import com.zhang.recommendation_system.pojo.Admin;
import com.zhang.recommendation_system.pojo.User;
import com.zhang.recommendation_system.pojo.admin.SearchRequest;
import com.zhang.recommendation_system.pojo.admin.UserLoginRequest;
import com.zhang.recommendation_system.service.IAdminService;
import com.zhang.recommendation_system.service.IUserService;
import com.zhang.recommendation_system.service.impl.TokenService;
import com.zhang.recommendation_system.util.RandomUtil;
import com.zhang.recommendation_system.util.ServerResponse;
import com.zhang.recommendation_system.util.SysConstant;
import com.zhang.recommendation_system.util.result.Result;
import com.zhang.recommendation_system.util.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redcomet
 * @since 2021-04-02
 */
@RestController
@RequestMapping("/user")
public class UserController2 {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;

    //@SysLog("测试")
    //后台管理系统的登陆接口，/user/login
    @PostMapping("/login")
    @SysLog(value= SysConstant.LOGIN)//记入tb_log表中
    public ServerResponse login(@RequestBody UserLoginRequest adminLoginRequest) throws Exception{
        Map<String, Object> map = new HashMap();
        Admin admin = new Admin();
        admin.setName(adminLoginRequest.getUsername());
        admin.setPassword(adminLoginRequest.getPassword());
        if(adminService.adminLogin(admin).getSucc()){//即存在该管理员
            String token = tokenService.getToken1(admin);
            map.put("user", admin);
            map.put("token", token);
            return ServerResponse.ofSuccess(map);
        }else{
            return ServerResponse.ofError("用户名或密码错误!");
        }
        //根据用户名和密码找到指定的用户
//        User user = userService.login(adminLoginRequest.getUsername(), adminLoginRequest.getPassword());
//        if (user != null){
//            String token = tokenService.getToken(user);
//            map.put("user", user);
//            map.put("token", token);
//            return ServerResponse.ofSuccess(map);
//        }
//        //若不存在该用户，则返回错误信息
//        return ServerResponse.ofError("用户名或密码错误!");
    }

    @GetMapping("/userInfo/{id}")
    public ServerResponse personInfo(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap();
        User user = userService.info(Integer.valueOf(id));
        if (user != null){
            map.put("userinfo", user);
            return ServerResponse.ofSuccess(map);
        }
        return ServerResponse.ofError("查询失败!");
    }

    @GetMapping("/info")
    public ServerResponse info(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        String token = request.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
//        User user = userService.info(Integer.valueOf(userId));
        Admin admin = adminMapper.queryByName(userId);
        if (admin != null){
            map.put("userinfo", admin);
            return ServerResponse.ofSuccess(map);
        }
        return ServerResponse.ofError("查询失败!");
    }
    /**
     * 更新用户资料
     * @return
     */
    @PostMapping("/modify")
    public ServerResponse modify(@RequestBody User user) {
        return userService.updateById(user) ? ServerResponse.ofSuccess("更新成功！") : ServerResponse.ofError("更新失败！");
    }

    //参数user只有一个id字段
    @PostMapping("/del")
    public ServerResponse delete(@RequestBody User user) {
        return userService.removeById(user) ? ServerResponse.ofSuccess("删除成功！") : ServerResponse.ofError("删除失败！");
    }

    /**
     * 根据ID查询管理员信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ServerResponse queryUser(@PathVariable("id") Integer id) {
        return ServerResponse.ofSuccess(userService.getById(id));
    }

    //查询某一页的用户
    @GetMapping("/users/{page}")
    public ServerResponse queryUsers(@PathVariable("page") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        Page<User> pages = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        IPage<User> iPage = userService.page(pages, wrapper);
        return ServerResponse.ofSuccess(iPage);
    }

    @PostMapping("/search2")
    public ServerResponse search2(@RequestBody SearchRequest params,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("registerTime");//按注册时间排序
        //根据用户名或id查询
        wrapper.like(!StringUtils.isEmpty(params.getKeyword()), "name", params.getKeyword()).or().eq("uid", params.getKeyword());
        Page<User> pages = new Page<>(page, limit);
        IPage<User> iPage = userService.page(pages, wrapper);
        if (iPage != null) {
            return ServerResponse.ofSuccess(iPage);
        }
        return ServerResponse.ofError("查询不到数据!");
    }

    @GetMapping({"/search/{keyword}","/search/"})
    public ServerResponse searchUser(@PathVariable(value = "keyword",required = false) String keyword, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_time");
        wrapper.like(!StringUtils.isEmpty(keyword), "realname", keyword)
                .eq("deleted",false);
        Page<User> pages = new Page<>(page, limit);
        IPage<User> iPage = userService.page(pages, wrapper);
        if (page != null) {
            return ServerResponse.ofSuccess(iPage);
        }
        return ServerResponse.ofError("查询不到数据!");
    }

    @PostMapping("/resetpass/{id}")
    public ServerResponse resetpass(@PathVariable Integer id) {
        User t = new User();
        t.setId(id);
        t.setPassword("123456");
        boolean b = userService.saveOrUpdate(t);
        if(b) {
            return ServerResponse.ofSuccess("成功！");
        }
        return ServerResponse.ofError("失败！");
    }

    //添加用户
    @PostMapping("/add")
    public ServerResponse addUser(@RequestBody User user) {
        // 得到要给10位的随机数字串
        user.setUid(RandomUtil.getNBitRandomDigit(10));
        User res = userMapper.queryById(user.getUid());
        if (res != null) {
            return ServerResponse.ofError("添加失败!");
        }
        user.setRegisterTime(System.currentTimeMillis() + "");
        boolean b = userService.save(user);
        if (b) {
            return ServerResponse.ofSuccess("添加成功", user);
        }
        return ServerResponse.ofError("添加失败!");
    }

//    @GetMapping("/admin")
//    public ServerResponse getadminInfo(@RequestBody String realname) {
//        System.out.println(JSON.parseObject(realname).get("realname").toString() + "\n\n\n\n\n\n\n");
////        JSONObject parse = JSON.parseObject(realname.toString());
//        return ServerResponse.ofSuccess();
////        adminMapper.queryByName(parse.getString("realname"))
//    }

//    @RequestMapping(value = "/admin",method = RequestMethod.POST)
//    public ServerResponse getadminInfo(@RequestBody String admin) {
//        System.out.println(admin + "\n\n\n\n\n\n\n\n\n\n");
//        return ServerResponse.ofSuccess();
//    }

//    @PostMapping("/register")
//    public ServerResponse register(@RequestBody User user) {
//        user.setUserId(user.getUsername());
//        user.setUserType(1);
//        user.setRemark("China");
//        if(user.getGroupId()==null)
//            user.setGroupId(2);
//        boolean b = userService.save(user);
//        if (b) {
//            return ServerResponse.ofSuccess("添加成功", user);
//        }
//        return ServerResponse.ofError("添加失败!");
//    }
}

