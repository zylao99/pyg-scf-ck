//控制层
app.controller('typeTemplateController', function ($scope, $controller, typeTemplateService, brandService, specificationService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        typeTemplateService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        typeTemplateService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //把品牌json字符串转换json对象
                $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
                //规格
                $scope.entity.specIds = JSON.parse($scope.entity.specIds);
                //扩展属性
                $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);

            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = typeTemplateService.update($scope.entity); //修改
        } else {
            serviceObject = typeTemplateService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        typeTemplateService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        typeTemplateService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //编写过滤品牌，规格，扩展属性json格式数据
    //参数1：jsonObj = [{"id":33,"text":"电视屏幕尺寸"},{"id":32,"text":"机身内存"}]
    //参数2：'text'
    //例如：
    // 1,jsonObj[0].text √
    // 2,key = 'text'   jsonObj[0].key  ×  此时key是变量：必须采用 jsonObj[0][key] √
    $scope.jsonToStr = function (jsonStr, key) {
        //把json字符转换成json对象
        var jsonObj = JSON.parse(jsonStr);

        //定义空字符串，组装数据，数据之间逗号分隔
        var value = "";
        //循环json数组对象，组装数据
        for (var i = 0; i < jsonObj.length; i++) {

            //判断
            if (i > 0) {
                value += ",";
            }

            //组装数据
            value += jsonObj[i][key];


        }

        return value;

    }

    //定义一个select2插件接口数据
    // $scope.brandList = {data:[{id:'1',text:'联想'},{id:'2',text:'华为'}]};
    //定义查询请求，查询品牌下拉列表，进行多项选择
    $scope.findBrandList = function () {
        brandService.findBrandSelect2List().success(function (data) {
            $scope.brandList = {data: data};
        })
    };

    //定义方法，实现规格下拉列表，实现规格多项选择
    $scope.findSpecList = function () {
        //调用规格服务
        specificationService.findSpecList().success(function (data) {
            $scope.specList = {data: data};
        })
    };
    
    //添加扩展属性
    //动态添加行
    $scope.addTableRow = function () {
        //添加行到扩展属性字段
        $scope.entity.customAttributeItems.push({});
    };

    //删除扩展属性行
    $scope.deleTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    }

});	
