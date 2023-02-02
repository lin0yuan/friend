package com.ayit.friend.service.impl;

import com.alibaba.cloud.context.utils.StringUtils;
import com.ayit.friend.common.CustomException;
import com.ayit.friend.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ayit.friend.pojo.UserInfo;
import com.ayit.friend.service.UserInfoService;
import com.ayit.friend.mapper.UserInfoMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
* @author 86138
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2023-01-19 00:37:10
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @SneakyThrows
    @Override
    public void update(UserInfo userInfo) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId()!=userInfo.getId()){
            throw new CustomException("不是本人用户");
        }
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<UserInfo>()
                .eq(UserInfo::getId,user.getId())
                .set(userInfo.getAge()!=null,UserInfo::getAge,userInfo.getAge())
                .set(userInfo.getBriefSelfIntro()!=null,UserInfo::getBriefSelfIntro,userInfo.getBriefSelfIntro())
                .set(userInfo.getFaculty()!=null,UserInfo::getFaculty,userInfo.getFaculty())
                .set(userInfo.getGoodAt()!=null,UserInfo::getGoodAt,userInfo.getGoodAt())
                .set(userInfo.getHobby()!=null,UserInfo::getHobby,userInfo.getHobby())
                .set(userInfo.getSelfIntro()!=null,UserInfo::getSelfIntro,userInfo.getSelfIntro())
                .set(userInfo.getPhoneNumber()!=null,UserInfo::getPhoneNumber,userInfo.getPhoneNumber())
                .set(userInfo.getGivenName()!=null,UserInfo::getGivenName,userInfo.getGivenName());
        this.update(updateWrapper);
        rabbitTemplate.convertAndSend("ayit.user","update",user.getId());
    }
}




