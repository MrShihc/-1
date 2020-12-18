app.controller('baseController',function ($scope) {
    $scope.searchEntity={},
        $scope.reloadList = function(){
            $scope.findPage($scope.searchEntity,$scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        },
        $scope.selectIds=[];
    $scope.updateSelection = function($event,id){
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else{
            //删除的话，我们需要把id在数组中角标找到，然后按照角标去删除
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx,1);
        }
    },
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [3,5,10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    }

    //从集合中按照key查找对象
    $scope.searchObjectByKey = function(list,key,keyValue){
        for(var i=0;i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }

});