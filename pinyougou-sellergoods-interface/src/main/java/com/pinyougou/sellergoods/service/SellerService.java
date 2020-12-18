package com.pinyougou.sellergoods.service;

import com.aliyuncs.exceptions.ClientException;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojogroup.Goods;
import entity.PageResult;
import entity.Result;

/**
 * 卖家业务逻辑层接口
 */
public interface SellerService {
    //验证登录名唯一性
    Result checkSellerId(TbSeller tbSeller);

    //获取验证码
    Result getCheckCode(TbSeller tbSeller) throws ClientException;

    //注册商家
    void saveSeller(TbSeller tbSeller);

    //查询商家信息，分页+模糊
    PageResult findPage(TbSeller tbSeller, int pageNum, int pageSize);

    //根据sellerId进行查询详情
    TbSeller findOne(String sellerId);

    //修改商家当前状态
    void updateStatus(String sellerId, String status);

    //保存商品信息
    void saveGoods(Goods goods);

    //修改商家信息
    void updateSeller(TbSeller tbSeller);

    //回显商家信息
    TbSeller findSellerInfo(String sellerId);

    //验证输入的原密码和数据库的密码是否一致
    Result checkOldPwd(String sellerId, String oldPwd);

    //修改密码
    void savePassword(String sellerId, String password);

    //修改商品信息
    public void updateGoodsInfo(Goods goods);
}
