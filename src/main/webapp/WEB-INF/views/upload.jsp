<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ruacom4
  Date: 04.10.2018
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>


<%@ include file="header.jsp" %>
<title>Upload page</title>
<legend>Uploading files</legend>
<form method="POST" enctype="multipart/form-data" action="${contextPath}/upload">
    <div class="panel panel-default">
        <div class="panel-heading">

            <h3 class="panel-title">Select files to upload and if you need - input description</h3>
            <table class="table">
                <tr>
                    <td>
                        <label class="btn btn-block btn-primary">
                            Browse&hellip; <input type="file" accept="${formats}" multiple name="fileData"
                                                  id="uploadFormControlFile1"
                                                  style="display: none;">
                        </label>
                        <script>
                            var label = document.querySelector("input");
                            label.addEventListener("change", function () {
                                var badge = document.getElementById("badge1");
                                badge.innerHTML = label.files.length;
                            });

                        </script>
                    </td>
                    <td>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-font"></span>
                            </span>
                            <input type="text" name="filedescription" class="form-control"
                                   placeholder="Description">
                        </div>
                    </td>
                </tr>
            </table>
        </div>

        <div class="panel-body">
            <input type="submit" class="btn btn-default"
                   value="Upload"> Press here to upload <span class="badge" id="badge1"></span>files!
        </div>

        <div class="panel-footer">
            <c:forEach var="uploadStatuses" items="${uploadStatuses}">
                <!-- Тут нужно вставить обработчик статусов для парсинга дизайна-->
                <c:if test="${uploadStatuses.substring(0,7)=='success'}">
                    <div class="alert alert-success">${uploadStatuses.substring(7)}</div>
                </c:if>
                <c:if test="${uploadStatuses.substring(0,13)=='warning_empty'}">
                    <div class="alert alert-warning">${uploadStatuses.substring(13)}</div>
                </c:if>
                <c:if test="${uploadStatuses.substring(0,20)=='warning_wrong_format'}">
                    <div class="alert alert-danger">${uploadStatuses.substring(20)}</div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</form>

</div>

<%@ include file="footer.jsp" %>
