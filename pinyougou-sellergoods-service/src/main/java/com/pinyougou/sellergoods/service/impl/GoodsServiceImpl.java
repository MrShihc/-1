package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;

import entity.PageResult;
import org.springframework.stereotype.Service;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper tbGoodsMapper;

	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	@Autowired
	private TbItemMapper tbItemMapper;

	/**
	 * 根据商家id查询指定商家的商品信息，以及分页加模糊
	 * @param goods
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		//开启分页
		PageHelper.startPage(pageNum, pageSize);

		//先判断，筛选出当前登录人的商品，然后在判断有没有根据条件查询
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){
			//商家编号判断
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			//商品名称判断
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			//状态判断
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			//是否上架判断
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteEqualTo(goods.getIsDelete());
			}
	
		}

		//根据example查询出需要的信息，状态--商品名称--商家编号
		Page<TbGoods> page= (Page<TbGoods>)tbGoodsMapper.selectByExample(example);

		//返回结果
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 批量删除商品信息
	 * @param ids
	 */
	@Override
	public void deleteGoods(String isDelete,Long[] ids) {
		if(ids.length>=0){
			for (Long id : ids) {
				//根据id去查询商品信息
				TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);

				//进行赋值
				tbGoods.setIsDelete(isDelete);

				//这里删除其实用的是软删除，方便我们后续去查看
				tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
			}
		}
	}

	/**
	 * 批量提交审核
	 * @param goodsStatus
	 * @param ids
	 */
	@Override
	public void updateGoodsStatus(String goodsStatus, Long[] ids) {
		if(ids.length >=0){
			for (Long id : ids) {
				TbGoods tbGoods = new TbGoods();
				tbGoods.setId(id);
				tbGoods.setAuditStatus(goodsStatus);
				tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
			}
		}
	}

	/**
	 * 修改商品上架或下架
	 * @param isMarketable
	 * @param id
	 */
	@Override
	public void updateMarketable(String isMarketable,Long id) {
		TbGoods tbGoods = new TbGoods();

		//设置商品id
		tbGoods.setId(id);

		//设置是否上架或下架
		tbGoods.setIsMarketable(isMarketable);

		//更新数据
		tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
	}

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id) {
		//创建组合实体类对象，用于装进查询出来的信息
		Goods goods = new Goods();

		//查询出来商品信息
		TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);

		//查询出来商品详情信息
		TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
		goods.setTbGoodsDesc(tbGoodsDesc);

		//查询SKU商品列表
		TbItemExample tbItemExample = new TbItemExample();
		TbItemExample.Criteria criteria = tbItemExample.createCriteria();
		criteria.andGoodsIdEqualTo(id);	//查询条件：商品ID
		List<TbItem> tbItemsList = tbItemMapper.selectByExample(tbItemExample);
		goods.setItemList(tbItemsList);

		return goods;
	}

	/**
	 * 批量更改商品状态
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, String status) {
		if(ids.length>=0){
			for (Long id : ids) {
				TbGoods tbGoods = new TbGoods();
				tbGoods.setId(id);
				tbGoods.setAuditStatus(status);
				tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
			}
		}
	}

	/**
	 * 修改单个商品状态
	 * @param status
	 * @param id
	 */
	@Override
	public void updateSingleStatus(String status, Long id) {
		TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
		tbGoods.setAuditStatus(status);

		tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
	}


}
