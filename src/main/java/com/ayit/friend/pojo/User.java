package com.ayit.friend.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@ApiModel
@NoArgsConstructor
public class  User implements Serializable{
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱地址",required = true)
    private String emailAddress;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",required = true)
    private String surname;


    /**
     * 用户角色：0=普通用户,1=管理员
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "用户角色：0=普通用户,1=管理员")
    private Integer userRole;

    /**
     * 密码
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "密码",required = true)
    private String password;

    /**
     * 头像信息
     */
    @ApiModelProperty(value = "头像信息",required = true)
    private String avatar;

    /**
     * 性别：0=男,1=女
     */
    @ApiModelProperty(value = "性别：0=男,1=女")
    private Integer gender;

    /**
     * 用户状态:0=在线,1=离线,2=禁用
     */
    @ApiModelProperty(value = "用户状态:0=在线,1=离线,2=禁用")
    private Integer userStatus;

    /**
     * 创建时间
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public void setCreateTime(Date createTime){
        LocalDateTime localDateTime = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.createTime = localDateTime;
    }
    public void setUpdateTime(Date updateTime){
        LocalDateTime localDateTime = updateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.updateTime = localDateTime;
    }
    /**
     * 修改时间
//     */
    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", surname='" + surname + '\'' +
                ", userRole=" + userRole +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender=" + gender +
                ", userStatus=" + userStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}'+'\n';
    }
}