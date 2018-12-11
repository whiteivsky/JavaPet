<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<%@ include file="header.jsp" %>

<form method="POST" action="${contextPath}/login" class="form-signin" role="form" accept-charset=utf-8>
    <h2 class="form-signin-heading ">Log in</h2>

    <div class="form-group ${error != null ? 'has-error' : ''}">
        <span>${message}</span><br>
        <input name="username" type="text" class="form-control" placeholder="Username"
               autofocus="true"/><br>
        <input name="password" type="password" class="form-control" placeholder="Password"/><br>
        <span>${error}</span><br>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
    </div>
</form>
<%@ include file="footer.jsp" %>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>