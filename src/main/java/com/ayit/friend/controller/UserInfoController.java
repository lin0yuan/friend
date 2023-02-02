package com.ayit.friend.controller;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.UserInfo;
import com.ayit.friend.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
@Api(tags = "详情控制器")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/update")
    @ApiOperation("修改用户信息")
    @ApiImplicitParam(name = "userInfo",value = "用户信息",required = true,dataTypeClass = UserInfo.class)
    public RespBean<String> updateUserInfo(@RequestBody UserInfo userInfo){
        userInfoService.update(userInfo);
        return RespBean.success("修改成功");
    }

    @GetMapping("/query")
    @ApiOperation("获取用户详情信息")
    public RespBean<UserInfo> getUserInfo(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfo userInfo = userInfoService.getById(user.getId());
        return RespBean.success(userInfo);
    }

}
