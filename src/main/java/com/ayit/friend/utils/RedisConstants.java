package com.ayit.friend.utils;

public class RedisConstants {

    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final String LIST_MESSAGE_KEY = "list:message:";

    public static final String LIST_TUTOR_KEY = "list:tutor:";
    public static final String LIST_SUBSCRIBER_KEY = "list:subscriber:";
    public static final String CACHE_NULL_VALUE = "";
    public static final Long CACHE_NULL_TTL = 2L;

    public static final String LIST_SHOP_KEY = "list:friend:";
}
