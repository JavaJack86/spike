package com.jack.spike.rabbitmq;

import com.jack.spike.model.User;

/**
 * @Author Jack
 * @Date 2019/4/28 10:48
 */
public class SpikeMessage {

    private User user;
    private long goodsId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
