//商品前端服务层
app.service('goodsService',function ($http) {

    //根据商家id查询指定商家的商品信息，以及分页加模糊
    this.findPage = function(searchEntity,pageNum,pageSize){
        return $http.post("../goods/search.do?page="+pageNum+"&rows="+pageSize,searchEntity);
    }

    //更改状态
    this.updateStatus = function(selectIds,status){
        return $http.post("../goods/updateStatus.do?status="+status,selectIds);
    }

    //查询实体
    this.findOne = function(id){
        return $http.get("../goods/findOne.do?id="+id);
    }

    //修改单个商品状态
    this.updateSingleStatus = function(status,id){
        return $http.get("../goods/updateSingleStatus.do?status="+status+"&id="+id);
    }

});