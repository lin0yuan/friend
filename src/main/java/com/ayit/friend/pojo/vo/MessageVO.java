package com.ayit.friend.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel
public class MessageVO {

//    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty(value = "发送者id",required = true)
    private Long fromId;
//    @JsonDeserialize(using = StringToLongDeserializer.class)
    @ApiModelProperty(value = "接收者id",required = true)
    private Long toId;
    @ApiModelProperty(value = "搜索",required = true)
    private String info;
    @ApiModelProperty(value = "起始时间",required = true)
    private LocalDate startDate;


    @ApiModelProperty(value = "截止时间",required = true)
    private LocalDate endDate;
    @ApiModelProperty(value = "信息类型:0=文本,1=图片,2=文件,3=音频",required = true)
    private Integer messageType;
    @ApiModelProperty(value = "当前分页",required = true)
    private Integer page;
    @ApiModelProperty(value = "每页大小",required = true)
    private Integer size;
}
