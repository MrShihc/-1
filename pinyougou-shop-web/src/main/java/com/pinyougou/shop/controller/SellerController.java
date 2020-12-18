package com.pinyougou.shop.controller;

import com.aliyuncs.exceptions.ClientException;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.SellerService;
import entity.PageResult;
import entity.Result;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 卖家控制层
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    //注入卖家业务逻辑层接口
    @Resource
    private SellerService sellerService;

    @Resource
    private GoodsService goodsService;

    /**
     * 修改商品信息
     */
    @RequestMapping("/updateGoodsInfo")
    public Result updateGoodsInfo(@RequestBody Goods goods){
        //校验是否当前商家id
        Goods goods2  = goodsService.findOne(goods.getTbGoods().getId());
        //获取当前登录的商家ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果传递过来的商家ID并不是当前登录的用户的ID,则属于非法操作
        if(!goods2.getTbGoods().getSellerId().equals(sellerId) || !goods.getTbGoods().getSellerId().equals(sellerId)){
            return new Result(false,"操作非法");
        }
        try {
            sellerService.updateGoodsInfo(goods);
            return new Result(true,"修改成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"修改失败!");
        }

    }

    /**
     * 上传图片
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {

        //调用fileservice子模块中的方法,目的是为了拿到过滤器中的项目前缀路径
//        response.sendRedirect("http://localhost:9104/file");
        try{

            //实现方式一：
               /* //获取全路径
                String path1 = request.getSession().getServletContext().getRealPath("/");
                //获取文件名
                String originalFilename = file.getOriginalFilename();
                //格式化日期
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String date = sdf.format(new Date());
                //生成文件前缀
                String vname = UUID.randomUUID().toString().replace("-", "").substring(0,6);
                //文件的拓展名
                String extname = originalFilename.substring(originalFilename.lastIndexOf("."));
                //生成图片存放路径
                File ff = new File(path1+"upload/"+date+"/"+vname+extname);

                //判断这个图片地址是否存在,不存在去创建
                if(!ff.exists()){
                    ff.mkdirs();
                }

                //转存文件
                file.transferTo(ff);
                return new Result(true,"/upload/" + date + "/" + vname + extname);
                */

            //获取文件名
            String originalFilename = file.getOriginalFilename();
            //格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(new Date());
            //生成文件前缀
            String vname = UUID.randomUUID().toString().replace("-", "").substring(0,6);
            //文件的拓展名
            String extname = originalFilename.substring(originalFilename.lastIndexOf("."));

            //拼接远程地址
            String address = "F:\\IDEAWorkSpace\\pinyougou\\pinyougou-fileservice\\src\\main\\webapp"+"/upload/" + date + "/" + vname + extname;

            //存入文件中
            File fff = new File(address);
            //判断地址是否存在,不存在创建
            if(!fff.exists()){
                fff.mkdirs();
            }
            //转存文件
            file.transferTo(fff );

            return new Result(true,"http://127.0.0.1:9104/upload/" + date + "/" + vname + extname);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,"文件上传失败!");
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping("/savePassword")
    public Result savePassword(String newPwd){
        try {
            //获取登录名
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            //把密码进行加密
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(newPwd);

            sellerService.savePassword(sellerId,password);
            return new Result(true,"修改成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"密码修改失败!");
        }
    }

    /**
     * 验证输入的原密码和数据库的密码是否一致
     */
    @RequestMapping("/checkOldPwd")
    public Result checkOldPwd(String oldPwd) {
        //获取登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return sellerService.checkOldPwd(sellerId, oldPwd);
    }

    /**
     * 回显商家信息
     */
    @RequestMapping("/findSellerInfo")
    public TbSeller findSellerInfo() {
        //获取登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();

        return sellerService.findSellerInfo(sellerId);
    }

    /**
     * 更改商家信息
     */
    @RequestMapping("/updateSeller")
    public Result updateSeller(@RequestBody TbSeller tbSeller) {
        //获取登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        tbSeller.setSellerId(sellerId);     //设置id
        try {
            sellerService.updateSeller(tbSeller);
            return new Result(true, "编辑成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑失败!");
        }
    }

    /**
     * 保存商品信息
     */
    @RequestMapping("/saveGoods")
    public Result saveGoods(@RequestBody Goods goods) {
        //获取登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.getTbGoods().setSellerId(sellerId);   //设置商家id
        try {
            sellerService.saveGoods(goods);
            return new Result(true, "编辑成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑失败!");
        }
    }

    /**
     * 注册商家
     */
    @RequestMapping("/saveSeller")
    public Result saveSeller(@RequestBody TbSeller tbSeller) {
        //密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(tbSeller.getPassword());
        tbSeller.setPassword(password);
        try {
            sellerService.saveSeller(tbSeller);
            return new Result(true, "注册成功,请等待运营商进行审核!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败!");
        }
    }

    /**
     * 获取验证码
     */
    @RequestMapping("/getCheckCode")
    public Result getCheckCode(@RequestBody TbSeller tbSeller) throws ClientException {
        return sellerService.getCheckCode(tbSeller);
    }

    /**
     * 验证登录名唯一性
     */
    @RequestMapping("/checkSellerId")
    public Result checkSellerId(@RequestBody TbSeller tbSeller) {
        return sellerService.checkSellerId(tbSeller);
    }

}
