<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
     <c:if test="${not empty existUser}">
         欢迎您：${existUser.nickname}
     </c:if>
     <c:if test="${empty existUser}">
         您还没有登陆，请先去<a href="${pageContext.request.contextPath}/login.jsp">登陆</a>
     </c:if>
  </body>
</html>
