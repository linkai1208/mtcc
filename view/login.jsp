<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();
	String error = (String)request.getSession().getAttribute("errormessage");
	if (error == null) error = "";
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include flush="true" page="/view/common/resource.jsp"></jsp:include>
    <link href="<%=path%>/includes/styles/login.css" rel="stylesheet">
    <style type="text/css">
	    .errLabel {
			font-size: 12px;
			color: #FF0000;
			text-align: left;;
			font-family: SimSun;
		}
	</style>
    <title>移动话费清单校对系统-用户登录</title>
    <script type="text/javascript">
        $(function() {
            //取得当前日期
            var myDate = new Date();
            var year = myDate.getFullYear();
            var month = myDate.getMonth() + 1 >= 10 ? myDate.getMonth() + 1 : "0" + (myDate.getMonth() + 1);
            var day = myDate.getDate() >= 10 ? myDate.getDate() : "0" + myDate.getDate();
            var week = myDate.getDay();

            var weekStr = "";
            if (week == 0) {
                weekStr = "星期日";
            } else if (week == 1) {
                weekStr = "星期一";
            } else if (week == 2) {
                weekStr = "星期二";
            } else if (week == 3) {
                weekStr = "星期三";
            } else if (week == 4) {
                weekStr = "星期四";
            } else if (week == 5) {
                weekStr = "星期五";
            } else if (week == 6) {
                weekStr = "星期六";
            }

            var str = year + "年" + month + "月" + day + "日" + " " + weekStr;
            $(".titleTime").html(str);
        })
    </script>

</head>
<body class="form-bg">
<div class="header">
    <span class="title">移动话费清单校对系统</span>
    <span class="titleTime"></span>
</div>
<div class="container" style="padding-top: 34px;padding-bottom: 34px">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
            <form class="form-horizontal" id="loginForm" name="loginForm" action="<%=path%>/enter" method="post">
                <span class="heading"></span>
                <div class="form-group">
                    <input type="text" class="form-control" id="user" name="user" placeholder="用户名">
                    <i class="glyphicon glyphicon-user"></i>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" id="password" name="password" placeholder="密　码" autocomplete="off">
                    <i class="glyphicon glyphicon-lock"></i>
                    <div>
		     			<span class="errLabel"><%=error %></span>
			  		</div>
                </div>
                
                <div class="form-group">
                    <button class="btn btn-primary" type="submit" id="loginBtn" >登录</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="nc_bottom">
</div>

</body>
</html>
