//首页控制层
app.controller('indexController',function($scope,$controller,loginService){

    $scope.lastLoginTime = "";

    //读取当前登录人
    $scope.showLoginName = function(){
        loginService.showLoginName().success(function(response){
            $scope.loginName = response.loginName;

            $scope.lastLoginTime = response.lastLoginTime;
        })
    }

});