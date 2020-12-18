//商品服务层
app.service('goodsService',function ($http) {

    //修改商品信息
    this.updateGoodsInfo = function(entity){
        return $http.post("../seller/updateGoodsInfo.do",entity);
    }

    //保存商品信息
    this.saveGoods = function(entity){
        return $http.post("../seller/saveGoods.do",entity);
    }

    //根据商家id查询指定商家的商品信息，以及分页加模糊
    this.findPage = function(searchEntity,pageNum,pageSize){
        return $http.post("../goods/search.do?page="+pageNum+"&rows="+pageSize,searchEntity);
    }

    //批量删除商品信息
    this.deleteGoods = function(ids,isDelete){
        return $http.post("../goods/deleteGoods.do?isDelete="+isDelete,ids);
    }

    //批量提交审核
    this.updateGoodsStatus = function(goodsStatus,ids){
        return $http.post("../goods/updateGoodsStatus.do?goodsStatus="+goodsStatus,ids);
    }

    //修改上架或下架
    this.updateMarketable = function(isMarketable,id){
        return $http.get("../goods/updateMarketable.do?isMarketable="+isMarketable+"&id="+id);
    }

    //查询实体
    this.findOne = function(id){
        return $http.get("../goods/findOne.do?id="+id);
    }

})