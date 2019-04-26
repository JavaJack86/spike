package com.jack.spike.service;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.User;
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

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo setSpikeOrderAndOrder(User user, GoodsVo goods) {
        goodsService.reduceStock(goods);
        return orderService.createOrder(user, goods);
    }

}
