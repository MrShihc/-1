/**
 * 规格信息控制层
 */
app.controller("specificationController",function($scope,$controller,specificationService){
    $controller("baseController",{$scope:$scope});

    //全查+分页+模糊
    $scope.findPage = function(searchEntity,page,rows){
        specificationService.findPage(searchEntity,page,rows).success(function(response){
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        })
    },
    /******************/
    //批量删除
    $scope.deleteSpecification = function () {
        specificationService.deleteSpecification($scope.selectIds).success(function (response) {
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        })
    },
    //保存信息
    $scope.saveSpecification = function(){
        specificationService.saveSpecification($scope.entity).success(function(response){
            // $scope.entity={tbspecificationOptionList:[]};
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        })
    },
    //修改回显
    $scope.findOne = function(id){
        specificationService.findOne(id).success(function(response){
           $scope.entity = response;
        });
    },
    //新增规格选项
    $scope.addTableRow = function(){
        $scope.entity.tbSpecificationOptionList.push({});
    },
    //删除规格选项一行
    $scope.deleteTableRow = function(index){
        $scope.entity.tbSpecificationOptionList.splice(index,1);
    }
})