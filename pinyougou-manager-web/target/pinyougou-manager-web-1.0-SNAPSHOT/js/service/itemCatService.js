//分类管理服务层
app.service('itemCatService',function($http){

    //根据父id查询分类信息，一级--二级--三级等信息
    this.findItemCatByPid = function(pid){
        return $http.get("../itemCat/findItemCatByPid.do?pid="+pid);
    }

    //删除分类信息
    this.deleteItemCat = function(ids){
        return $http.post("../itemCat/deleteItemCat.do",ids);
    }

    //保存分类信息
    this.saveItemCat = function(entity){
        return $http.post("../itemCat/saveItemCat.do",entity);
    }

    //根据id查询信息
    this.findOne = function(id){
        return $http.get("../itemCat/findOne.do?id="+id);
    }

    //全查商品列表
    this.findAll = function(){
        return $http.get("../itemCat/findAll.do");
    }

    //根据父id查询typeId
    this.getTypeIdByPid = function(pid){
        return $http.get("../itemCat/getTypeIdByPid.do?id="+pid);
    }

});