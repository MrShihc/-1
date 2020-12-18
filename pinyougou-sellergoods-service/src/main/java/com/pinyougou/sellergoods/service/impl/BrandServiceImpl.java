package com.pinyougou.sellergoods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理的业务逻辑层实现类
 */

@Service
public class BrandServiceImpl implements BrandService {

    //注入品牌的数据访问接口
    @Resource
    private TbBrandMapper tbBrandMapper;

    /**
     * 全查
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * 分页查询所有信息，以及模糊查询
     *
     * @param tbBrand
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPageAndLikeQuery(TbBrand tbBrand, int pageNumber, int pageSize) {
        //开启分页
        PageHelper.startPage(pageNumber,pageSize);
        TbBrandExample tbBrandExample = new TbBrandExample();
        if(tbBrand!=null){
            TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
            //根据品牌名称模糊查询
            if(tbBrand.getName()!=null &&!"".equals(tbBrand.getName())){
                criteria.andNameLike("%"+tbBrand.getName()+"%");
            }
            //根据品牌首字母模糊查询
            if(tbBrand.getFirstChar()!=null&&!"".equals(tbBrand.getFirstChar())){
                criteria.andFirstCharLike("%"+tbBrand.getFirstChar()+"%");
            }
        }

        //分页信息
        Page<TbBrand> tbBrandPage = (Page<TbBrand>)tbBrandMapper.selectByExample(tbBrandExample);
        return new PageResult(tbBrandPage.getTotal(),tbBrandPage.getResult());
    }

    /**
     * 添加信息以及修改信息通用
     * @param tbBrand
     */
    @Override
    public void saveBrandInfo(TbBrand tbBrand) {
        if(tbBrand!=null){
            if(tbBrand.getId()!=null){
                //修改
                tbBrandMapper.updateByPrimaryKeySelective(tbBrand);
            }else{
                //添加
                tbBrandMapper.insertSelective(tbBrand);
            }
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public TbBrand findOneById(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id批量删除品牌信息
     * @param selectIds
     * @return
     */
    @Override
    public Result deleteBatchById(Long[] selectIds) {
        if(selectIds.length>0){
            for (Long selectId : selectIds) {
                tbBrandMapper.deleteByPrimaryKey(selectId);
            }
            return new Result(true,"删除成功!");
        }
        return new Result(false,"请至少选择一项进行删除！");
    }

    /**
     * 全查品牌信息，模板管理select2插件回显时用的
     * @return
     */
    @Override
    public List<Map> selectBrandOptionList() {
        return tbBrandMapper.getBrandList();
    }
}
