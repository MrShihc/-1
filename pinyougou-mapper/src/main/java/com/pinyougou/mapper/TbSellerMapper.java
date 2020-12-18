package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojo.TbSellerExample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbSellerMapper {
    int countByExample(TbSellerExample example);

    int deleteByExample(TbSellerExample example);

    int deleteByPrimaryKey(String sellerId);

    int insert(TbSeller record);

    int insertSelective(TbSeller record);

    List<TbSeller> selectByExample(TbSellerExample example);

    TbSeller selectByPrimaryKey(String sellerId);

    int updateByExampleSelective(@Param("record") TbSeller record, @Param("example") TbSellerExample example);

    int updateByExample(@Param("record") TbSeller record, @Param("example") TbSellerExample example);

    int updateByPrimaryKeySelective(TbSeller record);

    int updateByPrimaryKey(TbSeller record);

    Integer getSellerIdCount(@Param("sellerId") String sellerId);

    void updateStatus(@Param("sellerId") String sellerId, @Param("status") String status);

    //修改当前登录时间
    void updateLastLoginTimeBySellId(@Param("sellerId") String name,@Param("lastLoginTime") Date date);

    //获取上次登录时间
    Date selectBySellerId(@Param("sellerId") String name);
}