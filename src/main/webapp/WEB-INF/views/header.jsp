<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<sec:authentication var="user" property="principal"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <!--<meta charset="utf-8">-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/signin.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%--<%@ include file="header.jsp" %>--%>
<div class="container">
    <div class="navbar navbar-default" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <A class="navbar-brand" HREF="/">JavaPet</a>
            </div>

            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <div class="collapse navbar-collapse ">
                    <ul class="nav navbar-nav navbar-left">
                        <li ${isActiveDownload}><A HREF="download">home</a></li>
                        <li ${isActiveUpload}><A HREF="upload">upload</a></li>
                        <li ${isActiveGroup}><A HREF="groups">groups</a></li>
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <li ${isActiveAdmin}><A HREF="admin">admin</a></li>
                        </sec:authorize>
                    </ul>
                    <div class="navbar-right">
                        <ul class="nav nav nav-pills navbar-nav navbar-left">
                            <p class="navbar-text">You signed in as ${pageContext.request.userPrincipal.name}</p>
                            <li class="active"><A HREF="/logout">out</a></li>
                                <%--<button type="button" class="btn btn-default navbar-btn" onClick='location.href="/logout"'>out</button>--%>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:if test="${pageContext.request.userPrincipal.name == null}">
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><A HREF="/login">login</a></li>
                        <li><A HREF="/registration">registration</a></li>
                    </ul>
                </div>
            </c:if>
        </div>
    </div>

    <%--<c:if test="${registrationStatus != null}">${registrationStatus}</c:if>--%>
