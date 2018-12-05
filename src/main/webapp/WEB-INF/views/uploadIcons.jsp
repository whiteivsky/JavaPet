<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="header.jsp" %>
<meta charset="utf-8">
<form method="POST" enctype="multipart/form-data" action="${contextPath}/uploadIcons">
    Icons for import: <input type="file" name="icons" accept=".png" multiple> <br/>
    <br/>
    <input type="submit" value="Upload"> Press here to upload the icons!

    <br>
    <c:forEach var="uploadStatuses" items="${uploadStatuses}">
        <br><br>
        ${uploadStatuses}
    </c:forEach>
</form>
<%@ include file="footer.jsp" %>
