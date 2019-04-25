package com.jack.spike.controller;

import com.jack.spike.model.User;
import com.jack.spike.service.GoodsService;
import com.jack.spike.service.UserService;
import com.jack.spike.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @Author Jack
 * @Date 2019/4/25 15:19
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsVoList);
        return "goodsList";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable("goodsId") long id) {
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus ;
        int remainSeconds ;
        //秒杀还没开始，倒计时
        if(now < startAt ) {
            spikeStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
            //秒杀已经结束
        }else  if(now > endAt){
            spikeStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("spikeStatus", spikeStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goodsDetail";
    }

}
