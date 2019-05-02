//定义控制器
app.controller("baseController", function ($scope) {

    //定义分页查询代码
    //定义reloadList
    $scope.reloadList = function () {
        $scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            //此方法将会被自动加载
            //1,页面刷新
            //2,分页控件中数据发生变化，reloadList也会自动调用
            $scope.reloadList();
        }
    };

    //定义数组，封装id
    $scope.selectIds = [];

    //定义方法，封装选中，获取取消品牌id
    $scope.updateSelection = function ($event, id) {
        //判断事件类型
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            //否则就是取消事件
            $scope.selectIds.splice($scope.selectIds.indexOf(id), 1);
        }

    };


});