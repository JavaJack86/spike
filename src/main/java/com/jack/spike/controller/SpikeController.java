package com.jack.spike.controller;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.User;
import com.jack.spike.result.CodeMsg;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.OrderService;
import com.jack.spike.service.SpikeService;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Jack
 * @Date 2019/4/26 9:46
 */
@Controller
@RequestMapping("/spike")
public class SpikeController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SpikeService spikeService;


    @RequestMapping("/do_spike")
    public String doSpike(Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errMsg", CodeMsg.SPIKE_OVER.getMsg());
            return "spikeFail";
        }
        OrderInfo isExist = orderService.getOrderInfoByUserIdAndGoodsId(user.getId(), goodsId);
        if (isExist != null) {
            model.addAttribute("errMsg", CodeMsg.SPIKE_REPEATE.getMsg());
            return "spikeFail";
        }
        OrderInfo orderInfo = spikeService.setSpikeOrderAndOrder(user, goods);
        if (orderInfo != null) {
            model.addAttribute("order", orderInfo);
            model.addAttribute("goods", goods);
            return "orderDetail";
        }
        return "goodsList";
    }

}
