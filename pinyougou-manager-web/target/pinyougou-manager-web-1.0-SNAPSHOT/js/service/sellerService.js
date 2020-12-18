//商家服务层
app.service('sellerService',function($http){

    //查询商品审核信息，分页+模糊
    this.findPage = function(searchEntity,pageNum,pageSize){
        return $http.post("../seller/findPage.do?pageNum="+pageNum+"&pageSize="+pageSize,searchEntity);
    }

    //根据sellerId进行查询详情
    this.findOne = function(sellerId){
        return $http.get("../seller/findOne.do?sellerId="+sellerId);
    }

    //修改商家当前状态
    this.updateStatus = function(sellerId,status){
        return $http.get("../seller/updateStatus.do?sellerId="+sellerId+"&status="+status);
    }

})