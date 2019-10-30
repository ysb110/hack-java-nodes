<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
>
<head>
    <title>Spring Security Example </title>
</head>
<body>
<form action="/login" method="post">
    <div><label> 用户名 : <input type="text" name="username" style="width:30%;height:100px;" value="ycq"/> </label>
    </div>
    <div><label> 密 码 : <input type="password" name="password" style="width:30%;height:100px;" value="123456"/> </label>
    </div>
<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
    <div><input type="submit" value="登录" style="width:30%;height:100px;"/></div>
</form>
</body>
</html>