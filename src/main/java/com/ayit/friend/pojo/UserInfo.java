package com.ayit.friend.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
@ApiModel
public class UserInfo implements Serializable {
    /**
     * 用户id
     */
    @TableId
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名",required = true)
    private String givenName;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码",required = true)
    private String phoneNumber;

    /**
     * 自我介绍
     */
    @ApiModelProperty(value = "自我介绍")
    private String briefSelfIntro;

    /**
     * 天赋才能
     */
    @ApiModelProperty(value = "天赋才能")
    private String faculty;

    /**
     * 擅长
     */
    @ApiModelProperty(value = "擅长")
    private String goodAt;

    /**
     * 兴趣爱好
     */
    @ApiModelProperty(value = "兴趣爱好")
    private String hobby;

    /**
     * 自我简介
     */

    @ApiModelProperty(value = "自我简介")
    private String selfIntro;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @JSONField(serialize = false)
    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public void setCreateTime(Date createTime){
        Instant instant = createTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        this.createTime = localDateTime;
    }
    public void setUpdateTime(Date updateTime){
        Instant instant = updateTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        this.updateTime = localDateTime;
    }
    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @JsonIgnore
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGivenName() == null ? other.getGivenName() == null : this.getGivenName().equals(other.getGivenName()))
            && (this.getPhoneNumber() == null ? other.getPhoneNumber() == null : this.getPhoneNumber().equals(other.getPhoneNumber()))
            && (this.getBriefSelfIntro() == null ? other.getBriefSelfIntro() == null : this.getBriefSelfIntro().equals(other.getBriefSelfIntro()))
            && (this.getFaculty() == null ? other.getFaculty() == null : this.getFaculty().equals(other.getFaculty()))
            && (this.getGoodAt() == null ? other.getGoodAt() == null : this.getGoodAt().equals(other.getGoodAt()))
            && (this.getHobby() == null ? other.getHobby() == null : this.getHobby().equals(other.getHobby()))
            && (this.getSelfIntro() == null ? other.getSelfIntro() == null : this.getSelfIntro().equals(other.getSelfIntro()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getAge() == null ? other.getAge() == null : this.getAge().equals(other.getAge()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGivenName() == null) ? 0 : getGivenName().hashCode());
        result = prime * result + ((getPhoneNumber() == null) ? 0 : getPhoneNumber().hashCode());
        result = prime * result + ((getBriefSelfIntro() == null) ? 0 : getBriefSelfIntro().hashCode());
        result = prime * result + ((getFaculty() == null) ? 0 : getFaculty().hashCode());
        result = prime * result + ((getGoodAt() == null) ? 0 : getGoodAt().hashCode());
        result = prime * result + ((getHobby() == null) ? 0 : getHobby().hashCode());
        result = prime * result + ((getSelfIntro() == null) ? 0 : getSelfIntro().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAge() == null) ? 0 : getAge().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(id);
        sb.append(", givenName=").append(givenName);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", briefSelfIntro=").append(briefSelfIntro);
        sb.append(", faculty=").append(faculty);
        sb.append(", goodAt=").append(goodAt);
        sb.append(", hobby=").append(hobby);
        sb.append(", selfIntro=").append(selfIntro);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", age=").append(age);
        sb.append("]");
        return sb.toString();
    }
}