app.controller("contentController",function ($scope, contentService) {

    $scope.contentList = [];

    $scope.findByCategoryId = function (categoryId) {
       contentService.findByCategoryId(categoryId).success(function (response) {
           $scope.contentList[categoryId] = response;
       })
    }

    //搜索索引库
    //angularJS参数路由:在静态页面上传递参数必须加上#
    //语法:#?key=value
    $scope.solrSearch = function () {
        //转发请求,把搜索请求转发搜索系统,且传递参数
        window.location.href = "http://localhost:8084/search.html#?keywords="+$scope.keywords;
    }

});