//控制层
app.controller('userController', function ($scope, $controller, userService, loginService, orderService,addressService,uploadService) {

    $controller('baseController', {$scope: $scope});//继承

    //获取用户登录信息
    $scope.showName = function () {
        loginService.loadLoginName().success(function (data) {
            $scope.loginName = data.loginName;
            $scope.loginPic = data.loginPic;
            $scope.findOne($scope.loginName);
            $scope.findAddressList();
        })
    };

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        userService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };

    //分页
    $scope.findPage = function (page, rows) {
        userService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    $scope.entity = {};
    //查询实体
    $scope.findOne = function (id) {
        userService.findOne(id).success(
            function(response){
                $scope.entity= response;
                $scope.birthday = $scope.entity.birthday.substring(0,10);
                $scope.date = $scope.birthday.split("-");
            }
        );
    };

    //发送消息，获取短信验证码
    $scope.sendSms = function () {
        //手机号不能为空
        if ($scope.entity.phone == null || $scope.entity.phone == "") {
            alert("手机号不能为空");
            return;
        }
        //调用服务实现发送消息，获取短信验证码
        userService.sendSms($scope.entity.phone).success(function (data) {
            if (data.success) {
                alert(data.message);
            } else {
                alert(data.message);
            }
        });
    };

    //保存
    $scope.save = function () {
        //验证2次是否匹配
        if ($scope.entity.password != $scope.password) {
            return;
        }
        //验证输入用户名，密码，手机号不能为空
        if ($scope.entity.username == null || $scope.entity.username == "") {
            return;
        }

        if ($scope.entity.password == null || $scope.entity.password == "") {
            return;
        }

        if ($scope.entity.phone == null || $scope.entity.phone == "") {
            return;
        }

        if ($scope.smsCode == null || $scope.smsCode == "") {
            return;
        }
        //完成注册
        userService.add($scope.entity, $scope.smsCode).success(
            function (response) {
                if (response.success) {
                    //重新查询
                    location.href = "login.html";
                } else {
                    alert(response.message);
                }
            }
        );
    };

    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        userService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        userService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };

    $scope.status = ["", "等待买家付款", "已付款", "未发货", "已发货", "交易成功", "交易关闭", "待评价"];
    //定义方法,实现分组查询订单
    /* $scope.findOrders = function () {
         orderService.findOrders().success(function (data) {
             $scope.orderList = data;
         })
     };*/

    //分页
    $scope.findOrders = function (pageNum, pageSize) {
        orderService.findOrders(pageNum, pageSize).success(function (response) {
                $scope.orderList = response.list;

                $scope.totalCount = response.totalCount;
                $scope.pageNum = response.pageNum;
                $scope.pageSize = response.pageSize;
                $scope.totalPage = response.totalPage;
            }
        );
    };

    $scope.range = function (n) {
        return new Array(n);
    };

    $scope.sum = function (order) {
        var orderItemList = order.orderItemList;
        var totalMoney = 0;
        for(var i = 0;i<orderItemList.length;i++){
            totalMoney += orderItemList[i].num*orderItemList[i].price;
        }
        return parseInt(totalMoney)+parseInt(order.orderList.postFee);
    }

    $scope.convertSpec = function (spec) {
        var specStr = spec.toString();
        return specStr.substring(1,specStr.length-1);
    };

    ///////////////\\\\\\\\\\\\\\\\\\\\\\\\
    //定义保存函数
    $scope.saveuser = function () {
        $scope.entity.birthday=this.date[0]+"-"+(this.date[1] < 10 ? "0" + this.date[1] : this.date[1])+"-"+ (this.date[2] < 10 ? "0" + this.date[2] : this.date[2])+' 00:00:00';

        userService.update($scope.entity).success(function (data) {
            //判断
            if (data.success) {
                alert("保存成功");
            } else {
                alert("保存失败");
            }
        })
    }
    //定义图片上传方法
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (data) {
            //判断
            if (data.success) {
                $scope.entity.headPic = data.message;

            } else {
                alert(data.message);
            }
        })
    };

    //查询订单收货人地址列表
    $scope.findAddressList = function () {
        addressService.findAddressList().success(
            function (response) {
                $scope.addressList = response;
            }
        );
    };

    //根据id查询
    $scope.addressFindOne = function (id) {
        //使用内置服务发送请求
        addressService.findOne(id).success(function (data) {
            $scope.address = data;
        })
    };

    //保存
    $scope.addressSave = function () {
        var serviceObject;//服务层对象
        if ($scope.address.id != null) {//如果有ID
            serviceObject = addressService.update($scope.address); //修改
        } else {
            serviceObject = addressService.add($scope.address);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findAddressList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    var addressAlias=['家里','父母家','公司'];
    //别名
    $scope.alias=function (index) {
        $scope.address.alias=addressAlias[index];
    }

    //删除
    $scope.deleAddress=function (ids) {
        addressService.delete(ids).success(function (data) {
            if (data.success){
                $scope.findAddressList();
            }else {
                alert(response.message);
            }
        })
    }
    //设置默认
    $scope.isDefault=function (id) {
        addressService.isDefault(id).success(function (data) {
            if (data.success){
                $scope.findAddressList();
            }else {
                alert(response.message);
            }
        })
    };

// 密码设置
    $scope.passwordSetting = function () {
        //验证2次是否匹配
        if ($scope.newPassword != $scope.confirm_password) {
            alert("密码不匹配");
            return;
        }
        if ($scope.entity.nickName == null || $scope.entity.username == "") {
            alert("用户名不能为空");
            return;
        }
        if ($scope.newPassword == null || $scope.newPassword == "") {
            alert("密码不能为空");
            return;
        }
        if ($scope.confirm_password == null || $scope.confirm_password == "") {
            alert("密码不能为空");
            return;
        }
        userService.passwordSetting($scope.nickName, $scope.newPassword)
            .success(function (response) {
                if (response.success) {
                    //重新查询
                    alert(response.message);
                    location.reload();
                } else {
                    alert(response.message);
                }
            })
    };
    //验证验证码
    $scope.checkCode=function (code,newphone) {
        userService.checkCode(code,newphone).success(function (data) {
            if (data.success){
                $scope.entity.phone="";
                location.href="../home-setting-address-phone.html";
            }else {
                alert(data.message);
            }
        })
    };

    //更改手机号
    $scope.updatePhone=function (smsCode,loginName) {
        userService.updatePhone(smsCode,loginName,$scope.entity).success(function (data) {
            if(data.success){
                location.href="../home-setting-address-complete.html";
            }else {
                alert(data.message);
            }
        })
    };

});	
