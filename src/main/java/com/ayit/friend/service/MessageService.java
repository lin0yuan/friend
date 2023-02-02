package com.ayit.friend.service;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.Message;
import com.ayit.friend.pojo.vo.MessageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
* @author 86138
* @description 针对表【message(信息数据表)】的数据库操作Service
* @createDate 2023-01-16 19:34:38
*/
public interface MessageService extends IService<Message> {

    Page<Message> search(MessageVO messageVO);

    Message removeById(Long userId, Long messageId);

    Page<Message> searchAdmin(MessageVO messageVO);

}

