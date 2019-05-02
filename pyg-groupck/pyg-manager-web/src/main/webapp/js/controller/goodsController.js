 //控制层 
app.controller('goodsController' ,function($scope,$controller,goodsService,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
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
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //商品状态
    //auditStatus=0  未审核
    //auditStatus=1  已审核
    //auditStatus=2  审核未通过
    //auditStatus=3  关闭
    //status[0]
    $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];

    //定义数组，封装分类名称
    $scope.allCatList = [];

    //查询所有的分类属性
    $scope.findAllItemCatList = function () {
        itemCatService.findAll().success(function (data) {

            //循环分类集合
            for (var i = 0; i < data.length; i++) {
                //把分类id作为数组角标
                //而在id角标对应位置上存储id对应分类名称
                $scope.allCatList[data[i].id] = data[i].name;
            }

        })
    };

    //定义方法，审核商家商品
	$scope.updateStatus = function (status) {

		//调用服务层方法
		goodsService.updateStatus($scope.selectIds,status).success(function (data) {
			//判断
			if(data.success){
				$scope.reloadList();
			}else{
				alert(data.message);
			}
        })

    }


});	
