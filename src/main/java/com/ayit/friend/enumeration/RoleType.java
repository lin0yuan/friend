package com.ayit.friend.enumeration;

public enum RoleType {
    ROLE_ADMIN("管理员"),
    ROLE_USER("普通用户"),
    ROLE_TUTOR("关注"),
    ROLE_SUBSCRIBER("粉丝");

    private final String name;

    private RoleType(String name){
        this.name = name;
    }
}
