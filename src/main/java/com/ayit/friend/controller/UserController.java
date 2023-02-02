package com.ayit.friend.controller;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.vo.MessageVO;
import com.ayit.friend.pojo.vo.UserVO;
import com.ayit.friend.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户控制器")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "emailAddress", value = "用户账户", required = true,dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true,dataTypeClass = String.class),
            @ApiImplicitParam(name="remember-me",value = "记住密码",dataTypeClass = String.class)
    })
    public RespBean<User> login(@RequestParam String emailAddress,@RequestParam String password)
    {
        return null;
    }

    @GetMapping("/logout")
    @ApiOperation(value = "用户退出")
    public RespBean<Boolean> logout(){
        return RespBean.success(true);
    }



    @PostMapping("/searchUser")
    @ApiOperation(value = "搜索用户")
    @ApiImplicitParam(name = "userVO",value = "搜索信息",required = true,dataTypeClass = UserVO.class)
    public RespBean<Page<User>> searchUser(@RequestBody UserVO userVO){
        Page<User> userPage = userService.pageUser(userVO);
        return RespBean.success(userPage);
    }

    @GetMapping("/getUser")
    @ApiOperation("用户信息")
    @ApiImplicitParam(name = "userId",value = "对象id",dataTypeClass = Long.class)
    public RespBean<User> getUser(@RequestParam Long userId){
        User user = userService.getUserById(userId);
        return RespBean.success(user);
    }

    @GetMapping("/checkPwd")
    @ApiOperation("检查密码")
    @ApiImplicitParam(name="password",value = "密码",required = true,dataTypeClass = String.class)
    public RespBean<Boolean> checkPassword(@RequestParam String password){
        boolean isTrue = userService.checkPassword(password);
        return RespBean.success(isTrue);
    }

    @PutMapping("/gender")
    @ApiOperation("修改性别")
    @ApiImplicitParam(name = "gender",value = "性别",required = true,dataTypeClass = Integer.class)
    public RespBean<String> updateGender(@RequestParam Integer gender){
        userService.updateGender(gender);
        return RespBean.success("修改成功");
    }

    @PutMapping("/password")
    @ApiOperation("修改密码")
    @ApiImplicitParam(name = "password",value = "密码",required = true,dataTypeClass = String.class)
    public RespBean<String> updatePassword(@RequestParam String password){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updatePassword(user.getId(),password);
        return RespBean.success("修改成功");
    }
}
