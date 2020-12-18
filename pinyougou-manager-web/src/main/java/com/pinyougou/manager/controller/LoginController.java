package com.pinyougou.manager.controller;

import com.pinyougou.sellergoods.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 运营商登录控制层
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 获取登录人名称
     */
    @RequestMapping("/getName")
    public Map getName(){
        //获取登录名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Date date = new Date();
        String lastLoginTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        lastLoginTime = sdf.format(date);
        System.out.println("\n\n\n\n"+lastLoginTime+"\n\n\n\n");

        Map map = new HashMap();
        map.put("loginName",name);
        map.put("lastLoginTime",lastLoginTime);
        return map;
    }

}
