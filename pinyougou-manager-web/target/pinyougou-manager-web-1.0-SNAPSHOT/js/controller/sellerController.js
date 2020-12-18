//商家控制层
app.controller('sellerController',function($scope,$controller,sellerService){

    //继承baseController
    $controller('baseController',{$scope:$scope});

    //查询商品审核信息，分页+模糊
    $scope.findPage = function(searchEntity,pageNum,pageSize){
        sellerService.findPage(searchEntity,pageNum,pageSize).success(function(response){
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    }

    //根据sellerId进行查询详情
    $scope.findOne = function(sellerId){
        sellerService.findOne(sellerId).success(function (response) {
            $scope.entity = response;
        })
    }

    //修改商家当前状态
    $scope.updateStatus = function(sellerId,status){
        sellerService.updateStatus(sellerId,status).success(function (response) {
            if(response.success){
                $scope.reloadList();    //刷新列表
            }else{
                alert(response.message);
            }
        })
    }
})