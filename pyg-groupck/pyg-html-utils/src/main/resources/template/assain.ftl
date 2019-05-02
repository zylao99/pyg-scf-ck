<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>assain常量指令</title>
    <#assign name="西游记"/>
    <#assign userinfo = {"name":"八戒","age":10000000,"address":"天河"}/>
</head>
<body>
<#include "hello.ftl"/>
<h1>
    获取常量值：${name}
</h1>
<h1>
    ${userinfo.name}<br>
    ${userinfo.age}<br>
    ${userinfo.address}<br>
</h1>
</body>
</html>