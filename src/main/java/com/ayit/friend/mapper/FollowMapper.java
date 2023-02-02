package com.ayit.friend.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.ayit.friend.pojo.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.parameters.P;

/**
* @author 86138
* @description 针对表【follow(关注与粉丝)】的数据库操作Mapper
* @createDate 2022-12-04 15:45:54
* @Entity com.ayit.friend.pojo.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




