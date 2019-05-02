//服务层
app.service('payLogService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../payLog/findAll');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../payLog/findPage/'+page+'/'+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../payLog/findOne/'+id);
	}
	//增加 
	this.add=function(out_trade_no){
		return  $http.post('../payLog/add/'+out_trade_no);
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../payLog/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../payLog/delete/'+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../payLog/search/'+page+"/"+rows, searchEntity);
	}    	
});
