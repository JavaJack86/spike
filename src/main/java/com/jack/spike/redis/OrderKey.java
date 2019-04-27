package com.jack.spike.redis;


/**
 * @Author Jack
 * @Date 2019/4/24 17:55
 */
public class OrderKey extends BasePrefix {

    private OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSpikeOrderByUidGid = new OrderKey("spikeOrder");
}
