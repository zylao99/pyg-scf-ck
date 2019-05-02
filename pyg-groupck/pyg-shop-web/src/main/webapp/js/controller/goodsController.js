//控制层
app.controller('goodsController', function ($scope, $controller,
                                            goodsService,
                                            itemCatService,
                                            typeTemplateService,
                                            uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        //把富文本编辑器中值取出来赋值需要保存数据字段
        $scope.entity.goodsDesc.introduction = editor.html();
        //保存
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    //清空页面数据
                    $scope.entity = {};
                    //清空富文本编辑器
                    editor.html('');
                    //重新查询
                    alert(response.message);
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
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
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    //定义方法，首先查询商品的顶级节点，顶级父节点
    $scope.findItemCatList = function () {
        //调用服务层方法
        itemCatService.findItemCatListByParentId(0).success(function (data) {
            $scope.cat1List = data;
        })
    };

    //使用angularJS 监听服务 $watch ，动态监听变量的变化，一旦发现变量变化后，就可以做些操作
    //监听顶级节点值变量
    //参数1：表示要监听的变量
    //参数2：newValue 是新变化的值  oldValue:变化之前的值
    $scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
        //根据新的分类id查询子节点
        itemCatService.findItemCatListByParentId(newValue).success(function (data) {
            $scope.cat2List = data;
        })
    });

    //监听二级节点变量变化，一旦发生变化，立马查询二级节点的子节点
    $scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
        //根据新的分类id查询子节点
        itemCatService.findItemCatListByParentId(newValue).success(function (data) {
            $scope.cat3List = data;
        })


    });

    //监听第三级节点变量变化，一旦三级节点变化后，根据节点id查询节点对象，获取模板id
    $scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
        //根据id查询商品分类对象
        itemCatService.findOne(newValue).success(function (data) {
            //把模板id赋值商品表中模板字段，赋值后保存到数据库中
            $scope.entity.goods.typeTemplateId = data.typeId;
        })
    });


    //对象必须进行初始化
    $scope.entity = {goodsDesc: {itemImages: [], customAttributeItems: [], specificationItems: []}};

    //监控模板id变化，一旦模板id发生变化，查询模板对象
    $scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
        //根据id查询模板对象
        typeTemplateService.findOne(newValue).success(function (data) {
            //先初始化
            $scope.typeTemplate = data;
            //是否正确？
            $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);

            //获取模板中扩展属性赋值给商品描述表中扩展属性字段
            $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);

        });

        //调用service服务层方法，查询规格选项数据
        typeTemplateService.findSpecOptionsList(newValue).success(function (data) {
            $scope.specList = data;
        })

    });

    //定义图片上传方法
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (data) {
            //判断
            if (data.success) {
                $scope.image_entity.url = data.message;
            } else {
                alert(data.message);
            }
        })
    };

    //定义方法，把图片颜色，图片地址绑定商品描述对象中itemImages属性中
    $scope.add_image_entity = function () {
        //itemImages=[{"color":"","url":"xx.jpg"}]
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };

    //判断选中是哪个规格中属性值
    searchSpecOptions = function (list, key, name) {
        //循环规格选项集合
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == name) {
                return list[i];
            }
        }
        return null;
    }


    //定义方法，组装规格选项参数
    //参数格式：specificationItems = [{"attributeName":"网络","attributeValue":["电信2G","联通2G"]},
    // {"attributeName":"机身内存","attributeValue":["32G","16G","64G"]}]
    //参数1：事件对象
    //参数2：规格名称
    //参数3：(选中，取消)规格选项
    $scope.updateSpecOptionSelection = function ($event, text, name) {

        //获取规格选择值
        var specOptionList = $scope.entity.goodsDesc.specificationItems;

        //判断选中是哪个规格中属性
        //规格有：网络，内存 ，到底是选择是网络中规格属性，还是内存中规格属性？
        var obj = searchSpecOptions(specOptionList, 'attributeName', text);

        //判断选中的规格属性列表是否为空
        if (obj != null) {
            //判断是否是选中事件
            if ($event.target.checked) {
                obj.attributeValue.push(name);
            } else {
                //取消事件
                obj.attributeValue.splice(obj.attributeValue.indexOf(name), 1);

                //判断规格选项是否全部删除，如果规格选项已经完全删除，规格对象也必须删除
                if (obj.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(obj), 1);
                }
            }

        } else {
            //第一次选择，需要给规格属性specificationItems初始化数据
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": text, "attributeValue": [name]});

        }

    };

    // //参数格式：specificationItems = [{"attributeName":"网络","attributeValue":["电信2G","联通2G"]},
    // {"attributeName":"机身内存","attributeValue":["32G","16G","64G"]}]
    //定义方法，根据选中规格选项，动态生成sku行
    $scope.createSKUTable = function () {
        //初始化sku行 ,sku行就是{}对象
        $scope.entity.itemList = [{spec: {}, price: 0, num: 9999999, status: 1, isDefault: 1}];

        //获取选中规格选项值
        //第一次：[{"attributeName":"网络","attributeValue":["联通4G"]}]
        //第二次：[{"attributeName":"网络","attributeValue":["联通4G","联通3G"]}]
        //3:[{"attributeName":"网络","attributeValue":["联通4G","联通3G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
        var specList = $scope.entity.goodsDesc.specificationItems;

        //判断规格选项都取消选中，删除初始化行
        if (specList.length == 0) {
            $scope.entity.itemList = [];
        }

        //动态生成sku行
        //根据选中规格选项，动态生成sku行
        //循环规格选项
        for (var i = 0; i < specList.length; i++) {
            //抽取方法，生成行
            //第一次循环返回结果：itemList = [{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}]
            //第二次返回结果：itemList=[{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1},
            // {spec: {"网络":"联通3G"}, price: 0, num: 9999999, status: 1, isDefault: 1}]
            //3:itemList=[{spec: {"网络":"联通4G","机身内存":16G}, price: 0, num: 9999999, status: 1, isDefault: 1},
            // {spec: {"网络":"联通3G","机身内存":16G}, price: 0, num: 9999999, status: 1, isDefault: 1}]
            $scope.entity.itemList =
                addColumn($scope.entity.itemList, specList[i].attributeName, specList[i].attributeValue);

        }
    };

    //动态生成sku行
    //参数1：itemList 保存sku行集合
    //参数2：规格名称
    //参数3：规格属性 ["电信2G","联通2G"]
    addColumn = function (itemList, name, values) {

        //顶一个集合，封装sku行
        var newList = [];

        //循环sku行
        //第一次循环：[{spec: {}, price: 0, num: 9999999, status: 1, isDefault: 1}]
        //第二次：[{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}]
        for (var i = 0; i < itemList.length; i++) {
            //获取旧的行对象
            //第一次循环：{spec: {}, price: 0, num: 9999999, status: 1, isDefault: 1}
            //第二次：{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}
            var oldRow = itemList[i];

            //循环规格属性
            //第一次循环：values = ["联通4G"]
            //第二次：:["联通4G","联通3G"]
            for (var j = 0; j < values.length; j++) {
                //深克隆，克隆数据，但是新创建一个对象
                var newRow = JSON.parse(JSON.stringify(oldRow));
                //添加规格属性数据
                //第一次循环：{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}
                //第二次循环：{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}
                //{spec: {"网络":"联通3G"}, price: 0, num: 9999999, status: 1, isDefault: 1}
                newRow.spec[name] = values[j];
                //把新生成行添加到newList集合
                //[{spec: {"网络":"联通4G"}, price: 0, num: 9999999, status: 1, isDefault: 1}]
                newList.push(newRow);


            }

        }

        return newList;


    };

    //商品状态
    //auditStatus=0  未审核
    //auditStatus=1  已审核
    //auditStatus=2  审核未通过
    //auditStatus=3  关闭
    //status[0]
    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];

    $scope.markStatus = ['下架','上架'];

    //定义数组，封装分类名称
    $scope.allCatList = [];

    //查询所有的分类属性
    $scope.findAllItemCatList = function () {
        itemCatService.findAll().success(function (data) {

            //循环分类集合
            for (var i = 0; i < data.length; i++) {
                //把分类id作为数组角标
                //而在id角标对应位置上存储id对应分类名称
                $scope.allCatList[data[i].id] = data[i].name;
            }

        })
    };

    //定义方法，做上下架操作
    $scope.isMarketable = function (status) {
        //调用服务层方法
        goodsService.isMarketable($scope.selectIds, status).success(function (data) {
            //判断
            if (data.success) {
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            } else {
                alert(data.message);
            }

        })
    }


});	
