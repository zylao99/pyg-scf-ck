//定义一个服务层service
app.service("brandService",function ($http) {

    //把发送请求方法全部抽取到服务层
    //查询所有品牌
    this.findAll = function () {
        return $http.get("../brand/findAll");
    };

    //分页查询
    this.findPage = function (page,rows) {
        return $http.get("../brand/findByPage/" + page + "/" + rows);
    };

    //保存
    this.add = function (entity) {
        return $http.post("../brand/add",entity);
    }

    //更新
    this.update = function (entity) {
        return $http.post("../brand/update",entity);
    };

    //定义根据id查询
    this.findOne = function (id) {
        return $http.get("../brand/findOne/" + id);
    };

    //删除方法
    this.dele = function (ids) {
        return $http.get("../brand/delete/" + ids);
    }

    //定义查询请求，查询品牌下拉列表，进行多项选择
    this.findBrandSelect2List = function () {
        return $http.get("../brand/findBrandSelect2List");
    };

});