package com.personal.utils;
import com.personal.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 描述：security认证的工具类，暂时未用到
 * @author
 * @date 2020/06/24 15:43
 **/
public class UserHolder {
    public static User getUserDetail(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }
    public static String getUserCode(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getCode();
    }
    public static int getUserId(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getId();
    }
}
