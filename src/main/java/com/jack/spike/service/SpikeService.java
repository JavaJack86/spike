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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

    public BufferedImage createVerityCode(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SpikeOrderKey.getSpikeVerifyCode, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    private static char[] ops = new char[]{'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;

    }

    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
       Integer result = redisService.get(SpikeOrderKey.getSpikeVerifyCode,user.getId()+","+goodsId,Integer.class);
        if (result == null || result - verifyCode != 0){
            return false;
        }
        redisService.delete(SpikeOrderKey.getSpikeVerifyCode,user.getId()+","+goodsId);
        return true;
    }
}
