package com.ayit.friend.controller;

import com.ayit.friend.enumeration.MessageType;
import com.ayit.friend.properties.MailProperties;
import com.ayit.friend.common.RespBean;
import com.ayit.friend.pojo.User;
import com.ayit.friend.service.UserService;
import com.ayit.friend.utils.AliyunOssUtil;
import com.ayit.friend.utils.ValidateCodeUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.ayit.friend.utils.RedisConstants.LOGIN_CODE_KEY;
import static com.ayit.friend.utils.RedisConstants.LOGIN_CODE_TTL;

@Slf4j
@RestController
@RequestMapping("/register")
@Api(tags = "注册控制器")
public class RegisterController {

    @Autowired
    private UserService userService;


    @GetMapping("/checkEmail")
    @ApiOperation(value = "检查用户邮箱")
    @ApiImplicitParam(name = "emailAddress",value = "用户邮箱",required = true)
    public RespBean<Boolean> checkEmail(String emailAddress){
        User user = userService.queryEmail(emailAddress);
        if(user==null){
            return RespBean.success(true);
        }
        return RespBean.success(false);
    }


    @Autowired
    private AliyunOssUtil aliyunOssUtil;

    private final static Integer IMAGE = 1;

    @PostMapping("/ossFileUpload")
    @ApiOperation(value = "上传头像")
    @ApiImplicitParam(name = "file",value = "头像信息",required = true,dataTypeClass = MultipartFile.class)
    public RespBean<String> ossFileUpload(@RequestPart("file") MultipartFile file) throws IOException {
        String url = aliyunOssUtil.upload(file.getInputStream(),IMAGE,file.getOriginalFilename());
        return RespBean.success(url);
    }

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/sendCode")
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParam(name = "emailAddress", value = "用户的邮箱地址", required = true,dataTypeClass = String.class)
    public RespBean<Boolean> sendEmailCode(String emailAddress){
        String code = ValidateCodeUtil.generateValidateCode(6).toString();
//        SMSUtil smsUtil = new SMSUtil(emailAddress,code,mailProperties);
//        smsUtil.run();
        log.error(code);
        redisTemplate.opsForValue().set(LOGIN_CODE_KEY+emailAddress,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return RespBean.success(true);
    }

    @PostMapping("/user")
    @ApiOperation(value = "注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "用户信息",required = true,dataTypeClass = User.class),
            @ApiImplicitParam(name= "code",value = "验证码",required = true,dataTypeClass = HttpSession.class),
            @ApiImplicitParam(name= "password",value = "密码",required = true,dataTypeClass = HttpSession.class)
    })
    public RespBean<User> register(@RequestBody User user,@RequestParam String code,@RequestParam String password){
        user.setPassword(password);
        userService.saveUser(user, code);
        return RespBean.success(user);
    }


}
