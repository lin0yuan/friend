package com.ayit.friend.controller;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.Follow;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.vo.UserVO;
import com.ayit.friend.service.FollowService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.List;

@RequestMapping("/follow")
@Api(tags = "关系控制器")
@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/addTutor")
    @ApiOperation(value = "添加关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tutorId",value = "关注对象id",required = true,dataTypeClass = Long.class)
    })
    public RespBean<String> addTutor(@RequestParam Long tutorId){
        followService.addTutor(tutorId);
        return RespBean.success("关注成功");
    }

    @DeleteMapping("/deleteTutor")
    @ApiOperation(value = "取消关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tutorId",value = "关注对象id",required = true,dataTypeClass = Long.class)
    })
    public RespBean<String> deleteTutor(@RequestParam Long tutorId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Follow follow = new Follow(user.getId(),tutorId);
        followService.removeFollow(follow);
        return RespBean.success("已取消关注");
    }

    @DeleteMapping("/deleteSubscriber")
    @ApiOperation(value = "拉黑粉丝")
    @ApiImplicitParam(name = "subscriberId",value = "粉丝id",required = true)
    public RespBean<String> deleteSubscriber(@RequestParam Long subscriberId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Follow follow = new Follow(subscriberId,user.getId());
        followService.removeFollow(follow);
        return RespBean.success("拉黑成功");
    }

    @GetMapping("/relation")
    @ApiOperation(value = "判断关系")
    @ApiResponse(description = "0=无关系,1=粉丝,2=博主,3=朋友")
    @ApiImplicitParam(name = "toUserId",value = "对象id",required = true)
    public RespBean<Integer> queryRelation(@RequestParam Long toUserId){
        Integer relation = followService.getRelation(toUserId);
        return RespBean.success(relation);
    }
    @GetMapping("/Mutually")
    @ApiOperation(value = "朋友列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码",required = true,dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "size",value = "每页条数",required = true,dataTypeClass = Integer.class)
    })
    public RespBean<Page<User>> listMutually(@RequestParam Integer page, @RequestParam Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<User> userList = followService.pageMutually(user.getId(),page,size);
        return RespBean.success(userList);
    }

    @GetMapping("/tutorList")
    @ApiOperation(value = "关注列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码",required = true,dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "size",value = "每页条数",required = true,dataTypeClass = Integer.class)
    })
    public RespBean<Page<User>> searchTutor(@RequestParam Integer page,@RequestParam Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<User> userList = followService.listTutor(user.getId(),page,size);
        return RespBean.success(userList);
    }

    @GetMapping("/subscriberList")
    @ApiOperation(value = "粉丝列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码",required = true,dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "size",value = "每页条数",required = true,dataTypeClass = Integer.class)
    })
    public RespBean<Page<User>> searchSubscriber(@RequestParam Integer page,@RequestParam Integer size){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<User> userList = followService.listSubscriber(user.getId(),page,size);
        return RespBean.success(userList);
    }

}
