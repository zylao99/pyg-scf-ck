<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemarker时间格式函数</title>
</head>
<body>
<h1>
    <ul>
        <li>现在时间：${today?time}</li>
        <li>现在日期：${today?date}</li>
        <li>日期时间：${today?datetime}</li>
        <li>时间格式化：${today?string('yyyy年MM月dd日')}</li>
    </ul>
</h1>
</body>
</html>