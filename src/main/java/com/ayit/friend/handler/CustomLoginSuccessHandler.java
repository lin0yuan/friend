package com.ayit.friend.handler;

import com.alibaba.fastjson.JSON;
import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.UserDTO;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        userService.updateState(userDTO.getId(),0);
        userDTO.setUserStatus(0);
        RespBean<User> ok = RespBean.success(userDTO);
        String s = JSON.toJSONString(ok);
        out.write(s);
        out.flush();
        out.close();
    }


}
