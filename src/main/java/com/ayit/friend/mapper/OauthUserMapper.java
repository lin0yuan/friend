package com.ayit.friend.mapper;

import com.ayit.friend.pojo.OauthUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86138
* @description 针对表【oauth_user(第三方授权信息)】的数据库操作Mapper
* @createDate 2023-01-14 23:57:38
* @Entity com.ayit.friend.pojo.OauthUser
*/
@Mapper
public interface OauthUserMapper extends BaseMapper<OauthUser> {

}




