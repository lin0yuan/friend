package com.ayit.friend.mq;

import com.alibaba.fastjson.JSON;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.service.MessageService;
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

import java.time.LocalDateTime;
import java.time.ZoneId;


@Component
public class MessageMq {
    @Autowired
    private MessageService messageService;
    @Autowired
    private RestHighLevelClient client;
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "message.delete"),
            exchange = @Exchange(name = "ayit.message",type = ExchangeTypes.DIRECT),
            key = "delete"
    ))
    public void listenDeleteMessage(String id){
        DeleteRequest request = new DeleteRequest("friend_message",id);
        client.delete(request, RequestOptions.DEFAULT);
    }
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "message.insert"),
            exchange = @Exchange(name = "ayit.message",type = ExchangeTypes.DIRECT),
            key = "insert"
    ))
    public void listenInsertMessage(String id){
        Message message = messageService.getById(id);
        IndexRequest request = new IndexRequest("friend_message").id(id);
        request.source(JSON.toJSONString(message), XContentType.JSON);
        client.index(request,RequestOptions.DEFAULT);
    }
}
