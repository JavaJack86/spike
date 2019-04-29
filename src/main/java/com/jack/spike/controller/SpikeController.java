package com.jack.spike.controller;

import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.rabbitmq.MQSender;
import com.jack.spike.rabbitmq.SpikeMessage;
import com.jack.spike.redis.GoodsKey;
import com.jack.spike.redis.RedisService;
import com.jack.spike.redis.SpikeOrderKey;
import com.jack.spike.result.CodeMsg;
import com.jack.spike.result.Result;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.OrderService;
import com.jack.spike.service.SpikeService;
import com.jack.spike.util.MD5Util;
import com.jack.spike.util.UUIDUtil;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jack
 * @Date 2019/4/26 9:46
 */
@RestController
@RequestMapping("/spike")
public class SpikeController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpikeService spikeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化后调用
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.spikeGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    /**
     * @return orderId ; 成功
     * -1 : 秒杀失败
     * 0 ： 排队中
     */
    @GetMapping("/result")
    public Result<Long> result(User user, @RequestParam("goodsId") long goodsId) {
        long result = spikeService.getSpikeResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @PostMapping("/{path}/do_spike")
    public Result<Integer> doSpike(Model model, User user, @RequestParam("goodsId") long goodsId ,@PathVariable String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = spikeService.checkPath(path,user.getId(),goodsId);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        boolean ret = localOverMap.get(goodsId);
        if (ret) {
            return Result.error(CodeMsg.SPIKE_OVER);
        }
        long stockCount = redisService.decr(GoodsKey.spikeGoodsStock, "" + goodsId);
        if (stockCount < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SPIKE_OVER);
        }
        SpikeOrder isExist = orderService.getSpikeOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (isExist != null) {
            return Result.error(CodeMsg.SPIKE_RAPEATE);
        }
        SpikeMessage spikeMessage = new SpikeMessage();
        spikeMessage.setGoodsId(goodsId);
        spikeMessage.setUser(user);
        mqSender.sendSpikeMessage(spikeMessage);
        //排队中
        return Result.success(0);
    }

    @GetMapping("/path")
    public Result<String> path(Model model, User user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
       String path =  spikeService.createSpikePath(user.getId(),goodsId);
        return Result.success(path);
    }

}
