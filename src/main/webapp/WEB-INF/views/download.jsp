<%--
  Created by IntelliJ IDEA.
  User: ruabyia
  Date: 15.10.2018
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="header.jsp" %>
<title>Download page</title>

<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">Download list</div>

    <c:choose>
        <c:when test="${attaches.size() == 0}">
            <div class="panel-body">
                <p>Download list is empty, <a href="/upload">upload</a> some files or <a href="/groups">add group</a></p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel-body">
                <c:if test="${attaches.size() != 0}">
                    <c:if test="${pageContext.request.userPrincipal.name != null}">
                        <table class="table">
                            <tr>
                                <th>File name</th>
                                <th>size</th>
                                <th>upload date</th>
                                <th>owner login</th>
                                <th>description</th>
                                <th>icon</th>
                            </tr>
                            <c:forEach var="attaches" items="${attaches}">
                                <tr>
                                    <td><a href="<c:url value='/download/${attaches.id}' />">${attaches.fileName}</a>
                                    </td>
                                    <td>${attaches.size}</td>
                                    <td>${attaches.uploadDate.toLocaleString()}</td>
                                    <td>${attaches.owner.userName}</td>
                                    <td>${attaches.description}</td>
                                    <td><img src="data:image/png;base64,${attaches.fileFormat.icon}" width="19"
                                             height="19" alt="img"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>


</div>


<%@ include file="footer.jsp" %>
