<%--
  Created by IntelliJ IDEA.
  User: ruabyia
  Date: 11.12.2018
  Time: 20:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<%@ include file="header.jsp" %>

<body>
<div id="notfound">

    <p> timestamp: ${timestamp} </p>
    <p> status: ${status} </p>
    <p> error: ${error} </p>
    <p> message: ${message} </p>
    <p> path: ${path} </p>
    <h2>Oops! This OUR Page</h2>
</div>

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-23581568-13"></script>
<script>
    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }

    gtag('js', new Date());

    gtag('config', 'UA-23581568-13');
</script>
</body><!-- This templates was made by Colorlib (https://colorlib.com) -->

</html>
