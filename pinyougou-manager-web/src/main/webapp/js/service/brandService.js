//品牌服务层
app.service('brandService',function($http){
    //读取列表数据绑定到表单中
    this.findAll = function(){
        return  $http.get('../brand/findAll.do');
    },
        //返回分页
        this.findPage = function(searchEntity,page,rows){
            return $http.post('../brand/findPageAndLikeQuery.do?page=' + page + '&rows=' + rows,searchEntity);
        },
        //返回根据id查询的信息
        this.findOne = function(id){
            return $http.get("../brand/findOneById.do?id="+id);
        },
        //新增
        this.addBrand = function(tbbrand){
            return $http.post("../brand/saveBrandInfo.do",tbbrand);
        },
        //批量删除
        this.deleteBatchById = function (selectIds) {
            return $http.post("../brand/deleteBatchById.do",selectIds);
        },
        //全查品牌信息，模板管理select2插件回显时用的
        this.selectBrandOptionList = function(){
            return $http.get("../brand/selectBrandOptionList.do");
        }
});