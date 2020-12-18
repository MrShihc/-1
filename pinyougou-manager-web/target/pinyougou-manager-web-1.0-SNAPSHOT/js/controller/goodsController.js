//商品前端控制层
app.controller('goodsController',function($scope,$controller,$location,goodsService,itemCatService,typeTemplateService){

    //继承baseController
    $controller('baseController',{$scope:$scope});

    //获取一级分类
    $scope.findItemCatListByPid  = function(pid){
        itemCatService.findItemCatByPid(pid).success(function (response) {
            $scope.itemCat1List = response;
        })
    }

    //监听一级下拉框回显二级分类
    $scope.$watch('entity.tbGoods.category1Id',function(newValue,oldValue){
        itemCatService.findItemCatByPid(newValue).success(function (response) {
            $scope.itemCat2List = response;
            $scope.itemCat3List=[];
            $scope.entity.tbGoods.typeTemplateId=null;
        })
    })

    //监听二级下拉框回显三级分类
    $scope.$watch('entity.tbGoods.category2Id',function (newValue,oldValue) {
        itemCatService.findItemCatByPid(newValue).success(function (response) {
            $scope.itemCat3List = response;
            $scope.entity.tbGoods.typeTemplateId=null;
        })
    })

    //监听三级下拉框回显模板id
    $scope.$watch('entity.tbGoods.category3Id',function (newValue,oldValue) {
        itemCatService.getTypeIdByPid(newValue).success(function (response) {
            $scope.entity.tbGoods.typeTemplateId=response.typeId;
        })
    })

    //初始化
    $scope.specList = [{options:[]}];

    //模板ID选择后，更新品牌列表
    $scope.$watch('entity.tbGoods.typeTemplateId',function(newValue,oldValue){
        typeTemplateService.findOne(newValue).success(function(response){
            $scope.typeTemplate = response; //获取类型模板

            //品牌列表
            $scope.typeTemplate.brandIds =
                JSON.parse($scope.typeTemplate.brandIds);

            if($location.search()['id']==null){
                //扩展属性
                $scope.entity.tbGoodsDesc.customAttributeItems =
                    JSON.parse($scope.typeTemplate.customAttributeItems);
            }

            //我们可以在这里实现,然后和品牌还有扩展属性一样处理，但是层次太多
        });
        //单独处理的，规格
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.specList = response;
        });
    });

    $scope.status=['未审核','审核中','审核通过','已驳回'];//商品状态
    //根据商家id查询指定商家的商品信息，以及分页加模糊
    $scope.findPage = function(searchEntity,pageNum,pageSize){
        goodsService.findPage(searchEntity,pageNum,pageSize).success(function(response){
            //给集合赋值
            $scope.goodsList = response.rows;
            //给分页组件赋值，总个数
            $scope.paginationConf.totalItems = response.total;
        });
    }

    $scope.itemCatList=[];  //商品分类列表
    //查询商品分类
    $scope.findItemCatList = function(){
        itemCatService.findAll().success(function (response) {
            for(var i=0;i<response.length;i++){
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        })
    }

    //更改状态
    $scope.updateStatus = function(status){
        goodsService.updateStatus($scope.selectIds,status).success(function(response){
            // alert($scope.selectIds);
            if(response.success){
                $scope.reloadList();    //刷新列表
                $scope.selectIds=[];    //情况ID集合
            }else{
                alert(response.message);
            }
        })
    }

    //查询实体
    $scope.findOne = function(){
        var id = $location.search()['id'];  //获取参数值
        // alert(id)
        if(id==null){
            return null;
        }
        goodsService.findOne(id).success(function(response){
            $scope.entity=response;
            //向富文本编辑器添加商品介绍
            editor.html($scope.entity.tbGoodsDesc.introduction);
            //显示图片列表
            $scope.entity.tbGoodsDesc.itemImages=JSON.parse($scope.entity.tbGoodsDesc.itemImages);

            //显示扩展信息
            $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);

            //规格
            $scope.entity.tbGoodsDesc.specificationItems = JSON.parse($scope.entity.tbGoodsDesc.specificationItems);

            //SKU列表规格列转换
            for(var i=0;i<$scope.entity.itemList.length;i++){
                $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
            }

        })
    }

    //根据规格名称和选项名称返回是否被勾选
    $scope.checkAttributeValue = function(specName,optionName){
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
            return false;
        }else{
            if(object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }
    }

    //修改单个商品状态
    $scope.updateSingleStatus = function(status){
        var id = $location.search()['id'];  //获取参数值
        goodsService.updateSingleStatus(status,id).success(function(response){
           if(response.success){
               alert(response.message);
               location.href="goods.html";
           }else{
               alert(response.message);
           }
        });
    }

})