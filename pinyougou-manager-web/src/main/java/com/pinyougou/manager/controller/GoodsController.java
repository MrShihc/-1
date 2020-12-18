package com.pinyougou.manager.controller;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	 * 修改单个商品状态
	 */
	@RequestMapping("/updateSingleStatus")
	public Result updateSingleStatus(String status,Long id){
		try{
			goodsService.updateSingleStatus(status,id);
			return new Result(true,"编辑成功!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"编辑失败,请稍后重试!");
		}
	}

	/**
	 * 批量更改商品状态
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(@RequestBody Long[] ids,String status){
		try{
			goodsService.updateStatus(ids,status);
			return new Result(true,"更改成功!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"更改失败!");
		}
	}

	/**
	 * 查询实体
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);
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
		PageResult page1 = goodsService.findPage(tbGoods, page, rows);
		return page1;
	}
	
}
