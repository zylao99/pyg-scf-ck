//服务层
app.service('cartService', function ($http) {

    //查询购物车列表
    this.findCartList = function () {
        return $http.get('../cart/findCartList');
    }
    //计算商品总价格
    this.sum = function (cartList) {
        //定义对象存储商品总数量，总价格
        var totalValue = {totalNum: 0, totalPrice: 0,totalItem:0};
        if(cartList!=""&&cartList!=undefined){
            //循环购物车列表
            for (var i = 0; i < cartList.length; i++) {
                //获取每一个商家的商品集合数据
                var orderItemList = cartList[i].orderItemList;
                //循环商家商品列表
                for (var j = 0; j < orderItemList.length; j++) {
                    totalValue.totalItem++;
                    if(orderItemList[j].checked) {
                        //计算总数量
                        totalValue.totalNum += orderItemList[j].num;
                        //计算总价格
                        totalValue.totalPrice += orderItemList[j].totalFee;
                    }
                }
            }
        }
        return totalValue;
    };

    //添加购物车
    this.addGoodsToCartList = function (itemId,num) {
        return $http.get('../cart/addGoodsToCartList/'+itemId+'/'+num);
    }
});
