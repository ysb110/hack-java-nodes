<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
<#--<#assign user=Session.SPRING_SECURITY_CONTEXT.authentication.principal/>-->
    <title>商城首页</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            width: 100%;
            height: 100%;
        }

        .cover {
            display: flex;
            flex-direction: column;
            width: 100%;
            height: 100%;
        }

        .header {
            height: 100px;
            background-color: #e6e9ee;
            text-align: center;
            line-height: 100px;
        }

        .content {
            display: flex;
            flex: 1;
            flex-direction: row;
        }

        .left {
            width: 300px;
            background-color: darkgrey;
        }

        .core {
            flex: 1;
            height: 100%;
        }

        .bottom {
            height: 40px;
            width: 100%;
            text-align: center;
        }

        .login {
            position: fixed;
            right: 10px;
            top: 30px;
        }
    </style>
</head>
<body>
<div class="cover">
    <div class="header"><h1>商城首页</h1>
        <#if user.name == 'anonymous'>
        <a class="login" href="/user/index">登陆</a>
        <#else>
            <span class="login" style="right:80px;">${user.name}</span>
        <a class="login" href="/logout">退出</a>
        </#if>
    </div>
    <div class="content">
        <div class="left"></div>
        <div class="core"></div>
    </div>
    <div class="bottom"></div>
</div>
<script src="/js/jquery1.10.2.min.js"></script>
<script>
    function login() {

    }
</script>
</body>
</html>