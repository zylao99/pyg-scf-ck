//服务层
app.service('addressService',function($http){
	    	
	//查询订单收货人地址列表
	this.findAddressList=function(){
		return $http.get('../address/findAddressList');
	}

	//增加订单收货人地址
	this.add = function (add) {
		return $http.post('../address/add',add);
    }
});
