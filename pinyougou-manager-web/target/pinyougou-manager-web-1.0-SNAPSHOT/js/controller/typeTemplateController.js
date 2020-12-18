 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller,typeTemplateService,brandService,specificationService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	

	//查询实体 
	$scope.findOne=function(id){
		$scope.selectBrandOptionList();
		$scope.selectSpecOptionList();
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;
				$scope.entity.brandIds=  JSON.parse($scope.entity.brandIds);//转换品牌列表
				$scope.entity.specIds=  JSON.parse($scope.entity.specIds);//转换规格列表
				$scope.entity.customAttributeItems= JSON.parse($scope.entity.customAttributeItems);//转换扩展属性
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){
		// alert($scope.selectIds);
		//获取选中的复选框			
		typeTemplateService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	//分页
	$scope.findPage=function(entity,page,rows){
		typeTemplateService.findPage(page,rows,entity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	$scope.brandList={data:[]};//品牌列表
	//全查品牌信息，模板管理select2插件回显时用的
	$scope.selectBrandOptionList = function(){
		brandService.selectBrandOptionList().success(function(response){
			$scope.brandList = {data:response};
		})
	}

	$scope.specList={data:[]};//规格列表
	//全查规格信息，模板管理select2插件回显时用的
	$scope.selectSpecOptionList = function(){
		specificationService.selectSpecOptionList().success(function(response){
			$scope.specList = {data:response};
		})
	}
	//新增扩展属性行
	$scope.addTableRows=function(){
		$scope.entity.customAttributeItems.push({});
	}
	//删除扩展属性行
	$scope.deleteTableRows=function(index){
		$scope.entity.customAttributeItems.splice(index,1);
	}

	//添加时初始化信息
	$scope.toAdd = function(){
		$scope.entity={customAttributeItems:[]};
		$scope.selectBrandOptionList();
		$scope.selectSpecOptionList();
	}

	//提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.jsonToString=function(jsonToString,key){
		var json = JSON.parse(jsonToString);	//将json字符串转换为json对象
		var value = "";
		for (var i=0;i<json.length;i++){
			if(i>0){
				value+=",";
			}
			value+=json[i][key];
		}
		return value;
	}
    
});	
