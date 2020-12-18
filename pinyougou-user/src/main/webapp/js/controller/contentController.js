//广告前端控制层
app.controller('contentController',function($scope,$controller,contentService){

    //继承baseController
    $controller('baseController',{$scope:$scope});

    $scope.contentList=[];//广告集合

    //根据广告分类id查询广告列表
    $scope.findByCategoryId = function(categoryId){
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId]=response;
        })
    }

});