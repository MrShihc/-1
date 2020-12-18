//
app.service('itemCatService',function($http){

    //三级分类
    this.findItemCatListByPid  = function(pid){
        return $http.get('../itemCat/findItemCatByPid.do?pid='+pid);
    }

    //根据父id查询typeId
    this.getTypeIdByPid = function(pid){
        return $http.get("../itemCat/getTypeIdByPid.do?id="+pid);
    }

    //全查商品列表
    this.findAll = function(){
        return $http.get("../itemCat/findAll.do");
    }

})