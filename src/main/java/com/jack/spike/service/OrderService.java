package com.jack.spike.service;

import com.jack.spike.dao.IOrderDao;
import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.redis.OrderKey;
import com.jack.spike.redis.RedisService;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author Jack
 * @Date 2019/4/26 9:54
 */
@Service
public class OrderService {


    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private RedisService redisService;

    public SpikeOrder getSpikeOrderByUserIdAndGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSpikeOrderByUidGid, "" + userId + "_" + goodsId, SpikeOrder.class);
    }

    public OrderInfo getOrderInfoByOrderId(long orderId) {
        return orderDao.getOrderInfoByOrderId(orderId);
    }


    @Transactional(rollbackFor = Exception.class)
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSpikePrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insertOrderInfo(orderInfo);
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setUserId(user.getId());
        spikeOrder.setOrderId(orderInfo.getId());
        orderDao.insertSpikeOrderInfo(spikeOrder);
        redisService.set(OrderKey.getSpikeOrderByUidGid, "" + user.getId() + "_" + goods.getId(), spikeOrder);
        return orderInfo;
    }
}
