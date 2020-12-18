//商品控制层
app.controller('goodsController',
    function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){

    //继承baseController
    $controller('baseController',{$scope:$scope});

    //保存商品信息
    $scope.saveGoods = function(){
        //获取富文本编辑器的值
        $scope.entity.tbGoodsDesc.introduction=editor.html();
        goodsService.saveGoods($scope.entity).success(function (response) {
            if(response.success){
               alert(response.message);
               $scope.entity={};
               //清空富文本编辑器
               editor.html("");
               $scope.specList = {};
            }else{
                alert(response.message);
            }
        })
    }

    /**
     * 上传图片
     */
    $scope.uploadFile = function(){
        uploadService.uploadFile().success(function (response) {
            if(response.success){
                $scope.image_entity.url = response.message;     //设置文件地址
            }else{
                alert(response.message);
            }
        }).error(function(){
            alert("上传发生错误!");
        });
    }
    $scope.entity={tbGoods:{},tbGoodsDesc:{itemImages:[],specificationItems:[]}};

    $scope.add_image_entity=function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
    }

    //从列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.tbGoodsDesc.itemImages.splice(index,1);
    }

    //获取一级分类
    $scope.findItemCatListByPid  = function(pid){
        itemCatService.findItemCatListByPid(pid).success(function (response) {
            $scope.itemCat1List = response;
        })
    }

    //监听一级下拉框回显二级分类
    $scope.$watch('entity.tbGoods.category1Id',function(newValue,oldValue){
        itemCatService.findItemCatListByPid(newValue).success(function (response) {
            $scope.itemCat2List = response;
            $scope.itemCat3List=[];
            $scope.entity.tbGoods.typeTemplateId=null;
        })
    })

    //监听二级下拉框回显三级分类
    $scope.$watch('entity.tbGoods.category2Id',function (newValue,oldValue) {
        itemCatService.findItemCatListByPid(newValue).success(function (response) {
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

    $scope.updateSpecAttribute = function($event,name,value){
        var obj = $scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems,"attributeName",name);

        /**
         * 判断这个规格以前有没有选中，有被选择过，obj不为空，直接操作obj,然后在判断用户是取消选择还是选中
         *  取消选择。取消，还要判断改规格有没有其他选项被选中，要是都没有，就要把该规格对象直接删掉，要是选中直接加进去就可以了
         *  规格对象要是为空，创建一个新的放进去
         *
         */
        if(obj==null){
            $scope.entity.tbGoodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }else{
            if($event.target.checked){
                obj.attributeValue.push(value);
            }else{
                obj.attributeValue.splice(obj.attributeValue.indexOf(value),1);
                if(obj.attributeValue.length==0){   //判断改规格有没有其他没选择的没有全部删除，
                    $scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(obj),1);
                }
            }
        }
    }

    //创建SKU列表
    $scope.createItemList = function(){
        //设置初始值
        $scope.entity.itemList = [{spec:{},price:0,num:99999,status:"0",isDefault:"0"}];

        //因为后面用的话太长,所以起个短点的名字
        var items = $scope.entity.tbGoodsDesc.specificationItems;

        //循环赋值
        for(var i=0;i<items.length;i++){
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
        console.log($scope.entity.itemList)
    }

    //遍历添加规格
    addColumn = function(list,columnName,columnValues){
        var newList = [];   //新的集合
        for(var i=0;i<list.length;i++){
            var oldRow = list[i];

            for(var j=0;j<columnValues.length;j++){
                //深克隆，意思也就是说把一个想克隆的集合先转成字符串,然后在转成数组
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName] = columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

    $scope.add_spec_entity = function(){

        var values = $scope.spec_entity.values;
        var split = values.split(",");
        var aa = [];
        for(var x=0;x<split.length;x++){
            aa.push({'optionName':split[x]});
        }
        $scope.specList.push({'text':$scope.spec_entity.text,'options':aa});
    }


    //显示状态
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

    $scope.itemCatList = [];    //商品分类列表

    //加载商品分类列表
    $scope.findItemCatList = function(){
        itemCatService.findAll().success(function (response) {
            for(var i=0;i<response.length;i++){
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        });
    }

    //批量删除商品信息
    $scope.deleteGoods = function(isDelete){
        goodsService.deleteGoods($scope.selectIds,isDelete).success(function(response){
            if(response.success){
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        })
    }

    //批量提交审核
    $scope.updateGoodsStatus = function(goodsStatus){
        goodsService.updateGoodsStatus(goodsStatus,$scope.selectIds).success(function(response){
            if(response.success){
                alert(response.message);
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        })
    }

    //修改上架或下架
    $scope.updateMarketable = function(isMarketable,id){
        goodsService.updateMarketable(isMarketable,id).success(function(response){
            if(response.success){
                alert(response.message);
                $scope.reloadList();
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

    //保存
    $scope.save = function(){
        //提取富文本编辑器的值
        $scope.entity.tbGoodsDesc.introduction=editor.html();
        var searchObject;   //服务层对象
        if($scope.entity.tbGoods.id!=null){     //如果有Id
            serviceObject = goodsService.updateGoodsInfo($scope.entity);    //修改
        }else{
            serviceObject = goodsService.saveGoods($scope.entity);    //增加
        }
        serviceObject.success(function (response) {
            if(response.success){
                alert('保存成功');
                $scope.entity={};
                editor.html("");
                location.href="goods.html";     //跳转到商品列表页
            }else{
                alert(response.message);
            }
        })
    }

});