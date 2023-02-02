package com.ayit.friend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注与粉丝
 * @TableName follow
 */
@TableName(value ="follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Follow extends  Object implements Serializable{
    /**
     * 粉丝id
     */
    private Long subscriberId;

    /**
     * 被关注者
     */
    private Long tutorId;

    /**
     * 添加时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    public Follow(Long subscriberId,Long tutorId){
        this.subscriberId = subscriberId;
        this.tutorId = tutorId;
    }
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
        Follow other = (Follow) that;
        return (this.getSubscriberId() == null ? other.getSubscriberId() == null : this.getSubscriberId().equals(other.getSubscriberId()))
            && (this.getTutorId() == null ? other.getTutorId() == null : this.getTutorId().equals(other.getTutorId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSubscriberId() == null) ? 0 : getSubscriberId().hashCode());
        result = prime * result + ((getTutorId() == null) ? 0 : getTutorId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", subscriberId=").append(subscriberId);
        sb.append(", tutorId=").append(tutorId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}