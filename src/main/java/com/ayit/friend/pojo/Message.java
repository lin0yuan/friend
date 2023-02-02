package com.ayit.friend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息数据表
 * @TableName message
 */
@TableName(value ="message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {
    /**
     * 信息id
     */
    @ApiModelProperty("主键")
    @TableId
    private Long id;

    /**
     * 发送者id
     */
    @ApiModelProperty(value = "发送者id",required = true)
    private Long fromId;

    /**
     * 接受者id
     */
    @ApiModelProperty(value = "接收者id",required = true)
    private Long toId;

    /**
     * 信息类型
     */
    @ApiModelProperty(value = "信息类型:0=文本,1=图片,2=文件,3=音频",required = false)
    private Integer messageType;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容",required = true)
    private String messageText;
    /**
     * 文件信息
     */
    @ApiModelProperty(value = "文件或者图片",required = true)
    private String file;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间",required = false)
    private LocalDateTime sentDatetime;

    public void setSentDatetime(Date sentDatetime){
        Instant instant = sentDatetime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        this.sentDatetime = localDateTime;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        Message other = (Message) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFromId() == null ? other.getFromId() == null : this.getFromId().equals(other.getFromId()))
                && (this.getToId() == null ? other.getToId() == null : this.getToId().equals(other.getToId()))
                && (this.getMessageText() == null ? other.getMessageText() == null : this.getMessageText().equals(other.getMessageText()))
                && (this.getSentDatetime() == null ? other.getSentDatetime() == null : this.getSentDatetime().equals(other.getSentDatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFromId() == null) ? 0 : getFromId().hashCode());
        result = prime * result + ((getToId() == null) ? 0 : getToId().hashCode());
        result = prime * result + ((getMessageText() == null) ? 0 : getMessageText().hashCode());
        result = prime * result + ((getSentDatetime() == null) ? 0 : getSentDatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", messageType=" + messageType +
                ", messageText='" + messageText + '\'' +
                ", file='" + file + '\'' +
                ", sentDatetime=" + sentDatetime +
                '}'+"\n";
    }

}