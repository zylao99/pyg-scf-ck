 //控制层 
app.controller('cartController' ,function($scope,cartService){
	
	//查询购物车数据
	$scope.findCartList=function(){
        cartService.findCartList().success(
			function(response){
				//购物车列表集合
				$scope.cartList = response ;

				//计算商品总数量，商品的总价格
				$scope.totalValue = cartService.sum($scope.cartList);

			}			
		);
	};
	
	//添加购物车方法，删除
	$scope.addGoodsToCartList = function (itemid,num) {
		cartService.addGoodsToCartList(itemid,num).success(function (data) {
			if(data.success){
				location.href="cart.html";
			}else {
				alert(data.message);
			}
        })
    }

    
});	
