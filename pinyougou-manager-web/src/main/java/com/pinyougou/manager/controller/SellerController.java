package com.pinyougou.manager.controller;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家控制层
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    //注入商家业务逻辑层接口
    @Autowired
    private SellerService sellerService;

    /**
     * 修改商家当前状态
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId,String status){
        try{
            sellerService.updateStatus(sellerId,status);
            return new Result(true,"更改成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更改失败!");
        }
    }

    /**
     * 根据sellerId进行查询详情
     */
    @RequestMapping("/findOne")
    public TbSeller findOne(String sellerId){
        return sellerService.findOne(sellerId);
    }

    /**
     * 查询商家信息，分页+模糊
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody TbSeller tbSeller,int pageNum,int pageSize){
        return sellerService.findPage(tbSeller,pageNum,pageSize);
    }

}
