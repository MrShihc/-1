//文件上传服务层
app.service('uploadService',function ($http) {

    //上传广告图
    this.uploadFile=function () {
        var formData = new FormData();
        formData.append("file",file.files[0]);
        return $http({
            method:"POST",
            url:"../content/uploadFile.do",
            data:formData,
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity
        })
    }


})