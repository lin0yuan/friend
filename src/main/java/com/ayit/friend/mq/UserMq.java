package com.ayit.friend.mq;

import com.alibaba.fastjson.JSON;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.UserInfo;
import com.ayit.friend.service.UserInfoService;
import com.ayit.friend.service.UserService;
import lombok.SneakyThrows;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserMq {

    @Autowired
    private UserService userService;
    @Autowired
    private RestHighLevelClient client;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.insert"),
            exchange = @Exchange(name = "ayit.user",type = ExchangeTypes.DIRECT),
            key = "insert"
    ))
    public void listenInsertUser(String id){
        User user = userService.getById(id);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Long.valueOf(id));
        userInfoService.save(userInfo);
        IndexRequest request = new IndexRequest("friend_user").id(id);
        request.source(JSON.toJSONString(user), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.delete"),
            exchange = @Exchange(name = "ayit.user",type = ExchangeTypes.DIRECT),
            key = "delete"
    ))
    public void listenDeleteUser(String id){
        DeleteRequest request = new DeleteRequest("friend_user",id);
        client.delete(request,RequestOptions.DEFAULT);
    }
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "user.update"),
            exchange = @Exchange(name = "ayit.user",type = ExchangeTypes.DIRECT),
            key = "update"
    ))
    public void listenUpdateUser(String id){
        UserInfo userInfo = userInfoService.getById(id);
        UpdateRequest request = new UpdateRequest("friend_user",id);
        Field[] fields = userInfo.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap<>();
        for(Field field:fields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(userInfo));
        }
        request.doc(map);
        client.update(request,RequestOptions.DEFAULT);
    }

    @Autowired
    private UserInfoService userInfoService;


}
