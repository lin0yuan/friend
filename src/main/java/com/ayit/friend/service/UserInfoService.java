package com.ayit.friend.service;

import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86138
* @description 针对表【user_info】的数据库操作Service
* @createDate 2023-01-19 00:37:10
*/
public interface UserInfoService extends IService<UserInfo> {

    void update(UserInfo userInfo);
}
