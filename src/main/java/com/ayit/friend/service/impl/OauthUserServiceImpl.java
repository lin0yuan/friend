package com.ayit.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ayit.friend.pojo.OauthUser;
import com.ayit.friend.service.OauthUserService;
import com.ayit.friend.mapper.OauthUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 86138
* @description 针对表【oauth_user(第三方授权信息)】的数据库操作Service实现
* @createDate 2023-01-14 23:57:38
*/
@Service
public class OauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser>
    implements OauthUserService{
    @Override
    public OauthUser getOne(String source, String uuid) {
        LambdaQueryWrapper<OauthUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(OauthUser::getSource,source)
                .eq(OauthUser::getUuid,uuid);
        return  this.getOne(queryWrapper);
    }

    @Override
    public void addUserId(OauthUser oauthUser) {
        this.update()
                .eq("source",oauthUser.getSource())
                .eq("uuid",oauthUser.getUuid())
                .update(oauthUser);
    }
}




