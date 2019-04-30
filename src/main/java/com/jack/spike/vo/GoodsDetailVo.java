package com.jack.spike.vo;

import com.jack.spike.model.User;
/**
 * @Author Jack
 * @Date 2019/4/25 13:03
 */
public class GoodsDetailVo {
    private int spikeStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;

    public int getSpikeStatus() {
        return spikeStatus;
    }

    public void setSpikeStatus(int spikeStatus) {
        this.spikeStatus = spikeStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
