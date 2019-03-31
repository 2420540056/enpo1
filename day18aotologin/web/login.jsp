<%@ page import="com.itheima.login.utils.CookieUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script>
        window.onload = function () {
            document.getElementById("img1").onclick=function () {
                this.src = "/login/checkImg?time="+new Date().getTime();
            }
        }
    </script>

</head>
<body>

    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
        用户名:<input type="text" name="username" placeholder="请输入用户名" value="${cookie.remember.value}"/><br>
        密&nbsp;&nbsp;&nbsp;码:<input type="text" name="password" placeholder="请输入密码"/><br>
      <%--  验证码:<input type="text" name="chcekcode" /><img id="img1" src="/login/checkImg" /><br>--%>
        <input type="checkbox" name="remember" ${cookie.remember.value==null?'':'checked'}/>记住用户名<input type="checkbox" name="autoLogin"/>自动登陆<br>
        <input type="submit" value="登陆">
    </form>
    <br>
    <font color="red">${msg_error}</font>
</body>
</html>