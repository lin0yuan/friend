package com.ayit.friend.properties;

import com.ayit.friend.enumeration.MessageType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "aliyun.oss.module")
public class ModuleProperties {
    private String headportrait;

    private String image;

    private String file;

    private String other;

    private String audio;

    public String getModule(Integer type){
        if(type==2){
            return file;
        }else if(type==1){
            return image;
        }else if(type==3){
            return audio;
        }
        return other;
    }
}
