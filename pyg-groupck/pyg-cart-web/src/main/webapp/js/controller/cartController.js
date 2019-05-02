//控制层
app.controller('cartController', function ($scope, cartService) {

    $scope.selectedList = [];

    $scope.selectAll = function () {
        var cartList = $scope.cartList;
        $scope.select_all = !$scope.select_all;
        if ($scope.select_all) {
            for (var i = 0; i < cartList.length; i++) {
                var orderItemList = cartList[i].orderItemList;
                for (var j = 0; j < orderItemList.length; j++) {
                    orderItemList[j].checked = true;
                    $scope.selectedList.push(orderItemList[j].id);
                }
            }
        }
        else {
            for (var i = 0; i < cartList.length; i++) {
                var orderItemList = cartList[i].orderItemList;
                for (var j = 0; j < orderItemList.length; j++) {
                    orderItemList[j].checked = false;
                }
            }
            $scope.selectedList = [];
        }
        $scope.update();
    };

    $scope.checkCart = function () {
        var cartList = $scope.cartList;
        if (cartList != null && cartList != undefined) {
            for (var i = 0; i < cartList.length; i++) {
                cartList[i].checked = true;
                var orderItemList = cartList[i].orderItemList;
                for (var j = 0; j < orderItemList.length; j++) {
                    if (!orderItemList[j].checked)
                        cartList[i].checked = false;
                }
            }
        }
    };

    $scope.selectCart = function (cart) {
        cart.checked = !cart.checked;
        for (var i = 0; i < cart.orderItemList.length; i++) {
            cart.orderItemList[i].checked = cart.checked;
            if (cart.checked)
                $scope.selectedList.push(cart.orderItemList[i].id);
            else
                $scope.selectedList.splice($scope.selectedList.indexOf(cart.orderItemList[i].id), 1);
        }

        $scope.update();
    };
    $scope.selectOne = function (orderItem) {
        orderItem.checked = !orderItem.checked;
        var index = $scope.selectedList.indexOf(orderItem.id);
        if (orderItem.checked) {
            if (index == -1)
                $scope.selectedList.push(orderItem.id);
        }
        else {
            if (index != -1)
                $scope.selectedList.splice(index, 1);
        }
        $scope.update();
    }

    $scope.update = function () {
        $scope.totalValue = cartService.sum($scope.cartList);
        if ($scope.selectedList.length != $scope.totalValue.totalItem)
            $scope.select_all = false;
        else {
            $scope.select_all = true;
        }
        $scope.checkCart();
    };

    //查询购物车数据
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                //购物车列表集合
                $scope.cartList = response;

                //计算商品总数量，商品的总价格
                $scope.totalValue = cartService.sum($scope.cartList);

                $scope.select_all = true;

                for (var i = 0; i < $scope.cartList.length; i++) {
                    var orderItemList = $scope.cartList[i].orderItemList;
                    for (var j = 0; j < orderItemList.length; j++) {
                        if (orderItemList[j].checked)
                            $scope.selectedList.push(orderItemList[j].id);
                        else
                            $scope.select_all = false;
                    }
                }
            }
        );
        // $scope.select_all=true;
        // $scope.cartList=[{"sellerId":"me","orderItemList":[{"itemId":19,"sellerId":"me","totalFee":15,"goodsId":149187842867954,"price":5,"num":3,"checked":true,"id":1},{"itemId":19,"sellerId":"me","totalFee":20,"goodsId":149187842867954,"price":10,"num":2,"checked":true,"id":2}],"sellerName":"me"},{"sellerId":"me2","orderItemList":[{"itemId":19,"sellerId":"me2","totalFee":21,"goodsId":149187842867954,"price":7,"num":3,"checked":true,"id":3}],"sellerName":"me2"}];
        //
        // for (var i = 0; i < $scope.cartList.length; i++) {
        //     var orderItemList = $scope.cartList[i].orderItemList;
        //     for (var j = 0; j < orderItemList.length; j++) {
        //         if(orderItemList[j].checked)
        //         	$scope.selectedList.push(orderItemList[j].id);
        //         else
        //         	$scope.select_all=false;
        //     }
        // }
        $scope.update();
    };

    //添加购物车方法，删除
    $scope.addGoodsToCartList = function (itemid, num) {
        cartService.addGoodsToCartList(itemid, num).success(function (data) {
            if (data.success) {
                location.href = "cart.html";
            } else {
                alert(data.message);
            }
        })
    }


});	
