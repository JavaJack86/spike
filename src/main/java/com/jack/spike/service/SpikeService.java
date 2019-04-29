package com.jack.spike.service;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.redis.RedisService;
import com.jack.spike.redis.SpikeOrderKey;
import com.jack.spike.util.MD5Util;
import com.jack.spike.util.UUIDUtil;
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


    public boolean checkPath(String path, Long userId, Long goodsId) {
        if (userId == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(SpikeOrderKey.getSpikePath, "" + userId + "_" + goodsId, String.class);
        return pathOld.equals(path);
    }

    public String createSpikePath(Long userId, Long goodsId) {
        String strUUID = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SpikeOrderKey.getSpikePath, "" + userId + "_" + goodsId, strUUID);
        return strUUID;
    }
}
