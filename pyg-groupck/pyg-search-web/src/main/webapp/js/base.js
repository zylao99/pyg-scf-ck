//定义模块
var app = angular.module("pyg", []);
//定义过滤器，处理html文本标签，让浏览器能识别html标签
app.filter("trustHtml",["$sce",function ($sce) {
    //解析html标签
    return function (data) {
        return $sce.trustAsHtml(data);
    }
    
}]);