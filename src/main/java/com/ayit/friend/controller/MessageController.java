package com.ayit.friend.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.ayit.friend.common.CustomException;
import com.ayit.friend.enumeration.FileType;
import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.vo.MessageVO;
import com.ayit.friend.service.MessageService;
import com.ayit.friend.utils.AliyunOssUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


@RequestMapping("/chat")
@Api(tags = "聊天控制器")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AliyunOssUtil aliyunOssUtil;


    @ApiOperation("上传文件")
    @PostMapping("/toFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartFile",value = "图片/文件/音频/音频",dataTypeClass = MultipartFile.class)
    })
    public RespBean<Message> sendFile(@RequestPart MultipartFile multipartFile) throws IOException {
        Message message = new Message();
        String contentType = multipartFile.getContentType();
        for(FileType fileType:FileType.values()){
            if(contentType.startsWith(fileType.getValue())){
                message.setMessageType(fileType.getExt());
            }
        }
        message.setMessageText(multipartFile.getOriginalFilename());
        String url = aliyunOssUtil.upload(multipartFile.getInputStream(), message.getMessageType(),multipartFile.getOriginalFilename());
        message.setFile(url);
        return RespBean.success(message);
    }

    @ApiOperation("获取聊天记录")
    @PostMapping("/messages")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageVO",value = "搜索条件",dataTypeClass = MessageVO.class)
    })
    public RespBean<Page<Message>> messageList(@RequestBody MessageVO messageVO){
        Page<Message> messageList = messageService.search(messageVO);
        return RespBean.success(messageList);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @ApiOperation("撤回消息")
    @DeleteMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId",value = "消息id",required = true,dataTypeClass = Long.class),
            @ApiImplicitParam(name = "userId",value = "用户",required = true,dataTypeClass = Long.class),
    })
    public RespBean<Message> deleteMessage(@RequestParam Long userId,@RequestParam Long messageId){
        Message message = messageService.removeById(userId,messageId);
        return RespBean.success(message);
    }

}
