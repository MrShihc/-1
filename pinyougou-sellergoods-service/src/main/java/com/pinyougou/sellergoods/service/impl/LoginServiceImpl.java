package com.pinyougou.sellergoods.service.impl;

import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.sellergoods.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 运营商登录业务逻辑层接口实现类
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;


}
