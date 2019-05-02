//服务层
app.service('orderService', function ($http) {

    //读取列表数据绑定到表单中
    this.findOrderItemList = function () {
        return $http.get('../order/findOrderItemList');
    };

    //计算商品总件数，总价格
    this.sum = function (cartList) {

        //定义对象，封装总件数，总价格
        var totalValue = {totalNum: 0, totalPrice: 0};

        //循环遍历商家购物车列表
        for (var i = 0; i < cartList.length; i++) {
            var orderItemList = cartList[i].orderItemList;
            //循环
            for (var j = 0; j < orderItemList.length; j++) {
                //总件数
                totalValue.totalNum += orderItemList[j].num;
                totalValue.totalPrice += orderItemList[j].totalFee;
            }
        }

        return totalValue;
    }

    //提交订单
    this.submitOrder = function (entity) {
        return $http.post("../order/submitOrder", entity);
    }
});
