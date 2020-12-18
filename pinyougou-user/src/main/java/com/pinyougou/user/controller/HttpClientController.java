package com.pinyougou.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * httpClient测试
 */
@RestController
@RequestMapping("/httpClient")
public class HttpClientController {

    /**
     * GET无参
     *
     * 测试数据
     */
    @RequestMapping("/doGetControllerOne")
    public void doGetControllerOne(){
        System.out.println("get无参无返回值!");
    }

}
