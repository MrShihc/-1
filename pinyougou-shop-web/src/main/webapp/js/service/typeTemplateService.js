//服务层
app.service('typeTemplateService',function($http){
	    	
	//查询实体
	this.findOne=function(id){
		return $http.get('../typeTemplate/findOne.do?id='+id);
	}

	//查询规格列表
	this.findSpecList=function(id){
		return $http.get('../typeTemplate/findSpecList.do?id='+id);
	}
});
