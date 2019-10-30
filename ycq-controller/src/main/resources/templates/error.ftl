<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>自定义错误页面</title>
</head>
<body>
<h1>status:[[${status}]]</h1>
<h2>timestamp:[[${timestamp?string('yyyy.MM.dd HH:mm:ss')}]]</h2>
<h2>exception:[[${error}]]</h2>
<h2>message:[[${message}]]</h2>
</body>
</html>