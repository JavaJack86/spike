package com.jack.spike.rabbitmq;

import com.jack.spike.model.SpikeOrder;
import com.jack.spike.model.User;
import com.jack.spike.redis.RedisService;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.OrderService;
import com.jack.spike.service.SpikeService;
import com.jack.spike.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Author Jack
 * @Date 2019/4/27 17:23
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private SpikeService spikeService;


    @RabbitListener(queues = MQConfig.SPIKE_QUEUE)
    public void receiveSpikeMessage(String message) {
        log.info("receiveSpikeMessage:" + message);
        SpikeMessage spikeMessage = RedisService.stringToBean(message, SpikeMessage.class);
        User user = spikeMessage.getUser();
        long goodsId = spikeMessage.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        SpikeOrder isExist = orderService.getSpikeOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (isExist != null) {
            return;
        }
        spikeService.setSpikeOrderAndOrder(user, goods);
    }

}
