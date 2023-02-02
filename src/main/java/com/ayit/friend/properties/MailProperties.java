package com.ayit.friend.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "spring.mail")
public class MailProperties {

    private String port;

    private String username;

    private String nickname;

    private String password;

    private String host;

}
