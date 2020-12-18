app.controller('baseController', function ($scope) {
    //初始化searchEntity
    $scope.searchEntity = {};
    //分页参数赋值
    $scope.reloadList = function () {
        //切换页码
        $scope.findPage($scope.searchEntity, $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    },
        //初始化复选框id信息
        $scope.selectIds = [];
    $scope.updateSelectionId = function ($event,id) {
        if ($event.target.checked) {  //选中状态
            $scope.selectIds.push(id);  //进行赋值
        } else {
            // alert(111)
            //删除的话,我们需要把id在数组中的角标找到，然后按照角标去删除
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);
            // alert($scope.selectIds);
        }
    },
        //分页控件设置
        $scope.paginationConf = {
            currentPage: 1,	//当前页
            totalItems: 10,  //总页数
            itemsPerPage: 10, //每页显示条数
            perPageOptions: [1, 5, 10, 15, 20, 25, 30, 35],
            onChange: function () {
                $scope.reloadList();	//重新加载
            }
        } //分页控件结尾

    //从集合中按照key查找对象
    $scope.searchObjectByKey = function(list,key,keyValue){
        for(var i=0;i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }
})