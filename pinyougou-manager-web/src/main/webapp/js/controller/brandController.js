//创建控制器  controller
app.controller('brandController', function ($scope,$controller,brandService) {

    //使用$controller来进行继承baseController,{$scope:$scope},
    // 就是为了让baseController和当前的controller中的$scope通用
    $controller('baseController',{$scope:$scope});

    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        brandService.findAll().success(function (response) {
            $scope.brandList = response;
        });
    },	//全查

        //全查并分页
        $scope.findPage = function (searchEntity,page, rows) {

            brandService.findPage(searchEntity,page,rows).success(function (response) {
                /**
                 * response是pageResult
                 * 里面有totalPage和list
                 * 把totalPage赋给前端的分页插件totalItems
                 * 把集合赋给brandList
                 */
                $scope.paginationConf.totalItems = response.total;
                $scope.brandList = response.rows;  //更新总记录数
            })
        },
        //保存信息
        $scope.addBrand = function(){
            brandService.addBrand($scope.tbbrand).success(function(response){
                if(response.success){
                    $scope.tbbrand = {};
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            });
        },
        //根据id查询信息回显数据
        $scope.findOne = function(id){
            brandService.findOne(id).success(function(response){
                $scope.tbbrand = response;
            });
        },

        //进行批量删除
        $scope.deleteBatchById = function(){
            brandService.deleteBatchById($scope.selectIds).success(function(response){
                if(response.success){
                    //删除成功,清空selectIds
                    $scope.selectIds=[];
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            });
        };  //删除end
});