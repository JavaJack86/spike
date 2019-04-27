package com.jack.spike.controller;

import com.jack.spike.model.User;
import com.jack.spike.redis.GoodsKey;
import com.jack.spike.redis.RedisService;
import com.jack.spike.result.Result;
import com.jack.spike.service.GoodsService;
import com.jack.spike.vo.GoodsDetailVo;
import com.jack.spike.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Author Jack
 * @Date 2019/4/25 15:19
 */

@RestController
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list", produces = "text/html")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        String html = redisService.get(GoodsKey.goodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsVoList);
        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", springWebContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    public String toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long id) {
        model.addAttribute("user", user);
        String html = redisService.get(GoodsKey.goodsDetail, "" + id, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus;
        int remainSeconds;
        //秒杀还没开始，倒计时
        if (now < startAt) {
            spikeStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
            //秒杀已经结束
        } else if (now > endAt) {
            spikeStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("spikeStatus", spikeStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext springWebContext = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", springWebContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodsDetail, "" + id, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    public Result<GoodsDetailVo> toDetail(User user, @PathVariable("goodsId") long id) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus;
        int remainSeconds;
        //秒杀还没开始，倒计时
        if (now < startAt) {
            spikeStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
            //秒杀已经结束
        } else if (now > endAt) {
            spikeStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setRemainSeconds(remainSeconds);
        vo.setSpikeStatus(spikeStatus);
        vo.setUser(user);
        return Result.success(vo);
    }

}
