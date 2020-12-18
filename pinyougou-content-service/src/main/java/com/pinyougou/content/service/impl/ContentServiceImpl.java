package com.pinyougou.content.service.impl;

import java.util.List;

import com.pinyougou.content.service.ContentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 广告服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Resource
    private TbContentMapper contentMapper;

    //注入redis模板
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //清楚缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //查询修改前的分类id
        Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        //修改前先删除缓存的广告，避免存在脏数据
        redisTemplate.boundHashOps("content").delete(categoryId);

        //修改数据
        contentMapper.updateByPrimaryKey(content);

        //如果分类ID发生了修改，清除修改后的分类id的缓存
        if(categoryId!=content.getCategoryId()){
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //清除缓存
            //获取广告分类id
            Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();

            redisTemplate.boundHashOps("content").delete(categoryId);

            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getContent() != null && content.getContent().length() > 0) {
                criteria.andContentLike("%" + content.getContent() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量开启广告
     * @param status
     * @param ids
     */
    @Override
    public void startContent(String status, Long[] ids) {
        if(ids.length>=0){
            for (Long id : ids) {
                //现根据主键id进行查询,然后把前台传来的状态赋值给查出来的对象
                    //最终实现修改状态的功能
                TbContent tbContent = contentMapper.selectByPrimaryKey(id);
                tbContent.setStatus(status);
                contentMapper.updateByPrimaryKeySelective(tbContent);
            }
        }
    }

    /**
     * 根据广告分类id查询广告列表
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {

        /**
         * 先从redis中查询看一下有没有缓存好的数据，有的话直接返回没有的话去数据库中查询，
         * 并把查询后的结果存入redis中
         */
        //获取缓存中的数据
        List<TbContent> content = (List<TbContent>)redisTemplate.boundHashOps("content").get(categoryId);
        if(content==null){  //判断缓存中是否存在

            //根据广告分类id查询广告列表
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andStatusEqualTo("1");     //开启状态的
            example.setOrderByClause("sort_order");     //升序排序
            List<TbContent> tbContentList = contentMapper.selectByExample(example);

            //存入缓存
            redisTemplate.boundHashOps("content").put(categoryId,tbContentList);
            System.out.println("从数据库中查");
            //返回结果
            return tbContentList;
        }else{
            System.out.println("从缓存中读取数据!");
            return content;
        }
    }

}
