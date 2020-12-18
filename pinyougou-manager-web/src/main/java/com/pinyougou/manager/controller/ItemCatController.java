package com.pinyougou.manager.controller;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类控制层
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    //注入商品分类业务逻辑层接口
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父id查询typeId
     */
    @RequestMapping("/getTypeIdByPid")
    public TbItemCat getTypeIdByPid(Long id){
        return itemCatService.getTypeIdByPid(id);
    }

    /**
     * 全查
     */
    @RequestMapping("/findAll")
    public List<TbItemCat> findAll(){
        List<TbItemCat> all = itemCatService.findAll();
        return all;
    }

    /**
     * 根据id查询分类信息
     */
    @RequestMapping("/findOne")
    public TbItemCat findOne(Long id){
        return itemCatService.findOne(id);
    }

    /**
     * 保存和修改分类信息
     */
    @RequestMapping("/saveItemCat")
    public Result saveItemCat(@RequestBody TbItemCat tbItemCat){
        try{
            itemCatService.saveItemCat(tbItemCat);
            return new Result(true,"编辑成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"编辑失败!");
        }
    }

    /**
     * 删除分类信息
     */
    @RequestMapping("/deleteItemCat")
    public Result deleteItemCat(@RequestBody Long[] ids){
        try{
            itemCatService.deleteItemCat(ids);
            return new Result(true,"删除成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败!");
        }
    }

    /**
     * 根据父id查询分类信息，一级--二级--三级等信息
     * @param pid
     * @return
     */
    @RequestMapping("/findItemCatByPid")
    public List<TbItemCat> findItemCatByPid(Long pid){
        return itemCatService.findItemCatByPid(pid);
    }

}
