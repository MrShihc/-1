package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;
import entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 品牌管理的业务逻辑层接口
 */
public interface BrandService {
    //全查
    List<TbBrand> findAll();

    //分页查询所有信息，以及模糊查询
    PageResult findPageAndLikeQuery(TbBrand tbBrand, int page, int rows);

    //添加信息以及修改信息
    void saveBrandInfo(TbBrand tbBrand);

    //根据id查询
    TbBrand findOneById(Long id);

    //根据id批量删除品牌信息
    Result deleteBatchById(Long[] selectIds);

    //全查品牌信息，模板管理select2插件回显时用的
    List<Map> selectBrandOptionList();
}
