package com.pinyougou.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.SellerService;
import com.pinyougou.util.SendSms;
import com.pinyougou.util.VerificationCode;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 卖家业务逻辑层接口实现类
 */
@Service
public class SellerServiceImpl implements SellerService {

    //注入卖家数据访问层接口
    @Autowired
    private TbSellerMapper tbSellerMapper;
    //注入商品SPU(标准产品单位)数据访问层接口
    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    //注入商品SPU(标准产品单位)扩展数据访问层接口
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    //注入商品SKU(库存量单位)
    @Autowired
    private TbItemMapper tbItemMapper;

    //注入品牌数据访问层接口
    @Autowired
    private TbBrandMapper tbBrandMapper;

    //注入商品分类数据访问层接口
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    //

    /**
     * 验证登录名唯一性
     * @param tbSeller
     * @return
     */
    @Override
    public Result checkSellerId(TbSeller tbSeller) {
        if(tbSeller!=null){
            try{
                if(tbSeller.getSellerId()!=null){
                    TbSeller tbSeller1 = tbSellerMapper.selectByPrimaryKey(tbSeller.getSellerId());
                    if(tbSeller1!=null){
                        return new Result(false,"登录名已存在");
                    }
                    return new Result(true,"登录名可以使用");
                }else{
                    return new Result(false,"登录名已存在");
                }
            }catch (Exception e){
                e.printStackTrace();
                return new Result(false,"登录名已存在");
            }
        }else{
            return new Result(false,"登录名已存在");
        }
    }

    /**
     * 获取验证码
     * @param tbSeller
     * @return
     */
    @Override
    public Result getCheckCode(TbSeller tbSeller) throws ClientException {
        if(tbSeller!=null){
            if(tbSeller.getMobile().length()==11 && tbSeller.getMobile()!=null && tbSeller.getMobile()!=""){
                //六位纯数字随机验证码
//                String newcode = Integer.toString((int)(Math.random()*9999)+100000);

                String randomCode = VerificationCode.getRandomCode();
                SendSmsResponse sendSmsResponse = SendSms.sendSms(tbSeller.getMobile(), randomCode);

                //解析获取Code值的方式二:
//                String data = SendInfo.test01(tbSeller.getMobile(),newcode);
//                Map map = JSON.parseObject(data, Map.class);
//                String code1 = map.get("Code").toString();
                if(sendSmsResponse.getCode().equals("OK")){
                    return new Result(true,randomCode);
                }else{
                    return new Result(false,"手机号不合法");
                }
            }else{
                return new Result(false,"手机号不合法");
            }
        }
        return new Result(false,"手机号不合法");
    }

    /**
     * 注册商家
     * @param tbSeller
     */
    @Override
    public void saveSeller(TbSeller tbSeller) {
        tbSeller.setStatus("0");
        tbSeller.setCreateTime(new Date());
        tbSellerMapper.insertSelective(tbSeller);
    }

    /**
     * 查询商家信息，分页+模糊
     * @param tbSeller
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(TbSeller tbSeller, int pageNum, int pageSize) {

        //1.开启分页
        PageHelper.startPage(pageNum,pageSize);
        TbSellerExample example = new TbSellerExample();
        TbSellerExample.Criteria criteria = example.createCriteria();
        if(tbSeller!=null){
            //判断公司名称
            if(tbSeller.getName()!=null && !tbSeller.getName().equals("")){
                criteria.andNameLike("%"+tbSeller.getName()+"%");
            }
            //判断店铺名称
            if(tbSeller.getNickName()!=null && !tbSeller.getNickName().equals("")){
                criteria.andNickNameLike("%"+tbSeller.getNickName()+"%");
            }
            //判断状态
            if(!"-1".equals(tbSeller.getStatus())){
                criteria.andStatusEqualTo(tbSeller.getStatus());
            }
        }
        Page pageInfo = (Page)tbSellerMapper.selectByExample(example);
        return new PageResult(pageInfo.getTotal(),pageInfo.getResult());
    }

    /**
     * 根据sellerId进行查询详情
     * @param sellerId
     * @return
     */
    @Override
    public TbSeller findOne(String sellerId) {
        return tbSellerMapper.selectByPrimaryKey(sellerId);
    }

    /**
     * 修改商家当前状态
     * @param sellerId
     * @param status
     */
    @Override
    public void updateStatus(String sellerId, String status) {
        /**
         * 先查询在新增
         */
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(sellerId);
        tbSeller.setStatus(status);
        tbSellerMapper.updateByPrimaryKeySelective(tbSeller);
    }

    /**
     * 保存商品信息
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        /**
         * 先去保存商品SPU(标准产品单位)表信息TbGoods,
         *  然后在把SKU(库存量单位)保存后的主键id拿出来去保存商品扩展信息TbGoodsDesc,
         *  因为这两张表是主键关联，且只有主表能主键自增
         */
        //设置状态值默认为0     未审核
        goods.getTbGoods().setAuditStatus("0");
        //设置是否删除默认为0    展示
        goods.getTbGoods().setIsDelete("0");
        //设置是否上架默认为0    下架
        goods.getTbGoods().setIsMarketable("0");

        //插入商品表
        tbGoodsMapper.insertSelective(goods.getTbGoods());
        //获取新增id去保存商品SPU(标准产品单位)扩展表信息
        Long id = goods.getTbGoods().getId();
        goods.getTbGoodsDesc().setGoodsId(id);  //设置id

