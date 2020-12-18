package com.pinyougou.shop.controller;
import java.util.List;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	/**
	 * 查询实体
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);
	}

	/**
	 * 修改商品上架或下架
	 */
	@RequestMapping("/updateMarketable")
	public Result updateMarketable(String isMarketable,Long id){
		try{
			goodsService.updateMarketable(isMarketable,id);
			return new Result(true,"更新成功!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更新失败!");
		}
	}

	/**
	 * 批量提交审核
	 */
	@RequestMapping("/updateGoodsStatus")
	public Result updateGoodsStatus(String goodsStatus,@RequestBody Long[] ids){
		try {
			goodsService.updateGoodsStatus(goodsStatus,ids);
			return new Result(true,"提交成功,请等待运营商进行审核!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"提交失败,请及时联系后台管理员!");
		}
	}

	/**
	 * 批量删除商品信息
	 */
	@RequestMapping("/deleteGoods")
	public Result deleteGoods(String isDelete,@RequestBody Long[] ids){
		try{
			goodsService.deleteGoods(isDelete,ids);
			return new Result(true,"删除成功!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败!");
		}
	}

	/**
	 * 根据商家id查询指定商家的商品信息，以及分页加模糊
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods tbGoods, int page, int rows){
		//获取当前登录人
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//设置到tbGoods对象中
		tbGoods.setSellerId(sellerId);

		return goodsService.findPage(tbGoods, page, rows);
	}
	
}
