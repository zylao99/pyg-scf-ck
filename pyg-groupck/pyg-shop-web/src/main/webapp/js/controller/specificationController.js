//定义控制器
app.controller("specificationController", function ($scope,$controller,specificationService) {


    //继承父控制器
    //使用$controller服务继承父控制器
    //继承父控制器： 把父控制器$scope作用域传递给子控制器$scope作用域
    $controller("baseController",{$scope:$scope});



    //定义查询所有方法,所有的方法都必须绑定在$scope作用域
    $scope.findAll = function () {
        //使用内置服务$http向后台发送ajax请求
        specificationService.findAll().success(function (data) {
            $scope.list = data;
        })
    };

    //定义分页查询
    $scope.findPage = function (page, rows) {
        //使用内置服务向后台服务发送分页查询请求
        specificationService.findPage(page,rows).success(function (data) {
            //把总记录数赋值给分页控件
            $scope.paginationConf.totalItems = data.total;
            //分页结果
            $scope.list = data.rows;
        })
    };

    //定义保存函数
    $scope.save = function () {

        var objService = null;
        //判断是否是修改操作，还是保存操作
        if ($scope.entity.tbSpecification.id != null) {
            objService = specificationService.update($scope.entity);
        }else{
            //否则添加
            objService = specificationService.add($scope.entity);
        }
        //发送请求
        objService.success(function (data) {
            //判断
            if (data.success) {
                //刷新分页列表
                $scope.reloadList();
            } else {
                alert("保存失败");
            }
        })

    };

    //根据id查询规格及规格选项数据方法
    $scope.findOne = function (id) {
        //使用内置服务发送请求
        specificationService.findOne(id).success(function (data) {
            $scope.entity = data;
        })
    };



    //定义删除方法
    $scope.dele = function () {
        //发送请求
        specificationService.dele($scope.selectIds).success(function (data) {
            //判断
            if (data.success) {
                //清空数组
                $scope.selectIds = [];
                //刷新分页列表
                $scope.reloadList();
            } else {
                alert(data.message);
            }
        })
    };
    
    //添加规格选项行
    //entity = {tbSpecification:{},specificationOptionList:[]}
    //添加行核心原理就是添加空对象{}
    //entity = {tbSpecification:{},specificationOptionList:[{},{},{}]}
    $scope.addTableRow = function () {
        $scope.entity.specificationOptionList.push({});
    };

    //删除规格选项行
    $scope.deleTableRow = function (index) {
        $scope.entity.specificationOptionList.splice(index,1);
    }


})