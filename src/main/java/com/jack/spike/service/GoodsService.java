package com.jack.spike.service;

import com.jack.spike.dao.IGoodsDao;
import com.jack.spike.model.Goods;
import com.jack.spike.model.SpikeGoods;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Jack
 * @Date 2019/4/25 17:07
 */
@Service
public class GoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList() {
        return goodsDao.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long id) {
        return goodsDao.getGoodsVoByGoodsId(id);
    }

    public int reduceStock(GoodsVo goods) {
        SpikeGoods spikeGoods = new SpikeGoods();
        spikeGoods.setGoodsId(goods.getId());
        spikeGoods.setStockCount(goods.getStockCount() - 1);
        return goodsDao.reduceStock(spikeGoods);
    }
}
