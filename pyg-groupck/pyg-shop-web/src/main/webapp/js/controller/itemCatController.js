//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            $scope.entity.parentId =  $scope.parentId;
            //父id属性就有值
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findCatListByParentId($scope.entity.parentId);
                    $scope.entity = {};
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.findCatListByParentId($scope.parentId);
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //定义方法查询商品分类列表数据
    //首先查询顶级节点
    $scope.findCatListByParentId = function (parentId) {
        //记录此节点id,作为添加此节点父节点id
        $scope.parentId = parentId;
        itemCatService.findCatList(parentId).success(function (data) {
            $scope.catList = data;
        })
    };


    //记录分类级别
    //定义变量，初始化级别，默认第一级
    $scope.grade = 1;

    //定义方法，每次点击一下级，级别加1
    $scope.setGrade = function (value) {
        $scope.grade = value;
    }

    //定义查询下级节点方法，记录节点对象
    $scope.selectList = function (entity) {



        //判断目前所处于级别
        if ($scope.grade == 1) {
            //定义2个对象，记录级别对象
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        if ($scope.grade == 2) {
            $scope.entity_1 = entity;
            $scope.entity_2 = null;
        }

        if ($scope.grade == 3) {
            $scope.entity_2 = entity;
        }
        //查询下级节点
        $scope.findCatListByParentId(entity.id);

    }


});
