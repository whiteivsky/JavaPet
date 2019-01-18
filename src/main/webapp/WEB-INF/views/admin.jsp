<%@ include file="header.jsp" %>

<div class="panel panel-default">
    <div class="panel-heading">Icons</div>
    <div class="panel-body">
        <div class="input-group">
            <form method="POST" enctype="multipart/form-data" action="${contextPath}/uploadIcons">

                <div class="btn-group">
                    <label class="btn btn-default">
                    <span class="glyphicon glyphicon glyphicon-plus">
                        <input type="file" name="newIcons" accept=".png" multiple style="display: none;">
                    </span>Select
                    </label>
                    <label class="btn btn-primary">Upload
                        <span class="glyphicon glyphicon-upload">
                            <input type="submit" value="Upload" style="display: none;">
                        </span>
                    </label>
                </div>
                <br>
                <div class="container">
                    <div class="row">
                        <c:forEach var="uploadStatuses" items="${uploadStatuses}">
                    <span class="label label-info">
                            ${uploadStatuses}
                    </span>
                        </c:forEach>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="panel-footer">
        <FORM NAME="deleteForm" METHOD="POST" action="${contextPath}/deleteIcons">
            <INPUT TYPE="hidden" name="iconForDelete" id="iconForDelete">
            <div class="row">
                <c:forEach var="icons" items="${icons}">
                    <div class="col-sm-1 col-md-1" style="padding-left: 5px; padding-right: 5px;">
                        <div class="thumbnail">
                            <img src="data:image/png;base64,${icons.icon}" width="46"
                                 height="46" alt="img"/>
                            <div class="caption">
                                <h4>*.${icons.name}</h4>
                            </div>
                            <SCRIPT LANGUAGE="JavaScript">
                                function deleteIconByName(iconName) {
                                    document.getElementById("iconForDelete").value = iconName;
                                    var user = document.getElementById("iconForDelete").value;
                                    var x = document.getElementsByName('deleteForm');
                                    x[0].submit();
                                    return "";
                                }
                            </SCRIPT>
                            <button type="button" class="btn btn-block btn-primary"
                                    ONCLICK="deleteIconByName('${icons.name}');">
                                <span class="glyphicon  glyphicon glyphicon-trash"></span>
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </FORM>
    </div>
</div>
<%@ include file="footer.jsp" %>
