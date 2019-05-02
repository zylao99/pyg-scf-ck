//定义控制器
app.controller("loginController", function ($scope,loginService) {


    //定义查询所有方法,所有的方法都必须绑定在$scope作用域
    $scope.showName = function () {
        //使用内置服务$http向后台发送ajax请求
        loginService.showLoginName().success(function (data) {
            $scope.loginName = data.loginName;
        })
    };
})