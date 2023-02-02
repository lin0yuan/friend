package com.ayit.friend.service;

import com.ayit.friend.enumeration.RoleType;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.OAuth2UserDTO;
import com.ayit.friend.pojo.vo.UserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

/**
* @author 86138
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-11-30 15:30:50
*/
public interface UserService extends IService<User> {
    void saveUser(User user,String code);

    User queryEmail(String email);

    void updateState(Long id,Integer state);

    User getUserById(Long userId);

    User bindUser(OAuth2UserDTO oAuth2UserDTO, String code);

    void updatePassword(Long id, String password);

    Page<User> pageUser(UserVO userVO);

    boolean checkPassword(String password);

    void updateGender(Integer gender);
}
