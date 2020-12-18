package com.pinyougou.manager.controller;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 规格信息控制层
 */
@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Resource
    private SpecificationService specificationService;

    /**
     * 全查规格信息，模板管理select2插件回显时用的
     */
    @RequestMapping("/selectSpecOptionList")
    public List<Map> selectSpecOptionList(){
        return specificationService.selectSpecOptionList();
    }

    /**
     * 根据规格id查询出规格信息以及规格选项信息
     */
    @RequestMapping("/findOne")
    public Specification findOne(Long id){
        return specificationService.findOne(id);
    }

    /**
     * 保存规格信息以及规格选项信息
     */
    @RequestMapping("/saveSpecification")
    public Result saveSpecification(@RequestBody Specification specification){
        try {
            specificationService.saveSpecification(specification);
            return new Result(true,"编辑成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"编辑失败!");
        }
    }

    /**
     * 批量删除，规格信息以及规格选项信息
     */
    @RequestMapping("/deleteSpecification")
    public Result deleteSpecification(Long[] ids){

        try {
            specificationService.deleteSpecification(ids);
            return new Result(true,"删除成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败！");
        }
    }

    /**
     * 全查-分页-模糊查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody TbSpecification specification, int page, int rows){

        return specificationService.findPage(specification,page,rows);
    }

}
