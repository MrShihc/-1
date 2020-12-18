package com.pinyougou.manager.controller;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理的控制层
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Resource
    private BrandService brandService;

    /**
     * 全查品牌信息，模板管理select2插件回显时用的
     */
    @RequestMapping("/selectBrandOptionList")
    public List<Map> selectBrandOptionList(){
        return brandService.selectBrandOptionList();
    }

    /**
     * 根据id批量删除品牌信息
     */
    @RequestMapping("/deleteBatchById")
    public Result deleteBatchById(@RequestBody Long[] selectIds){
        return brandService.deleteBatchById(selectIds);
    }

    /**
     * 根据id查询
     */
    @RequestMapping("/findOneById")
    public TbBrand findOneById(Long id){
        return brandService.findOneById(id);
    }

    /**
     * 保存信息
     */
    @RequestMapping("/saveBrandInfo")
    public Result saveBrandInfo(@RequestBody TbBrand tbBrand){
        try{
            brandService.saveBrandInfo(tbBrand);
            return new Result(true,"编辑成功！");
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,"编辑失败！");
        }
    }

    /**
     * 分页查询，以及模糊查询共用
     */
    @RequestMapping("/findPageAndLikeQuery")
    public PageResult findPageAndLikeQuery(@RequestBody TbBrand tbBrand, int page, int rows){

        return brandService.findPageAndLikeQuery(tbBrand,page,rows);
    }

    /**
     * 全查
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }


    public static void main(String[] args) {
//        Integer i1=100;
//        Integer i2=100;
        Integer i3=-128;
        Integer i4=-128;
//        System.out.println(i1==i2);
        System.out.println(i3==i4);
    }
}
