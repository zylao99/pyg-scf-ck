<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemarker list指令</title>
</head>
<body>
<#--
    例如：
    List pList = new ArrayList();
    pList.add(person);
    使用c标签如何循环集合：
    语法：jsp获取方法
    <c:foreach items="${pList}" var="p" varStatus="pv">
        ${p.name}
        ........
    </c:foreach>
    ftl模板语法：
    <#list pList as p>
        ${p.name}
        .........
    </#list>
    #list指令角标获取语法：别名_index
 -->
<table style="width: 600px;height: 300px;" border="1">
    <tr>
        <td>角标</td>
        <td>编号</td>
        <td>姓名</td>
        <td>性别</td>
        <td>年龄</td>
        <td>地址</td>
        <td>操作</td>
    </tr>

<#list pList as p>
    <#if p_index%2==0>
    <tr style="background-color: blue">
    <#else >
    <tr style="background-color: red">
    </#if>

    <td>${p_index}</td>
    <td>${p.id!}</td>
    <td>${p.username!}</td>
    <td>${p.sex!}</td>
    <td>${p.age!}</td>
    <td>${p.address!}</td>
    <td>
        <a href="#">删除</a>
        <a href="#">修改</a>
    </td>
</tr>
</#list>

</table>
<hr color="blue" size="2">
<h1>
    共 ${pList?size} 条记录
</h1>
</body>
</html>