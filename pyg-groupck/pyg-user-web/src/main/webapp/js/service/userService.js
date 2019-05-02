//服务层
app.service('userService',function($http){
	    	
	//调用服务实现发送消息，获取短信验证码
	this.sendSms=function(phone){
		return $http.get('../user/sendSms/'+phone);
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage/'+page+'/'+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../user/findOne/'+id);
	}
	//增加 
	this.add=function(entity,smsCode){
		return  $http.post('../user/add/'+smsCode,entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../user/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../user/delete/'+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../user/search/'+page+"/"+rows, searchEntity);
	}

    //密码设置
    this.passwordSetting = function (nickName, newPassword) {
        return $http.post('../user/passwordSetting/' + nickName + '/' + newPassword);
    }
    //验证验证码
    this.checkCode=function(code,phone){
        return $http.get('../user/checkCode/'+phone+'/'+code);
    };
    //修改
    this.updatePhone=function(smsCode,loginName,entity){
        return  $http.post('../user/updatePhone/'+smsCode+'/'+loginName,entity);
    }
});
