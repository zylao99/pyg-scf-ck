<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemarker 空值处理</title>
</head>
<body>
<h1>第一种处理方式：default内建函数</h1>
<h1>默认值：您好:${name?default("默认值")},热烈欢迎：${message?default("默认值")}</h1>
<h1>显示空：您好:${name?default("")},热烈欢迎：${message?default("")}</h1>

<hr color="red" size="2">
<h1>第二种处理方式：！处理空值</h1>
<h1>默认值：您好:${name!"默认值"},热烈欢迎：${message!"默认值"}</h1>
<h1>显示空：您好:${name!},热烈欢迎：${message!}</h1>


<hr color="red" size="2">
<h1>第三种处理方式：if判断处理空值</h1>
<h1>您好:
<#if name??>
    ${name},
</#if>
    热烈欢迎：
<#if message??>
    ${message}
</#if>
</h1>
</body>
</html>