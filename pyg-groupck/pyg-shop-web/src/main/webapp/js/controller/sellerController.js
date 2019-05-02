 //控制层 
app.controller('sellerController' ,function($scope,$controller,$location,sellerService,uploadService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.sellerMessage=function(){
		var sellerId = $location.search()['sellerId'];
		sellerService.sellerMessage(sellerId).success(
			function(response){
				$scope.entity=response;
			}			
		);
	}
	//读取用户输入的原密码
	$scope.sellerPassword = function () {
        var sellerId = $location.search()['sellerId'];
		if ( $scope.oneNewPassword == "" || $scope.oneNewPassword == undefined){
			alert("请输入密码");
			return;
		}else if($scope.twoNewPassword=="" || $scope.twoNewPassword == undefined){
			alert("请确认密码");
			return;
		}else if($scope.oneNewPassword != $scope.twoNewPassword){
			alert("密码不相等");
            return;
		}else {
            sellerService.sellerPassword(sellerId, $scope.oldPassword, $scope.twoNewPassword).success(function (response) {
                if (response.success) {
                    alert(response.message);
                    window.open("http://localhost:8082/logout");
                } else {
                    alert(response.message);
                }
            })
        }
    }
	
	//分页
	$scope.findPage=function(page,rows){			
		sellerService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		//记录待审核商家id
		$scope.sellerId = id;
		sellerService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//商家入驻 ： 注册 添加
	$scope.save=function(){
        var objService = null;
        //判断是否是修改操作，还是保存操作
        if ($scope.entity.sellerId != null) {
            objService = sellerService.update($scope.entity);
        }else{
            //否则添加
            objService = sellerService.add($scope.entity);
        }
        objService.success(
			function(response){
				if(response.success){
					alert(response.message);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		sellerService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		sellerService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};

	//定义图片上传方法
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (data) {
            //判断
            if (data.success) {
                $scope.image_entity.url = data.message;
                alert(data.message);
            } else {
                alert(data.message);
            }
        })
    };
    //定义方法，把图片颜色，图片地址绑定商品描述对象中itemImages属性中
    $scope.add_image_entity = function () {
        //itemImages=[{"color":"","url":"xx.jpg"}]
        // $scope.entity.logoPic.push($scope.image_entity);
         $scope.entity.logoPic = $scope.image_entity ;
    };
    
});	
