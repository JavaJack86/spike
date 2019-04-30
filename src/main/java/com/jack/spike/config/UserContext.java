package com.jack.spike.config;

import com.jack.spike.model.User;

/**
 * @Author Jack
 * @Date 2019/4/30 15:56
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

}
