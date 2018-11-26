package com.codingcow.knapsack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Austin Zhang
 * @Date: 18-8-8 下午10:13
 */
@CrossOrigin
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
