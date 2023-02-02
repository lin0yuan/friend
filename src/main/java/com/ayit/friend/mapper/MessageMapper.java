package com.ayit.friend.mapper;

import com.ayit.friend.pojo.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86138
* @description 针对表【message(信息数据表)】的数据库操作Mapper
* @createDate 2023-01-16 19:34:38
* @Entity com.ayit.friend.pojo.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




