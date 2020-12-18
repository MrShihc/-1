package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbGoods;

import com.pinyougou.pojogroup.Goods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 根据商家id查询指定商家的商品信息，以及分页加模糊
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum,int pageSize);

	//批量删除商品信息
    void deleteGoods(String isDelete,Long[] ids);

    //批量提交审核
	void updateGoodsStatus(String goodsStatus, Long[] ids);

	//修改商品上架或下架
	void updateMarketable(String isMarketable,Long id);

	//查询实体
	Goods findOne(Long id);

	//批量更改商品状态
    void updateStatus(Long[] ids, String status);

    //修改单个商品状态
    void updateSingleStatus(String status,Long id);
}
