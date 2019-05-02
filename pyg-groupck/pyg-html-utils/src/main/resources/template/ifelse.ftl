<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemarker判断指令</title>
</head>
<body>
<h1>
    <#if flag=1>
        <div style="height: 100px;width:100px; background-color: blue;">

        </div>
        <#elseif flag=2>
            <div style="height: 100px;width:100px; background-color: red;">

            </div>
        <#else>
        <div style="height: 100px;width:100px; background-color: black;">

        </div>

    </#if>
</h1>
</body>
</html>