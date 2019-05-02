<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>内建函数-eval</title>
    <#assign userinfo = '{"name":"八戒","age":10000000,"address":"天河"}'/>
    <#assign user = userinfo?eval/>
</head>
<body>
<h1>
    ${user.name}<br>
    ${user.age}<br>
    ${user.address}<br>
</h1>
</body>
</html>