package com.ayit.friend.mapper;

import com.ayit.friend.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86138
* @description 针对表【user_info】的数据库操作Mapper
* @createDate 2023-01-19 00:37:10
* @Entity com.ayit.friend.pojo.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




