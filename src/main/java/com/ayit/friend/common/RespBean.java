package com.ayit.friend.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel
public class RespBean<T> {

    @ApiModelProperty(value = "编码:200成功,405请求出错,1001信息未完善,1002格式不规范")
    private Integer code;

    @ApiModelProperty(value = "错误信息",allowEmptyValue = true)
    private String errorMsg;

    @ApiModelProperty(value = "数据",allowEmptyValue = true)
    private T data;

    @ApiModelProperty(value = "动态数据",allowEmptyValue = true)
    private Map map = new HashMap();

    public static <T> RespBean<T> success(T object){
        RespBean<T> respBean = new RespBean<>();
        respBean.data = object;
        respBean.code = 200;
        return respBean;
    }

    public static <T> RespBean<T> warn(T object,Integer code){
        RespBean<T> respBean = new RespBean<>();
        respBean.data = object;
        respBean.code = code;
        return respBean;
    }
    public static <T> RespBean<T> error(String msg){
        RespBean respBean = new RespBean();
        respBean.errorMsg = msg;
        respBean.code = 405;
        return respBean;
    }

    public RespBean<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
