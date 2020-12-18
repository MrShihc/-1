package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbItemCat;

import java.util.List;

/**
 * 商品分类业务逻辑层接口
 */
public interface ItemCatService {
    //根据父id查询分类信息，一级--二级--三级等信息
    List<TbItemCat> findItemCatByPid(Long pid);

    //删除分类信息
    void deleteItemCat(Long[] ids);

    //保存和修改分类信息
    void saveItemCat(TbItemCat tbItemCat);

    //根据id查询分类信息
    TbItemCat findOne(Long id);

    //根据pid查询typeId
    TbItemCat getTypeIdByPid(Long id);

    //全查
    List<TbItemCat> findAll();
}
