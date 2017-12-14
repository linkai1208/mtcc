<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include flush="true" page="common/resource.jsp"></jsp:include>

    <script>
        $(function() {
            var type = '${type}';
            if (type == "1") {
                $("#policyFiles").show();
                $("#systemFiles").hide();
            } else if (type == "2") {
                $("#policyFiles").hide();
                $("#systemFiles").show();
            }
        })
    </script>
</head>
<body style="padding-top: 10px">
    <legend>文件下载</legend>
    <div class="form-group" id="policyFiles" style="display: none;padding-left: 10px">
        <a href="<%=path%>/download/3">
            民用无人驾驶航空器实名制登记管理规定<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
        <a href="<%=path%>/download/4">
            民用无人机驾驶员管理规定<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
        <a href="<%=path%>/download/5">
            轻小无人机运行规定（试行）<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
        <a href="<%=path%>/download/6">
            民用无人驾驶航空器系统空中交通管理办法<span class="glyphicon glyphicon-download-alt"></span>
        </a>
    </div>
    <div class="form-group" id="systemFiles" style="display: none;padding-left: 10px">
        <a href="<%=path%>/download/0">
            系统常见问题汇总<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
        <a href="<%=path%>/download/1">
            无人机厂商产品登记指南<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
        <a href="<%=path%>/download/2">
            无人机用户登记指南<span class="glyphicon glyphicon-download-alt"></span>
        </a><br>
    </div>
</body>
</html>
