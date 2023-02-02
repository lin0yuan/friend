package com.ayit.friend.controller;

import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.OAuth2UserDTO;
import com.ayit.friend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "授权控制器")
@RestController
@RequestMapping
public class OAuth2Controller {

    @Autowired
    private UserService userService;

    @PostMapping("/bind")
    @ApiOperation(value = "注册授权用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="oAuth2UserDTO",value = "第三方授权信息",required = true,dataTypeClass = OAuth2UserDTO.class),
            @ApiImplicitParam(name="code",value = "验证码",required = true,dataTypeClass = String.class)
    })
    public RespBean<User> oAuth2Bind(@RequestBody OAuth2UserDTO oAuth2UserDTO,@RequestParam String code){
        User user = userService.bindUser(oAuth2UserDTO,code);
        RespBean<User> success = RespBean.success(user);
        String password = user.getPassword();
        if(password!=null){
            success.add("password",password);
        }
        return success;
    }

    @GetMapping("/oauth2/authorization/gitee")
    @ApiOperation(value = "gitee授权")
    public RespBean<OAuth2UserDTO> oAuth2Login(){
        return null;
    }

}
