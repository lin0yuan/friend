package com.ayit.friend.controller;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.enumeration.RoleType;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.vo.MessageVO;
import com.ayit.friend.service.MessageService;
import com.ayit.friend.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理控制器")
public class AdminController {

    private static final Long ADMIN_ROLE = 1L;

    @Autowired
    private UserService userService;

    @PutMapping("/updateStatus")
    @ApiOperation(value = "修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataTypeClass = Long.class),
            @ApiImplicitParam(name = "state",value = "状态",required = true,dataTypeClass = Integer.class)
    })
    public RespBean<String> updateStatus(@RequestParam Long id,@RequestParam Integer state){
        userService.updateState(id,state);
        return RespBean.success("修改成功");
    }

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    @ApiOperation("获取反馈信息")
    @ApiImplicitParam(name = "messageVO",value = "查询信息",required = true,dataTypeClass = String.class)
    public RespBean<Page<Message>> pageMessage(@RequestBody MessageVO messageVO){
        Page<Message> messagePage = messageService.searchAdmin(messageVO);
        return RespBean.success(messagePage);
    }

}
