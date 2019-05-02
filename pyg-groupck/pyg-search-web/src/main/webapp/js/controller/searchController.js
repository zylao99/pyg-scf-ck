//控制层
app.controller('searchController', function ($scope, $location, searchService) {

    //搜索页面传递哪些搜索参数
    //1,关键词
    //2,分类
    //3,品牌
    //4,规格属性
    //5,价格
    //6,排序
    //7,分页
    //定义对象，封装搜索搜索参数
    $scope.searchMap = {
        keywords: "",
        category: "",
        brand: "",
        spec: {},
        price: "",
        sortField: "",
        sort: "ASC",
        page: 1,
        pageSize: 40
    };

    //搜索页面传递搜索参数时候，页面需要刷新
    $scope.reloadSearchList = function () {
        if ($scope.searchMap.keywords != null && $scope.searchMap.keywords != '') {
            //携带参数页面刷新
            window.location.href = "http://localhost:8084/search.html#?keywords=" + $scope.searchMap.keywords;
            //携带参数页面刷新，必须reload为true
            window.location.reload(true);
        } else {
            window.location.href = "http://localhost:8084/search.html";
        }
    }


    //抽取关键词查询参数封装方法
    $scope.loadSearchList = function () {
        //接受参数
        var keywords = $location.search()['keywords'];

        if (keywords == undefined){
            keywords = "";
        }

        //把查询关键词封装到查询对象中
        $scope.searchMap.keywords = keywords;

        //调用查询方法
        $scope.searchList();

    }


    //angularJS参数路由：接受静态页面传递的参数
    //使用$location服务接受参数
    //语法：
    //1,$location.search().key
    //2,$location.search()['key']
    ///search.html#?keywords=华为
    $scope.searchList = function () {
        //调用服务层方法，发送搜索请求
        searchService.searchList($scope.searchMap).success(function (data) {
            $scope.resultMap = data;
            //定义方法，实现分页页码动态封装
            buildPageLable();
        })

    };

    //实现分页页码动态封装
    buildPageLable = function () {
        //定义数组，封装动态页码
        $scope.pageLable = [];
        //获取总页码数
        var maxPage = $scope.resultMap.totalPages;
        //定义动态页码起始页
        var firstPage = 1;
        //定义动态页码结束页
        var lastPage = maxPage;
        //定义2个变量，用来标识前后省略号
        $scope.isPreDot = false;
        $scope.isPosDot = false;

        //判断页码是是否大于5页
        if (maxPage > 5) {
            //判断当前页如果小于5页
            if ($scope.searchMap.page <= 3) {
                //显示前5页
                lastPage = 5;
                //后面后省略号
                $scope.isPosDot = true;

            } else if ($scope.searchMap.page > maxPage - 2) {
                //显示后5页
                firstPage = maxPage - 4;
                //前面有省略号
                $scope.isPreDot = true;
            } else {
                //显示中间5页
                firstPage = $scope.searchMap.page - 2;
                lastPage = $scope.searchMap.page + 2;
                //前后都有省略号
                $scope.isPreDot = true;
                $scope.isPosDot = true;
            }
        }
        //直接封装分页结果
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLable.push(i);
        }
    }


    //定义条件搜索方法
    $scope.addFilterCondition = function (key, value) {
        //判断参数类型：是否是 分类，品牌，价格
        //分类，品牌，价格 参数格式都是key value模式，但是value是字符
        if (key == "category" || key == "brand" || key == "price") {
            //参数封装
            $scope.searchMap[key] = value;
        } else {
            //一定是规格参数条件
            $scope.searchMap.spec[key] = value;
        }
        //调用查询方法
        $scope.searchList();
    };

    //删除搜索条件，剩余条件合并重新再次查询结果
    $scope.removeSearchItem = function (key) {

        //判断参数类型：是否是 分类，品牌，价格
        //分类，品牌，价格 参数格式都是key value模式，但是value是字符
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = "";
        } else {
            delete $scope.searchMap.spec[key];
        }
        //再次调用查询方法
        $scope.searchList();

    };

    //定义搜索排序方法
    $scope.sortSearch = function (field, sort) {
        //把参数封装到searchMap
        $scope.searchMap.sortField = field;
        $scope.searchMap.sort = sort;
        //调用搜索方法
        $scope.searchList();
    };

    //定义分页查询方法
    $scope.queryForPage = function (page) {

        //判断页码是否符合条件
        if (page < 1) {
            return;
        }
        if (page > $scope.resultMap.totalPages) {
            return;
        }

        //封装参数
        $scope.searchMap.page = parseInt(page);
        //调用搜索方法
        $scope.searchList();


    };
    
    //定义方法，判断当前页码是否是第一页
    $scope.isTopPage = function () {
        if($scope.searchMap.page==1){
            return true;
        }
        return false;
    }

    //定义方法，判断当前页码是否是最后一页
    $scope.isLastPage = function () {
        if($scope.searchMap.page==$scope.resultMap.totalPages){
            return true;
        }
        return false;
    }


});
