package com.ayit.friend.handler;

import com.ayit.friend.common.CustomException;
import com.ayit.friend.common.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.ServerEndpoint;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class,Service.class,ServerEndpoint.class},basePackageClasses = UserDetailsService.class)
public class GlobalExceptionHandler{

    @ExceptionHandler(CustomException.class)
    public RespBean<String> exceptionHandler(CustomException ex){

        log.error(ex.getMessage());
        if(ex.getMessage().contains("格式错误")){
            return RespBean.warn(ex.getMessage(),1002);
        }
        return RespBean.error(ex.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RespBean<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return RespBean.error(msg);
        }
        return RespBean.error("未知错误");
    }

    @ExceptionHandler(ParseException.class)
    public RespBean<String> exceptionHandler(ParseException ex){
        return RespBean.error("格式转换异常");
    }
}