        //保存商品扩展表信息
        tbGoodsDescMapper.insertSelective(goods.getTbGoodsDesc());

        //插入商品SKU列表数据
        saveItemList(goods);
    }

    /**
     * 修改商家信息
     * @param tbSeller
     */
    @Override
    public void updateSeller(TbSeller tbSeller) {
        tbSellerMapper.updateByPrimaryKeySelective(tbSeller);
    }

    /**
     * 回显商家信息
     * @param sellerId
     * @return
     */
    @Override
    public TbSeller findSellerInfo(String sellerId) {
        return tbSellerMapper.selectByPrimaryKey(sellerId);
    }

    /**
     * 验证输入的原密码和数据库的密码是否一致
     * @param sellerId
     * @param oldPwd
     */
    @Override
    public Result checkOldPwd(String sellerId, String oldPwd) {
        //根据登录名查询
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(sellerId);

        //判断登录名是否存在
        if(tbSeller!=null){
            //拿到数据库的密码
            String password = tbSeller.getPassword();
            //使用spring安全内置方法，验证两次密码是否一致
            if(new BCryptPasswordEncoder().matches(oldPwd,password)){
                return new Result(true,oldPwd);
            }else{
                return new Result(false,"您输入的原密码和本次登录密码不一致!");
            }
        }else{
            return new Result(false,"系统繁忙或登录名不存在!");
        }
    }

    /**
     * 修改密码
     * @param sellerId
     * @param password
     */
    @Override
    public void savePassword(String sellerId, String password) {
        //现根据登录名查询
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(sellerId);
        //设置密码
        tbSeller.setPassword(password);
        //修改密码
        tbSellerMapper.updateByPrimaryKeySelective(tbSeller);
    }

    /**
     * 修改商品信息
     * @param goods
     */
    @Override
    public void updateGoodsInfo(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");     //设置未申请状态：如果是经过修改的商品，需要重新设置状态
        //设置是否删除默认为0    展示
        goods.getTbGoods().setIsDelete("0");
        tbGoodsMapper.updateByPrimaryKey(goods.getTbGoods());  //保存商品表
        tbGoodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());   //保存商品扩展表
        //删除原有的sku列表数据
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getTbGoods().getId());
        tbItemMapper.deleteByExample(tbItemExample);

        //添加新的sku列表数据
        saveItemList(goods);    //插入商品SKU列表数据
    }

    /**
     * 插入SKU列表数据
     * @param goods
     */
    private void saveItemList(Goods goods){
        //判断规格是否启用
        if("1".equals(goods.getTbGoods().getIsEnableSpec())){
            //遍历商品集合
            for(TbItem item:goods.getItemList()){
                //标题
                String title = goods.getTbGoods().getGoodsName();

                //获取规格集合
                Map<String,Object> specMap = JSON.parseObject(item.getSpec());

                for (String key : specMap.keySet()) {
                    title+= " " + specMap.get(key);
                }
                //设置标题
                item.setTitle(title);

                //调用封装的方法
                setItemValues(goods,item);

                //保存商品信息
                tbItemMapper.insert(item);
            }
        }else{
            TbItem item = new TbItem();
            item.setTitle(goods.getTbGoods().getGoodsName());   //商品KPU+规格描述串作为SKU名称
            item.setPrice(goods.getTbGoods().getPrice());   //价格
            item.setStatus("1");    //状态
            item.setIsDefault("1"); //是否默认
            item.setNum(99999);     //库存数量
            item.setSpec("{}");
            setItemValues(goods,item);

            //保存商品信息
            tbItemMapper.insert(item);
        }
    }

    /**
     * 保存时用的
     * @param goods
     * @param item
     */
    private void setItemValues(Goods goods,TbItem item){
        //商品SPU编号
        item.setGoodsId(goods.getTbGoods().getId());

        //商家编号
        item.setSellerId(goods.getTbGoods().getSellerId());

        //商品分类编号(3级)
        item.setCategoryid(goods.getTbGoods().getCategory3Id());

        //创建日期
        item.setCreateTime(new Date());

        //修改日期
        item.setUpdateTime(new Date());

        //品牌名称
        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
        item.setBrand(tbBrand.getName());

        //分类名称
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(goods.getTbGoods().getCategory3Id());
        item.setCategory(tbItemCat.getName());

        //商家名称
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
        item.setSeller(tbSeller.getNickName());

        //图片地址
        List<Map> imageList = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);

        if(imageList.size()>0){
            item.setImage((String)imageList.get(0).get("url"));
        }
    }

    public static void main(String[] args) {
        String zq = "{\"RequestId\":\"203A4FF5-45B4-4D87-81B6-9429ECF54B44\",\"Message\":\"OK\",\"BizId\":\"523025007560652518^0\",\"Code\":\"OK\"}";
        String cw = "{\"RequestId\":\"A41095C9-CAA2-4EFC-86B4-7C6D7E2E6312\",\"Message\":\"10008832000invalid mobile number\",\"Code\":\"isv.MOBILE_NUMBER_ILLEGAL\"}";
        Map map = JSONArray.parseObject(zq, Map.class);
        Object code = map.get("Code");
        System.out.println(code);
    }

}
