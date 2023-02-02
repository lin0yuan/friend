package com.ayit.friend.handler;

import com.alibaba.fastjson.JSON;
import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.OauthUser;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.OAuth2UserDTO;
import com.ayit.friend.service.OauthUserService;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.UserUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Configuration
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private OauthUserService oauthUserService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String message = null;
        OAuth2UserDTO oAuth2UserDTO = (OAuth2UserDTO) authentication.getPrincipal();
        String email = oAuth2UserDTO.getEmail();
        OauthUser oauthUser = oauthUserService.getOne(oAuth2UserDTO.getSource(),oAuth2UserDTO.getId());
        RespBean<Object> respBean = null;
        if(oauthUser==null){
            oauthUser = new OauthUser();
            oauthUser.setSource(oAuth2UserDTO.getSource());
            oauthUser.setUuid(oAuth2UserDTO.getId());
            if(email==null){
                respBean = RespBean.warn(oAuth2UserDTO,1001);
            }else{
                User userByEmail = userService.queryEmail(email);
                if(userByEmail!=null){
                    oauthUser.setUserId(userByEmail.getId());
                    respBean = RespBean.success(userByEmail);
                }else {
                    respBean = RespBean.warn(oAuth2UserDTO, 1001);
                }

            }
            oauthUserService.save(oauthUser);
        }else{
            Long userId = oauthUser.getUserId();
            if(userId==null){
                if(email==null){
                    respBean = RespBean.warn(oAuth2UserDTO,1001);
                }else{
                    User userByEmail = userService.queryEmail(email);
                    if(userByEmail!=null){
                        oauthUser.setUserId(userByEmail.getId());
                        oauthUserService.addUserId(oauthUser);
                        respBean = RespBean.success(userByEmail);
                    }else {
                        respBean = RespBean.warn(oAuth2UserDTO, 1001);
                    }
                }
            }else{
                User user = userService.getUserById(userId);
                respBean = RespBean.success(user);
                userService.updateState(userId,0);
            }
        }
        message = JSON.toJSONString(respBean);
        out.write(message);
        out.flush();
        out.close();
    }
}
