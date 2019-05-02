//服务层
app.service('payService', function ($http) {

    //生成二维码
    this.createQrCode = function () {
        return $http.get('../pay/createQrCode');
    };

    //监控二维码支付状态
    this.queryStatus = function (out_trade_no) {
        return $http.get('../pay/queryStatus/'+out_trade_no);
    }

});
