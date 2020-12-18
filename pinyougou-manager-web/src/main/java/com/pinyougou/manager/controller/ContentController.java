package com.pinyougou.manager.controller;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.pinyougou.content.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pinyougou.pojo.TbContent;

import entity.PageResult;
import entity.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 广告控制层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Resource
	private ContentService contentService;

	/**
	 * 批量开启广告
	 */
	@RequestMapping("/startContent")
	public Result startContent(String status,@RequestBody Long[] ids){
		try{
			contentService.startContent(status,ids);
			return new Result(true,"开启成功!");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"开启失败,请稍后重试!");
		}
	}

	/**
	 * 上传广告图
	 */
	@RequestMapping("/uploadFile")
	public Result uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		try{
			//获取文件名
			String fileName = file.getOriginalFilename();

			//格式化日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String date = sdf.format(new Date());

			//生成文件前缀
			String vname = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

			//生成文件后缀(即拓展名)
			String extname = fileName.substring(fileName.lastIndexOf("."));

			//拼接远程地址
			String address = "F:\\IDEAWorkSpace\\pinyougou\\pinyougou-fileservice\\src\\main\\webapp"+"/upload/" + date + "/" + vname + extname;

			//存入文件中
			File file1 = new File(address);
			//判断文件存放地址是否存在,不存在则创建
			if(!file1.exists()){
				file1.mkdirs();
			}

			//转存文件
			file.transferTo(file1);

			return new Result(true,"http://127.0.0.1:9104/upload/" + date + "/" + vname + extname);
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"上传失败!");
		}
	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbContent> findAll(){			
		return contentService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return contentService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param content
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbContent content){
		try {
			contentService.add(content);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param content
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbContent content){
		try {
			contentService.update(content);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbContent findOne(Long id){
		return contentService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			contentService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbContent content, int page, int rows  ){
		return contentService.findPage(content, page, rows);		
	}
	
}
