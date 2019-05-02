//控制层
app.controller('seckillGoodsController', function ($scope, $location, $interval, seckillGoodsService,seckillOrderService) {


    //查询秒杀商品数据，在秒杀频道页进行展示
    $scope.findSecKillGoodsList = function () {
        seckillGoodsService.findSecKillGoodsList().success(
            function (response) {
                $scope.seckillList = response;
            }
        );
    };

    //跳转请求
    $scope.loadSeckillItem = function (id) {
        location.href = "seckill-item.html#?id=" + id;
    }

    //根据秒杀商品id查询秒杀详情，进入立即抢购页面
    $scope.findOne = function (id) {

        //接受参数
        var id = $location.search()["id"];
        //查询秒杀商品
        seckillGoodsService.findOne(id).success(function (data) {
            $scope.seckillGoods = data;

            //计算出剩余时间
            //getTime:表示获取毫秒时间
            var endTime = new Date($scope.seckillGoods.endTime).getTime();
            var nowTime = new Date().getTime();

            //剩余时间
            //Math.floor 向下取整
            //3.2  ==== 3
            //-5.9 ==== -6
            $scope.secondes = Math.floor((endTime - nowTime) / 1000);

            var time = $interval(function () {
                if ($scope.secondes > 0) {
                    //时间递减
                    $scope.secondes = $scope.secondes - 1;
                    //时间格式化
                    $scope.timeString = $scope.convertTimeString($scope.secondes);
                } else {
                    //结束时间递减
                    $interval.cancel(time);
                }
            }, 1000);


        })
    };

    //时间计算转换
    $scope.convertTimeString = function (allseconds) {
        //计算天数
        var days = Math.floor(allseconds / (60 * 60 * 24));

        //小时
        //6890s==== 6890/3600 = 向下取整 === > 1
        var hours = Math.floor((allseconds - (days * 60 * 60 * 24)) / (60 * 60));

        //分钟
        var minutes = Math.floor((allseconds - (days * 60 * 60 * 24) - (hours * 60 * 60)) / 60);

        //秒
        var seconds = allseconds - (days * 60 * 60 * 24) - (hours * 60 * 60) - (minutes * 60);

        //拼接时间
        var timString = "";
        if (days > 0) {
            timString = days + "天:";
        }
        return timString += hours + ":" + minutes + ":" + seconds;
    };


    //提交订单
    $scope.submitOrder = function (id) {
        //调用服务层方法
        seckillOrderService.submitOrder(id).success(function (data) {
            //判断
            if (data.success) {
                //跳转支付页面
                location.href = "pay.html";
            } else {
                alert(data.message);
            }
        })
    }


});	
