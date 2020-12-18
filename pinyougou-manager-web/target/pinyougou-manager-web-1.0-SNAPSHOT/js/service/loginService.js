//登录界面服务层
app.service('loginService',function($http){

    //读取当前登录人名称
    this.showLoginName = function(){
        return $http.get('../login/getName.do');
    }
})