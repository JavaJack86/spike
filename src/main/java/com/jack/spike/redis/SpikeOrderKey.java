package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/28 11:18
 */
public class SpikeOrderKey extends BasePrefix {

    private SpikeOrderKey(String prefix) {
        super(prefix);
    }

    public static SpikeOrderKey goodsOver = new SpikeOrderKey("go");
}
