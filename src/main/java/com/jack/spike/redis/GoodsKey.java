package com.jack.spike.redis;

/**
 * @Author Jack
 * @Date 2019/4/26 16:56
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey goodsList = new GoodsKey(60, "gl");
    public static GoodsKey goodsDetail = new GoodsKey(60, "gd");
}
