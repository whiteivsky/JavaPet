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
<legend>Download files</legend>
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">Download list</div>
    <div class="tooltip tooltip-top" role="tooltip">
        <div class="tooltip-arrow"></div>
        <div class="tooltip-inner">
            Some tooltip text!
        </div>
    </div>
    <c:choose>
        <c:when test="${attaches.size() == 0}">
            <div class="panel-body">
                <p>Download list is empty, <a href="/upload">upload</a> some files or <a href="/groups">add group</a>
                </p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="panel-body">
                <c:if test="${attaches.size() != 0}">
                    <c:if test="${pageContext.request.userPrincipal.name != null}">
                        <table class="table">
                            <tr>
                                <th></th>
                                <th>File name</th>
                                <th>upload date</th>
                                <th>owner</th>
                                <th>size</th>
                            </tr>
                            <c:forEach var="attaches" items="${attaches}">
                                <tr>
                                    <td><img src="data:image/png;base64,${attaches.fileFormat.icon}" width="19"
                                             height="19" alt="img"/></td>
                                    <td><a href="<c:url value='/download/${attaches.id}' />"
                                           data-toggle="tooltip" title="${attaches.description}" data-placement="right"
                                           style="white-space: nowrap;">
                                            ${attaches.fileName}</a>
                                    </td>
                                    <td>${attaches.uploadDate.toLocaleString()}</td>
                                    <td><span class="label label-info">${attaches.owner.userName}</span></td>
                                    <td>${attaches.formatSize}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script src="https://github.com/HubSpot/tether/blob/master/dist/js/tether.min.js"></script>
<%@ include file="footer.jsp" %>
