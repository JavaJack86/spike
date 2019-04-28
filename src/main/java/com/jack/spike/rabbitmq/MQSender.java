package com.jack.spike.rabbitmq;

import com.jack.spike.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Jack
 * @Date 2019/4/27 17:22
 */
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    public void sendSpikeMessage(SpikeMessage spikeMessage) {
        String msg = RedisService.beanToString(spikeMessage);
        log.info("sendSpikeMessage:" + msg);
        amqpTemplate.convertAndSend(MQConfig.SPIKE_QUEUE, msg);
    }
}
