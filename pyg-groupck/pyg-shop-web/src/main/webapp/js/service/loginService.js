//定义一个服务层service
app.service("loginService",function ($http) {

    //把发送请求方法全部抽取到服务层
    //查询所有品牌
    this.showLoginName = function () {
        return $http.get("../login/showName");
    };


});