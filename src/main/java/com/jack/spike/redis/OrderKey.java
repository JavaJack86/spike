package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/24 17:55
 */
public class OrderKey extends BasePrefix {

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
