package com.ayit.friend.controller;

import com.alibaba.cloud.context.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ayit.friend.common.CustomException;
import com.ayit.friend.enumeration.FileType;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.pojo.User;
import com.ayit.friend.service.MessageService;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.AliyunOssUtil;
import com.ayit.friend.utils.SpringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/ws/{userId}/{password}")
@Component
public class WebSocketServer {

    /**
     * 使用stomp方法实现的聊天功能
     */
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @Autowired
//    private MessageService messageService;
//
//    @MessageMapping("/ws/chat")
//    @ApiOperation("发送私聊")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "message",value = "消息信息",required = true)
//    })
//    public void handleMessage(Authentication authentication,Message message){
//        User user =(User) authentication.getPrincipal();
//        message.setFromId(user.getId());
//        messageService.save(message);
//        simpMessagingTemplate.convertAndSendToUser(message.getToId().toString(),"/queue/chat",message);
//    }
    private static int onlineCount = 0;

    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String userId="";



    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId,@PathParam("password") String password){
        UserService userService = SpringUtil.getBean(UserService.class);
        PasswordEncoder passwordEncoder = SpringUtil.getBean(PasswordEncoder.class);
        User user = userService.getById(userId);
        if(user==null){
            throw new CustomException("没有该用户");
        }
        boolean isTrue = passwordEncoder.matches(password,user.getPassword());
        if(!isTrue){
            throw new CustomException("密码错误");
        }
        userService.updateState(Long.valueOf(userId),0);
        this.session = session;
        this.userId = userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }else{
            webSocketMap.put(userId,this);
        }
    }

    @OnClose
    public void onClose(){
        UserService userService = SpringUtil.getBean(UserService.class);
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
        }
        userService.updateState(Long.valueOf(userId),1);
    }

    @OnMessage
    public void onMessage(Session session,String messageString){
        if(StringUtils.isEmpty(messageString)){
            throw new CustomException("无效信息");
        }
        Message message = JSON.parseObject(messageString,Message.class);
        message.setFromId(Long.valueOf(this.userId));
        Long toId = message.getToId();
        if(this.userId.equals(toId.toString())){
            throw new CustomException("信息发送失败");
        }
        sendInfo(message,toId.toString());

    }


    @OnError
    public void onError(Session session,Throwable throwable) {
        UserService userService = SpringUtil.getBean(UserService.class);
        userService.updateState(Long.valueOf(userId),1);
        log.error("用户错误:"+this.userId+",原因:"+throwable.getMessage());
    }

    public static void sendInfo(Message message,String toUserId){
        if(StringUtils.isNotEmpty(toUserId)){
            UserService userService = SpringUtil.getBean(UserService.class);
            MessageService messageService = SpringUtil.getBean(MessageService.class);
            RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
            User user = userService.getById(toUserId);
            if(user==null){
                throw new CustomException("没有该用户");
            }
            if(webSocketMap.get(toUserId)!=null){
                webSocketMap.get(toUserId).sendMessage(message);
            }
            messageService.save(message);
            rabbitTemplate.convertAndSend("ayit.message","insert",message.getId());
        }else{
            log.error("用户"+toUserId+",不在线！");
        }
    }

    public void sendMessage(Message message){
            try {
                this.session.getBasicRemote().sendText(JSON.toJSONString(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
