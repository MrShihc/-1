angular.module('controllers', []).controller('registerCtrl', function($scope,$interval){
    $scope.paracont = "获取验证码";
    $scope.paraclass = "but_null";
    $scope.paraevent = true;
    var second = 60,
        timePromise = undefined;
    timePromise = $interval(function(){
        if(second<=0){
            $interval.cancel(timePromise);
            timePromise = undefined;
            second = 60;
            $scope.paracont = "重发验证码";
            $scope.paraclass = "but_null";
            $scope.paraevent = true;
        }else{
            $scope.paracont = second + "秒后可重发";
            $scope.paraclass = "not but_null";
            second--;
        }
    },1000,100);
});