package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/24 17:51
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();

}
