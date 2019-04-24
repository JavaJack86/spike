package com.jack.spike.controller;

import com.jack.spike.model.User;
import com.jack.spike.redis.RedisService;
import com.jack.spike.redis.UserKey;
import com.jack.spike.result.Result;
import com.jack.spike.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Jack
 * @Date 2019/4/24 15:39
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Jack");
        return "hello";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<User> get() {
        User user = userService.getUserById(1);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "1", User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User(1, "Rose");
        boolean result = redisService.set(UserKey.getById, "1", user);
        return Result.success(result);
    }
}
