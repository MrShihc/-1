/**
 * 规格信息的服务层
 */
app.service("specificationService",function($http){

    //全查-分页-模糊
    this.findPage = function(searchEntity,page,rows){
        return $http.post("../spec/findPage.do?page="+page+"&rows="+rows,searchEntity);
    },
    //批量删除
    this.deleteSpecification = function(ids){
        return $http.post("../spec/deleteSpecification.do?ids="+ids);
    },
    //保存信息
    this.saveSpecification = function (entity) {
        return $http.post("../spec/saveSpecification.do",entity);
    },
    //修改回显
    this.findOne = function(id){
        return $http.get("../spec/findOne.do?id="+id);
    },
    //全查规格信息，模板管理select2插件回显时用的
    this.selectSpecOptionList = function () {
        return $http.get("../spec/selectSpecOptionList.do");
    }

});