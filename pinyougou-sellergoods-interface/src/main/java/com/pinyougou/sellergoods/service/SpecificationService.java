package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;
import entity.PageResult;
import entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 规格信息业务逻辑层接口
 */
public interface SpecificationService {
    //全查-分页-模糊
    PageResult findPage(TbSpecification specification, int page, int rows);

    //批量删除，规格信息以及规格选项信息
    void deleteSpecification(Long[] ids);

    //保存规格信息以及规格选项信息
    void saveSpecification(Specification specification);

    //根据规格id查询出规格信息以及规格选项信息
    Specification findOne(Long id);

    //全查规格信息，模板管理select2插件回显时用的
    List<Map> selectSpecOptionList();
}
