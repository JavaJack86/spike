package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/28 11:18
 */
public class SpikeOrderKey extends BasePrefix {


    private SpikeOrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpikeOrderKey goodsOver = new SpikeOrderKey(0, "go");
    public static SpikeOrderKey getSpikePath = new SpikeOrderKey(60, "sp");
    public static SpikeOrderKey getSpikeVerifyCode = new SpikeOrderKey(300, "svc");
}
