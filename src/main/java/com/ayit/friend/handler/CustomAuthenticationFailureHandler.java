package com.ayit.friend.handler;

import com.ayit.friend.common.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        PrintWriter out = httpServletResponse.getWriter();

        RespBean error = RespBean.error("登录失败!");
        if(e instanceof LockedException){
            error.setErrorMsg("账户已锁定,请联系管理员!");
        }else if(e instanceof CredentialsExpiredException){
            error.setErrorMsg("密码已过期,请联系管理员!");
        }else if(e instanceof AccountExpiredException){
            error.setErrorMsg("账户已过期,请联系管理员!");
        }else if(e instanceof DisabledException){
            error.setErrorMsg("账户被禁用,请联系管理员");
        }else if(e instanceof BadCredentialsException){
            error.setErrorMsg("用户名或密码错误,请重新输入");
        }else if(e instanceof RememberMeAuthenticationException){
            error.setErrorMsg("记住密码错误,请重新登录");
        }else if(e instanceof OAuth2AuthenticationException){
            error.setErrorMsg("授权登录错误,请重新授权");
        }
        String s = new ObjectMapper().writeValueAsString(error);
        out.write(s);
        out.flush();
        out.close();
    }
}
