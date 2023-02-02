package com.ayit.friend.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class UserVO {
    @ApiModelProperty("id表")
    private List<String> ids;
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("每页大小")
    private Integer size;
    @ApiModelProperty("性别")
    private Integer gender;
    @ApiModelProperty("关键字")
    private String info;

}
