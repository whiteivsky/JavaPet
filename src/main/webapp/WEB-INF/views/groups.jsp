<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp" %>
<title>Group page</title>

<form method="POST" action="/groups">

    <legend>Manage group list</legend>
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="row">
                <div class="col-lg-6">
                    <div class="input-group">
                        <input name="addGroupName" type="text" class="form-control">
                        <span class="input-group-btn">
                            <input type="submit" class="btn btn-default" value="Add"> new group and apply</input>
                        </span>
                    </div><!-- /input-group -->
                </div><!-- /.col-lg-6 -->
            </div><!-- /.row -->
        </div>
        <div class="panel-body">
            <table class="table">
                <tr>
                    <th>Groups</th>
                    <th>Users</th>
                </tr>
                <c:forEach var="grouplist" items="${groups}">
                    <tr>
                        <td>
                            <input type="checkbox" name="selectGroup" value="${grouplist.groupName}"
                                   id="${grouplist.groupName}id" ${grouplist.checked}/>
                            <label for="${grouplist.groupName}id">${grouplist.groupName}</label>
                        </td>
                        <td>
                            <c:forEach var="curUser" items="${grouplist.users}">
                                <c:choose>
                                    <c:when test="${pageContext.request.userPrincipal.name == curUser}">
                                        <span class="label label-primary">${curUser}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="label label-default">${curUser}</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>

            </table>
        </div>

        <div class="panel-footer">
            <input type="submit" name="changeGroupList" type="submit" class="btn btn-default" value="Press"> here to
            apply shanges!</input>
        </div>


    </div>

</form>
<!--Результат добавления групп-->
<br><br>
<br><br>
<c:forEach var="Applgroups" items="${Applgroups}">
    <c:out value="${Applgroups}"/>!!!<br>
</c:forEach>

<%@ include file="footer.jsp" %>