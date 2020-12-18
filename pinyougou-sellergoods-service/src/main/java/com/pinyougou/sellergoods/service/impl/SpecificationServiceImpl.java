package com.pinyougou.sellergoods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 规格信息业务逻辑层接口实现类
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    //规格信息数据访问层接口
    @Resource
    private TbSpecificationMapper tbSpecificationMapper;

    //规格选项信息数据访问层接口
    @Resource
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    /**
     * 全查-分页-模糊
     */
    @Override
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {

        /**
         * 条件查询时用的
         */
        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        if(specification!=null){
            if(specification.getSpecName()!=null && !"".equals(specification.getSpecName())){
                criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
            }
        }

        //根据example查询
        Page page = (Page)tbSpecificationMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除，规格信息以及规格选项信息
     */
    @Override
    public void deleteSpecification(Long[] ids) {

        //删除规格信息的同时也需要删除规格的选项信息
        if(ids!=null && ids.length>=1){
            for (Long id : ids) {
                /**
                 * 因为这里不知道规格选项id，所以需要用到example条件进行删除，
                 * 并根据规格id删除
                 */
                TbSpecificationOptionExample example = new TbSpecificationOptionExample();
                TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
                criteria.andSpecIdEqualTo(id);
                tbSpecificationOptionMapper.deleteByExample(example);

                //删除规格信息，有主键，按照主键删除
                tbSpecificationMapper.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 保存规格信息以及规格选项信息
     * @param specification
     */
    @Override
    public void saveSpecification(Specification specification) {
        /**
         * 判断这个主键id是否存在，存在的话就是修改，不存在就是新增
         */
        if(specification!=null){
            //获取规格id,用于非空判断
            Long id = specification.getTbSpecification().getId();
            if(id!=null && !"".equals(id)){     //不为空就是修改
                /**
                 * 先根据规格id删除对应的规格选项信息，然后进行新增，最后修改规格信息
                 */
                TbSpecificationOptionExample example = new TbSpecificationOptionExample();
                TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
                criteria.andSpecIdEqualTo(id);
                tbSpecificationOptionMapper.deleteByExample(example);

                //新增规格选项
                for (TbSpecificationOption so : specification.getTbSpecificationOptionList()) {
                    so.setSpecId(id);
                    tbSpecificationOptionMapper.insert(so);
                }

                //修改规格信息
                tbSpecificationMapper.updateByPrimaryKeySelective(specification.getTbSpecification());

            }else{ //为空就是新增
                /**
                 * 新增的话先新增规格信息，然后再把规格id拿回来，再添加进规格选项里面，再次新增规格选项
                 */
                tbSpecificationMapper.insert(specification.getTbSpecification());
                Long id1 = specification.getTbSpecification().getId();

                //新增规格选项信息
                List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
                for (TbSpecificationOption so : tbSpecificationOptionList) {
                    so.setSpecId(id1);
                    tbSpecificationOptionMapper.insert(so);
                }
            }

        }
    }

    /**
     * 根据规格id查询出规格信息以及规格选项信息
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        //1、创建规格以及规格选项组合实体类
        Specification specification = new Specification();

        //2、判断id不为空
        if(id!=null){
            //3、根据规格id获取对应的规格信息
            TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);

            //4、根据规格id获取对应的规格选项信息
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            List<TbSpecificationOption> list = tbSpecificationOptionMapper.selectByExample(example);

            //5、把获取到的规格信息存储到组合实体类中
            specification.setTbSpecification(tbSpecification);

            //6、规格选项信息也一样
            specification.setTbSpecificationOptionList(list);

            //7、返回结果
            return specification;
        }

        //8、返回null
        return null;
    }

    /**
     * 全查规格信息，模板管理select2插件回显时用的
     * @return
     */
    @Override
    public List<Map> selectSpecOptionList() {
        return tbSpecificationMapper.selectOptionList();
    }


}
