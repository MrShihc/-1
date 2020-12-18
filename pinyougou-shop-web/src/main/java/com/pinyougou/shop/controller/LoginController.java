package com.pinyougou.shop.controller;

import com.pinyougou.mapper.TbSellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @RequestMapping("/getLoginName")
    public Map getLoginName(){
        //获取登录名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Date lastLoginTime = tbSellerMapper.selectBySellerId(name);
        tbSellerMapper.updateLastLoginTimeBySellId(name,new Date());

        Map map = new HashMap();
        map.put("loginName",name);
        map.put("lastLoginTime",lastLoginTime);

        return map;
    }

}
