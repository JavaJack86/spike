package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/24 17:54
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

    {

    }

}
