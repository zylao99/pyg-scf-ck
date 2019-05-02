//定义一个服务层service
app.service("specificationService",function ($http) {

    //把发送请求方法全部抽取到服务层
    //查询所有品牌
    this.findAll = function () {
        return $http.get("../specification/findAll");
    };

    //分页查询
    this.findPage = function (page,rows) {
        return $http.get("../specification/findByPage/" + page + "/" + rows);
    };

    //保存
    this.add = function (entity) {
        return $http.post("../specification/add",entity);
    }

    //更新
    this.update = function (entity) {
        return $http.post("../specification/update",entity);
    };

    //定义根据id查询
    this.findOne = function (id) {
        return $http.get("../specification/findOne/" + id);
    };

    //删除方法
    this.dele = function (ids) {
        return $http.get("../specification/delete/" + ids);
    }
    //定义方法，实现规格下拉列表，实现规格多项选择
    this.findSpecList = function () {
        return $http.get("../specification/findSpecList");
    }
    
});