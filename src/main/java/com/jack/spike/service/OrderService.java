package com.jack.spike.service;

import com.jack.spike.dao.IOrderDao;
import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
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

    public OrderInfo getOrderInfoByUserIdAndGoodsId(long userId, long goodsId) {
        return orderDao.getOrderInfoByUserIdAndGoodsId(userId, goodsId);
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
        long orderId = orderDao.insertOrderInfo(orderInfo);
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setGoodsId(goods.getId());
        spikeOrder.setUserId(user.getId());
        spikeOrder.setOrderId(orderId);
        orderDao.insertSpikeOrderInfo(spikeOrder);
        return orderInfo;
    }
}
