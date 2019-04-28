package com.jack.spike.controller;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.User;
import com.jack.spike.result.CodeMsg;
import com.jack.spike.result.Result;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.OrderService;
import com.jack.spike.vo.GoodsVo;
import com.jack.spike.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Jack
 * @Date 2019/4/27 11:17
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    public Result<OrderDetailVo> detail( User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderInfoByOrderId(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.Order_NOT_EXIST);
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(orderInfo);
        orderDetailVo.setGoods(goods);
        return Result.success(orderDetailVo);
    }
}
