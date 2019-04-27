package com.jack.spike.controller;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.result.CodeMsg;
import com.jack.spike.result.Result;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.OrderService;
import com.jack.spike.service.SpikeService;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Jack
 * @Date 2019/4/26 9:46
 */
@RestController
@RequestMapping("/spike")
public class SpikeController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SpikeService spikeService;


    @PostMapping("/do_spike")
    public Result<OrderInfo> doSpike(Model model, User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errMsg", CodeMsg.SPIKE_OVER.getMsg());
            return Result.error(CodeMsg.SPIKE_OVER);
        }
        SpikeOrder isExist = orderService.getSpikeOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (isExist != null) {
            model.addAttribute("errMsg", CodeMsg.SPIKE_RAPEATE.getMsg());
            return Result.error(CodeMsg.SPIKE_RAPEATE);
        }
        OrderInfo orderInfo = spikeService.setSpikeOrderAndOrder(user, goods);
        return Result.success(orderInfo);
    }

}
