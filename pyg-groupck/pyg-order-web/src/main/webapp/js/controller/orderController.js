//控制层
app.controller('orderController', function ($scope, addressService, orderService,loginService) {


    //查询购物车送货清单
    $scope.findOrderItemList = function () {
        orderService.findOrderItemList().success(function (data) {
            $scope.cartList = data;
            //调用服务层方法，计算商品总件数，总价格
            $scope.totalValue = orderService.sum($scope.cartList);
        })
    }

    //查询订单收货人地址列表
    $scope.findAddressList = function () {
        addressService.findAddressList().success(
            function (response) {
                $scope.addressList = response;

                //遍历地址列表，获取默认地址对象
                for (var i = 0; i < $scope.addressList.length; i++) {
                    //判断是否是默认地址
                    if ($scope.addressList[i].isDefault == '1') {
                        $scope.address = $scope.addressList[i];
                    }
                }

            }
        );
    };




    //定义方法，判断此时地址是否处于选中状态
    $scope.isSelected = function (address) {
        //判断
        if ($scope.address == address) {
            return true;
        }
        return false;
    };

    //动态选择默认地址
    $scope.changeAddress = function (address) {
        //循环遍历所有地址列表数据
        for (var i = 0; i < $scope.addressList.length; i++) {
            //判断
            if ($scope.addressList[i] == address) {
                $scope.addressList[i].isDefault = '1';
                $scope.address = address;
            } else {
                //把每一个对象isDefault属性都赋值为0
                $scope.addressList[i].isDefault = '0';

            }


        }
    };


    //定义方法，选择支付方式
    $scope.entity = {};
    $scope.selectPayWay = function (status) {
        $scope.entity.paymentType = status;
    }

    //定义提交订单方法
    $scope.submitOrder = function () {
        //封装支付金额
        $scope.entity.payment = $scope.totalValue.totalPrice;
        //封装送货地址
        $scope.entity.receiverAreaName = $scope.address.address;
        //收件人手机号
        $scope.entity.receiverMobile = $scope.address.mobile;
        //收件人
        $scope.entity.receiver = $scope.address.contact;

        //提交订单
        orderService.submitOrder($scope.entity).success(function (data) {
            //判断
            if (data.success) {
                location.href = "http://localhost:8088/pay.html";
            } else {
                alert(data.message);
            }
        })

    }

    //保存
    $scope.save = function () {
        addressService.add($scope.add).success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                    //重新查询
                    $scope.findAddressList();
                } else {
                    alert(response.message);
                }
            }
        );
    }

    //定义方法查询用户名
    $scope.showName = function () {
        //使用内置服务$http向后台发送ajax请求
        loginService.showLoginName().success(function (data) {
            $scope.loginName = data.loginName;
        })
    };


});	
