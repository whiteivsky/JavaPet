<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>



<%@ include file="header.jsp" %>

<title>Registration page</title>
<legend>Create a new user account</legend>

<form  method="post" action="${contextPath}/registration">
    <div class="panel panel-default">
        <div class="panel-heading"> Input registration data</div>
        <div class="panel-body">
            <input  class="form-control" name="userName" placeholder="Enter user name" required><br>
            <input  class="form-control" name="email" placeholder="Enter email" required><br>
            <input  class="form-control" name="password" type="password" placeholder="Enter password" required><br>
            <input  class="form-control" name="confirmPassword" type="password" placeholder="Confirm the password" required><br>
        </div>
        <div class="panel-footer">
            <!--<button type="submit">Submit</button>-->
            <input type="submit" class="btn btn-default" value="registration">
        </div>
    </div>
    ${registrationStatus}
</form>


<%@ include file="footer.jsp" %>