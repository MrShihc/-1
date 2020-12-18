//商家控制层
app.controller('sellerController',function($scope,$controller,sellerService,$interval){

    //初始化entity
    $scope.entity = {};

    //验证码定时器
    $scope.paracont = "获取验证码";
    $scope.paraclass = "but_null";
    $scope.paraevent = true;

    var second = 0, timePromise = undefined;

    function interval(){
        if(second<=0){
            $interval.cancel(timePromise);
            timePromise = undefined;

            $scope.paracont = "重发验证码";
            $scope.paraclass = "but_null";
            $scope.paraevent = true;
        }else{
            $scope.paracont = second + "秒后可重发";
            $scope.paraclass = "not but_null";
            second--;
        }
    }

   // timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次

    //测试定时器
    $scope.getCheckCodes = function(){
        if(second <= 0) {
            second = 60;
            timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次
        }
    }



    var flag = false;
    //给出用户登录名的要求提示
    $scope.checkSellerIdSpan = function(){
        document.getElementById("sellerIdSpan").innerHTML = "<font color='red'>登录名以数字字母、下划线组成,长度5-8位</font>";
        document.getElementById("sellerId").focus();
    }

    //给出用户登录密码的要求提示
    $scope.checkPasswordSpan = function(){
        document.getElementById("passwordSpan").innerHTML = "<font color='red'>密码以数字字母、下划线组成,5位及以上</font>";
        document.getElementById("password").focus();
    }

    //给出用户手机号码的要求提示
    $scope.checkMobileSpan = function(){
        //初始化
        $scope.ifmobile = "";
        document.getElementById("mobileSpan").innerHTML = "<font color='red'>手机号码必须为11位数字</font>";
        document.getElementById("mobile").focus();

    }

    //给出用户验证码的要求提示
    $scope.checkCodeSpan = function(){
        document.getElementById("checkCodeSpan").innerHTML = "<font color='red'>验证码为6位数字和字母组成</font>";
        document.getElementById("checkCode").focus();

    }


    //验证登录名的唯一性
    $scope.checkSellerId = function(){
        if($scope.entity.sellerId!=null && $scope.entity.sellerId!=""){
            var test = /^[^\u4e00-\u9fa5]\w{5,7}$/;
            var flag = test.test($scope.entity.sellerId);
            if(flag){
                sellerService.checkSellerId($scope.entity).success(function(response){
                    if(response.success){
                        document.getElementById("sellerIdSpan").innerHTML = "<font color='green'>√"+response.message+"</font>";
                    }else{
                        document.getElementById("sellerIdSpan").innerHTML = "<font color='red'>"+response.message+"</font>";
                        document.getElementById("sellerId").focus();
                    }
                });
                flag = true;
            }else{
                flag = false;
                document.getElementById("sellerIdSpan").innerHTML = "<font color='red'>登录名输入的格式不正确,请重新输入!</font>";
                document.getElementById("sellerId").focus();
            }
        }else{
            flag = false;
            document.getElementById("sellerIdSpan").innerHTML = "<font color='red'>请输入用户名</font>";
            document.getElementById("sellerId").focus();
        }
    }

    //登录密码验证
    $scope.checkPassword = function(){
        //验证密码长度不少于5位
        var passwordReg = /^\w{5,}$/;
        var flag = passwordReg.test($scope.entity.password);
        if($scope.entity.password!=null && $scope.entity.password.length>=1){
            if(flag){
                document.getElementById("passwordSpan").innerHTML="<font color='green'>√</font>"
                flag = true;
            }else{
                flag = false;
                document.getElementById("passwordSpan").innerHTML = "<font color='red'>密码格式不正确,请重新输入!</font>";
                document.getElementById("password").focus();
            }
        }else{
            flag = false;
            document.getElementById("passwordSpan").innerHTML = "<font color='red'>请输入密码</font>";
            document.getElementById("password").focus();
        }
    }

    //手机号验证
    $scope.checkMobile = function(){
        //验证手机号必须是11位纯数字
        var mobileReg = /^\d{11}$/;
        var flag = mobileReg.test($scope.entity.mobile);
        if($scope.entity.mobile!=null && $scope.entity.mobile.length>=1){
            if(flag){
                document.getElementById("mobileSpan").innerHTML = "";
                $scope.ifmobile = "ok";
                flag = true;
            }else{
                flag = false;
                document.getElementById("mobileSpan").innerHTML = "<font color='red'>手机号码格式不正确,请重新输入!</font>";
                document.getElementById("mobile").focus();
            }
        }else{
            flag = false;
            document.getElementById("mobileSpan").innerHTML = "<font color='red'>请输入手机号</font>";
            document.getElementById("mobile").focus();
        }
    }

    //验证码验证
    $scope.checkCodes = function(){
        //验证验证码必须是6位纯数字
        var checkCodeReg = /^[0-9a-zA-Z]{6}$/i;
        var flag = checkCodeReg.test($scope.entity.checkCode);
        if($scope.entity.checkCode!=null && $scope.entity.checkCode.length>=1){
            if(flag){
                document.getElementById("checkCodeSpan").innerHTML = "<font color='green'>√</font>";
                flag = true;
            }else{
                flag = false;
                document.getElementById("checkCodeSpan").innerHTML = "<font color='red'>验证码格式不正确,请重新输入!</font>";
                document.getElementById("checkCode").focus();
            }
        }else{
            flag = false;
            document.getElementById("checkCodeSpan").innerHTML = "<font color='red'>请输入验证码</font>";
            document.getElementById("checkCode").focus();
        }
    }


    $scope.checkCode = "";
    //获取验证码
    $scope.getCheckCode = function(){
        if($scope.entity.mobile!=null){
           if($scope.entity.mobile.length==11){
               var mobiletest = /^\d{11}$/;
               var flag = mobiletest.test($scope.entity.mobile);
               if(flag==true){
                   if(second <= 0) {
                       second = 60;
                       timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次

                       sellerService.getCheckCode($scope.entity).success(function(response){
                           if(response.success){
                               alert("短信发送成功，一分钟内有效!");
                               $scope.checkCode = response.message;
                               // alert($scope.checkCode);
                           }else{
                               alert(response.message);
                           }
                       })
                   }
               }else{
                   alert("手机号码不合法");
                   document.getElementById("mobile").focus();
               }
           }else{
               alert("手机号码不合法");
               document.getElementById("mobile").focus();
           }
        }else{
            alert("手机号码不能为空!");
            document.getElementById("mobile").focus();
        }
    }

    //注册商家
    $scope.saveSeller = function(){
        //判断登录名
        if($scope.entity.sellerId!=null && $scope.entity.sellerId.length>=1){
            //判断登录密码
            if($scope.entity.password!=null && $scope.entity.password.length>=1){
                //判断手机号码
                if($scope.entity.mobile!=null && $scope.entity.mobile.length>=1){
                    //判断验证码
                    if($scope.entity.checkCode!=null && $scope.entity.checkCode.length>=1){
                        flag = true;
                    }else{
                        flag = false;
                        document.getElementById("checkCodeSpan").innerHTML = "<font color='red'>验证码不能为空,请重新输入</font>";
                        document.getElementById("checkCode").focus();
                    }
                }else{
                    flag = false;
                    document.getElementById("mobileSpan").innerHTML = "<font color='red'>手机号码不能为空,请重新输入</font>";
                    document.getElementById("mobile").focus();
                }
            }else{
                flag = false;
                document.getElementById("passwordSpan").innerHTML = "<font color='red'>登录密码不能为空,请重新输入</font>";
                document.getElementById("password").focus();
            }
        }else{
            flag = false;
            document.getElementById("sellerIdSpan").innerHTML = "<font color='red'>登录名不能为空,请重新输入</font>";
            document.getElementById("sellerId").focus();
        }

        //都通过了去发送请求到后台,注册商家
        if(flag){
           if($scope.entity.checkCode==$scope.checkCode){
               sellerService.saveSeller($scope.entity).success(function(response){
                   if(response.success){
                       location.href="shoplogin.html";
                   }else{
                       alert(response.message);
                   }
               })
           }else{
               alert("您当前输入的验证码和发送的验证码不一致");
           }
        }

    }

    //更改商家信息
    $scope.updateSeller = function(){
        sellerService.updateSeller($scope.entity).success(function (response) {
            if(response.success){
                alert(response.message);
                $scope.findSellerInfo();
                $scope.entity = {};
            }else{
                alert(response.message);
            }
        })
    }

    //回显商家信息
    $scope.findSellerInfo = function(){
        sellerService.findSellerInfo().success(function (response) {
            $scope.entity = response;
        })
    }

    //修改密码
    $scope.checkOldPwd = function(){
        //判断旧密码不为空
        if($scope.entity.oldPwd!=null && $scope.entity.oldPwd.length>=1){
            //验证输入的原密码和数据库的密码是否一致
            sellerService.checkOldPwd($scope.entity.oldPwd).success(function (response) {
                if(response.success){
                    //判断新密码不为空
                    if($scope.entity.newPwd!=null && $scope.entity.newPwd.length>=1){
                        //判断确认密码不为空
                        if($scope.entity.trueNewPwd!=null && $scope.entity.trueNewPwd.length>=1){
                            //判断新密码和确认新密码是否一致
                            if($scope.entity.newPwd==$scope.entity.trueNewPwd){
                                //更改密码
                                $scope.savePassword = function(){
                                    sellerService.savePassword($scope.entity.newPwd).success(function (response) {
                                        if(response.success){
                                            alert(response.message);
                                            window.top.location="/shoplogin.html";
                                        }else{
                                            alert(response.message);
                                        }
                                    })
                                }
                            }else{
                                alert("新密码和确认新密码两次输入不一致,请重试!");
                            }
                        }else{
                            alert("确认密码不能为空!");
                            document.getElementById("trueNewPwd").focus();
                        }
                    }else{
                        alert("新密码不能为空!");
                        document.getElementById("newPwd").focus();
                    }
                }else{
                    alert("您输入的原密码和登录密码不一致");
                    document.getElementById("oldPwd").focus();
                }
            })
        }else{
            alert("原密码不能为空!");
            document.getElementById("oldPwd").focus();
        }
    }

    //


});



// angular.element(document).ready(function() {
//     angular.bootstrap(document,['pinyougou']);
// });