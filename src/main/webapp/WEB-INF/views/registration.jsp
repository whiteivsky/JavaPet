<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<%@ include file="header.jsp" %>
<h2>Create a new user</h2>

<div>
    <form method="post" action="${contextPath}/registration">
        <input name="userName" placeholder="Enter user name" required><br>
        <input name="email" placeholder="Enter email" required><br>
        <input name="password" type = "password" placeholder="Enter password" required><br>
        <input name="confirmPassword" type = "password" placeholder="Confirm the password" required><br>
        <button type="submit">Submit</button>
    </form>
    <br>
    ${registrationStatus}
</div>

<%@ include file="footer.jsp" %>