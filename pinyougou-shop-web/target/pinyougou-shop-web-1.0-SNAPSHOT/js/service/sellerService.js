//商家服务层
app.service('sellerService',function($http){

    //验证登录名唯一性
    this.checkSellerId = function(entity){
        return $http.post("../seller/checkSellerId.do?m="+Math.random(),entity);
    }

    //获取验证码
    this.getCheckCode = function (entity) {
        return $http.post("../seller/getCheckCode.do?m="+Math.random(),entity);
    }

    //注册商家
    this.saveSeller = function(entity){
        return $http.post("../seller/saveSeller.do",entity);
    }

    //更改商家信息
    this.updateSeller = function(entity){
        return $http.post("../seller/updateSeller.do",entity);
    }

    //回显商家信息
    this.findSellerInfo = function(){
        return $http.get("../seller/findSellerInfo.do");
    }

    //验证输入的原密码和数据库的密码是否一致
    this.checkOldPwd = function(oldPwd){
        return $http.get("../seller/checkOldPwd.do?oldPwd="+oldPwd);
    }

    //修改密码
    this.savePassword = function(newPwd){
        return $http.post("../seller/savePassword.do?newPwd="+newPwd);
    }

})