package com.pinyougou.sellergoods.service.impl;

import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.sellergoods.service.ItemCatService;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类业务逻辑层接口实现类
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    //注入商品分类数据访问层接口
    @Resource
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 根据父id查询分类信息，一级--二级--三级等信息
     * @param pid
     * @return
     */
    @Override
    public List<TbItemCat> findItemCatByPid(Long pid) {

        //根据传过来的父id进行查询
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(pid);

        return tbItemCatMapper.selectByExample(example);
    }


    //创建空集合
    List<Long> allIds = new ArrayList<Long>();

    /**
     * 删除分类信息
     * @param ids
     */
    @Override
    public void deleteItemCat(Long[] ids) {
        //判断传入的ids不为空，并且长度大于0
        if(ids!=null && ids.length>=1){
            for (Long id : ids) {
                getAllIdsById(id);
            }
        }
        //进行递归删除
        if(allIds!=null && allIds.size()>=1){
            for (Long id : allIds) {
                tbItemCatMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 保存和修改分类信息
     * @param tbItemCat
     */
    @Override
    public void saveItemCat(TbItemCat tbItemCat) {
        if(tbItemCat!=null){
            if(tbItemCat.getId()!=null){    //修改
                tbItemCatMapper.updateByPrimaryKeySelective(tbItemCat);
            }else{  //添加
                tbItemCatMapper.insertSelective(tbItemCat);
            }
        }
    }

    /**
     * 根据id查询分类信息
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return tbItemCatMapper.selectByPrimaryKey(id);
    }

    @Override
    public TbItemCat getTypeIdByPid(Long id) {
        return tbItemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 全查
     * @return
     */
    @Override
    public List<TbItemCat> findAll() {
        return tbItemCatMapper.selectByExample(null);
    }

    //进行递归
    private void getAllIdsById(Long id) {
        allIds.add(id);
        //根据父id查询商品分类信息
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

        /**
         * 删除分类，那么万一有别的地方引用的，需要做判断
         * 分类里面有typeid,判断这个typeid有没有被引用
         * 商品里面引用的typeid，其实影响不到我们，因为我们商品分类用到的是
         * 已经通过freemarker生成的静态界面，无非也就是后续我们用不了此分类了而已
         */
        if(list!=null && list.size()>=1){
            for (TbItemCat tbItemCat : list) {
                getAllIdsById(tbItemCat.getId());
            }
        }

    }

    /**
     * 阶乘测试
     */
    @Test
    public void test(){
        Long rs = getJieCheng(9000L);
        System.out.println(rs);
    }

    private Long getJieCheng(long x) {
        if(x==1){
            return 1L;
        }
        return x*getJieCheng(x-1);
    }
}
