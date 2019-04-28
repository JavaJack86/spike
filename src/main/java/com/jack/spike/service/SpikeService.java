package com.jack.spike.service;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.redis.RedisService;
import com.jack.spike.redis.SpikeOrderKey;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Jack
 * @Date 2019/4/26 10:01
 */
@Service
public class SpikeService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo setSpikeOrderAndOrder(User user, GoodsVo goods) {
        boolean ret = goodsService.reduceStock(goods);
        if (!ret) {
            setGoodsOver(goods.getId());
            return null;
        }
        return orderService.createOrder(user, goods);
    }


    public long getSpikeResult(long id, long goodsId) {
        SpikeOrder spikeOrder = orderService.getSpikeOrderByUserIdAndGoodsId(id, goodsId);
        if (spikeOrder != null) {
            return spikeOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            }
            return 0;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SpikeOrderKey.goodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SpikeOrderKey.goodsOver, "" + goodsId);
    }


}
