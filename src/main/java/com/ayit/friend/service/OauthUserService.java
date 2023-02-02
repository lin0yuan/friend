package com.ayit.friend.service;

import com.ayit.friend.pojo.OauthUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86138
* @description 针对表【oauth_user(第三方授权信息)】的数据库操作Service
* @createDate 2023-01-14 23:57:38
*/
public interface OauthUserService extends IService<OauthUser> {
    OauthUser getOne(String source,String uuid);


    void addUserId(OauthUser oauthUser);
}
