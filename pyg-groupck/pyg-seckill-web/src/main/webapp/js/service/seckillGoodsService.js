//服务层
app.service('seckillGoodsService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findSecKillGoodsList=function(){
		return $http.get('../seckillGoods/findSecKillGoodsList');
	}

	this.findOne = function (id) {
        return $http.get('../seckillGoods/findOne/'+id);
    }


});
