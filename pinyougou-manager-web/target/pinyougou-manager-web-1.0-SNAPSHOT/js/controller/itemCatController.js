//分类管理控制层
app.controller('itemCatController',function($scope,$controller,itemCatService,typeTemplateService){
    //继承过来baseController的信息
    $controller('baseController',{$scope:$scope});

    //新增功能区域
    $scope.parentId = 0;    //上级ID

    //根据父id查询分类信息，一级--二级--三级等信息
    $scope.findItemCatByPid = function(pid){

        //查询时记录上级id
        $scope.parentId = pid;  //记住上级id

        itemCatService.findItemCatByPid(pid).success(function(response){
            $scope.list = response;
        })
    }

    //面包屑导航
    $scope.grade = 1;   //默认为1级
    //设置级别
    $scope.setGrade = function(value){
        $scope.grade = value;
        // console.log($scope.grade)
        // console.log(value);
    }

    //读取列表
    $scope.selectList=function(p_entity){
        if($scope.grade==1){    //一级
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        if($scope.grade==2){    //二级
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }
        if($scope.grade==3){    //三级
            $scope.entity_2 = p_entity;
        }
        $scope.findItemCatByPid(p_entity.id);       //查询此级下拉列表
    }

    //删除分类信息
    $scope.deleteItemCat=function(){
        itemCatService.deleteItemCat($scope.selectIds).success(function(response){
            if(response.success){
                $scope.entity_1 = null;
                $scope.entity_2 = null;
                $scope.grade = 1;
                $scope.findItemCatByPid(0);
            }else{
                alert(response.message);
            }
        })
    }

    //保存分类信息
    $scope.saveItemCat = function(){
        $scope.entity.parentId = $scope.parentId;  //赋予上级ID
        itemCatService.saveItemCat($scope.entity).success(function(response){
            if(response.success){
                $scope.entity_1 = null;
                $scope.entity_2 = null;
                $scope.grade = 1;
                $scope.findItemCatByPid(0);
            }else{
                alert(response.message);
            }
        })
    }

    //根据id修改
    $scope.findOne = function(id){
        $scope.findCasTemplate();
        itemCatService.findOne(id).success(function(response){
            $scope.entity = response;
        })
    }

    $scope.casTemplateList = [];
    //全查类型模板信息
    $scope.findCasTemplate = function(){
        typeTemplateService.findAll().success(function(response){
            $scope.casTemplateList = response;
        })
    }

    $scope.toAdd = function(){
        $scope.entity={};
        $scope.casTemplateList = [];
        $scope.findCasTemplate();
    }


});