package com.ayit.friend.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.ayit.friend.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86138
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-01-19 00:34:44
* @Entity com.ayit.friend.temporary.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    int updateUserStatusById(@Param("userStatus") Integer userStatus, @Param("id") Long id);

    User selectByEmailAddress(@Param("emailAddress") String emailAddress);
}




