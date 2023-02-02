package com.ayit.friend.service;

import com.ayit.friend.pojo.Follow;
import com.ayit.friend.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86138
* @description 针对表【follow(关注与粉丝)】的数据库操作Service
* @createDate 2022-12-04 15:45:54
*/
public interface FollowService extends IService<Follow> {
    Page<User> listSubscriber(Long id,Integer page,Integer size);

    Page<User> listTutor(Long id,Integer page,Integer size);

    void removeFollow(Follow follow);

    Page<User> pageMutually(Long userId, Integer page,Integer size);

    void addTutor(Long tutor);

    Integer getRelation(Long toUserId);
}
