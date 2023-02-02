package com.ayit.friend.utils;

import com.alibaba.cloud.context.utils.StringUtils;
import com.ayit.friend.pojo.User;
import com.ayit.friend.pojo.dto.UserDTO;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {

    private static String[] fields = new String[6];

    public static String[] getUserFields(){
        return fields;
    }

    static {
        fields[0] = "id";
        fields[1] = "emailAddress";
        fields[2] = "surname";
        fields[3] = "avatar";
        fields[4] = "userStatus";
        fields[5] = "gender";
    }
    public static boolean isUsername(String username){
        if(StringUtils.isEmpty(username)){
            return false;
        }
        String userPattern = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        if(!username.matches(userPattern)){
            return false;
        }
        return true;
    }

    public static boolean isEmail(String email){
        if(StringUtils.isEmpty(email)){
            return false;
        }
        String emailPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if(!email.matches(emailPattern)){
            return false;
        }
        return true;
    }

    public static boolean isPhone(String phone){
        if(StringUtils.isEmpty(phone)){
            return false;
        }
        String phonePattern = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        if(!phone.matches(phonePattern)){
            return false;
        }
        return true;
    }

    public static boolean isPassword(String password){
        if(StringUtils.isEmpty(password)){
            return false;
        }
        String passwordPattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,15}$";
        if(!password.matches(passwordPattern)){
            return false;
        }
        return true;
    }

    private static final char[] CHARR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?-+=_"
            .toCharArray();
    private static final String PASSWORD_REGEX = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,16}$";
    private static final String NO_CHINESE_REGEX = "^[^\\u4e00-\\u9fa5]+$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern NO_CHINESE_PATTERN = Pattern.compile(NO_CHINESE_REGEX);

    public static String generatePassword(int length) {
        length = length < 8 ? 8 : length;
        length = length > 16 ? 16 : length;
        String result = getRandomPassword(length);
        Matcher m = PASSWORD_PATTERN.matcher(result);
        Matcher m1 = NO_CHINESE_PATTERN.matcher(result);
        if (m.matches() && m1.matches()) {
            return result;
        }
        return generatePassword(length);
    }
    private static String getRandomPassword(int length) {
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int x = 0; x < length; ++x) {
            sb.append(CHARR[r.nextInt(CHARR.length)]);
        }
        return sb.toString();
    }

}
