<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String path = request.getContextPath();

%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include flush="true" page="/view/common/resource.jsp"></jsp:include>
    
</head>
<body class="body">
<script type="text/javascript">
    $(document).ready(function () {

    });
    function validate(flag) {
		return true;
	}
    function ajaxLoading(){
        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
    }
    function ajaxLoadEnd(){
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }
    
 </script>

<form id="form-dialog" data-parsley-validate="">
	<div style="margin-left:20px;margin-top:20px;">
		开始执行预处理：${period_year }&nbsp;&nbsp;年&nbsp;&nbsp;${period_month }&nbsp;&nbsp;月
    </div>
</form>
</body>
</html>
