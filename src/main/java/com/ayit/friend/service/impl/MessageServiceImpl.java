package com.ayit.friend.service.impl;

import com.alibaba.cloud.context.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.ayit.friend.common.CustomException;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.vo.MessageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.service.MessageService;
import com.ayit.friend.mapper.MessageMapper;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 86138
 * @description 针对表【message(信息数据表)】的数据库操作Service实现
 * @createDate 2022-12-04 15:42:56
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService{


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public Message removeById(Long userId, Long messageId) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId()!=userId){
            throw new CustomException("无法撤回该信息");
        }
        Message message = this.getById(messageId);
        if(message.getFromId()!=userId){
            throw new CustomException("无法撤回该信息");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        long z1 = localDateTime.toEpochSecond(ZoneOffset.of("Z"));
        long z2 = message.getSentDatetime().toEpochSecond(ZoneOffset.of("Z"));
        if(z1-z2>120){
            throw new CustomException("超过两分钟");
        }
        this.removeById(messageId);
        rabbitTemplate.convertAndSend("ayit.message","delete",messageId);
        return message;
    }

    @SneakyThrows
    @Override
    public Page<Message> searchAdmin(MessageVO messageVO) {
        SearchRequest request = new SearchRequest("friend_message");
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.filter(QueryBuilders.termQuery("toId",0));
        buildBasicQuery(queryBuilder,request,messageVO);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        List<Message> messageList = handleResponse(response);
        Page<Message> messagePage = new Page<Message>()
                .setSize(messageVO.getSize()).setCurrent(messageVO.getPage()).setRecords(messageList);
        return messagePage;
    }
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @SneakyThrows
    @Override
    public Page<Message> search(MessageVO messageVO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        messageVO.setFromId(user.getId());
        SearchRequest request = new SearchRequest("friend_message");
        buildBasicQuery(builderBoolQuery(QueryBuilders.boolQuery(),messageVO),request,messageVO);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        List<Message> messageList = handleResponse(response);
        Page<Message> messagePage = new Page<Message>()
                .setSize(messageVO.getSize()).setCurrent(messageVO.getPage()).setRecords(messageList).setTotal(response.getHits().getTotalHits().value);
        return messagePage;
    }
    @Autowired
    private RestHighLevelClient client;
    private void buildBasicQuery(BoolQueryBuilder boolQuery,SearchRequest request, MessageVO messageVO) {
        if(messageVO.getMessageType()==0){
            boolQuery.filter(QueryBuilders.matchAllQuery());
        }else{
            boolQuery.filter(QueryBuilders.termQuery("messageType",messageVO.getMessageType()));
        }
        if(StringUtils.isNotEmpty(messageVO.getInfo())){
            boolQuery.must(QueryBuilders.matchQuery("messageText",messageVO.getInfo()));
            request.source().highlighter(new HighlightBuilder().field("messageText").preTags("<font color='red'>").postTags("</font>"));
        }
        if(messageVO.getStartDate()!=null){
            boolQuery.filter(QueryBuilders.rangeQuery("sentDatetime").gte(messageVO.getStartDate()).lte(messageVO.getEndDate()));
            request.source().sort("sentDatetime");
        }
        int page = messageVO.getPage();
        int size = messageVO.getSize();
        request.source().from((page - 1) * size).size(size).query(boolQuery);
    }
    private BoolQueryBuilder builderBoolQuery(BoolQueryBuilder boolQuery,MessageVO messageVO){
        BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder mustFrom = QueryBuilders.boolQuery();
        BoolQueryBuilder mustTo = QueryBuilders.boolQuery();
        mustFrom.filter(QueryBuilders.termQuery("fromId",messageVO.getFromId())).filter(QueryBuilders.termQuery("toId",messageVO.getToId()));
        mustTo.filter(QueryBuilders.termQuery("fromId",messageVO.getToId())).filter(QueryBuilders.termQuery("toId",messageVO.getFromId()));
        shouldQuery.should(mustFrom).should(mustTo);
        boolQuery.must(shouldQuery);
        return boolQuery;
    }
    private List<Message> handleResponse(SearchResponse response) {
        SearchHit[] hits = response.getHits().getHits();
        List<Message> messageList = new ArrayList<>();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Message message = JSON.parseObject(json, Message.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(!CollectionUtils.isEmpty(highlightFields)){
                HighlightField highlightField = highlightFields.get("messageText");
                if(highlightField!=null){
                    String name = highlightField.getFragments()[0].string();
                    message.setMessageText(name);
                }
            }
            messageList.add(message);
        }
        return messageList;
    }


}




