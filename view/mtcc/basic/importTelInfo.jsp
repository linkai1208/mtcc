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

    function validate() {
        var msg = "";
        var flg = true;
        $('input[data-category="tel_info"]').each(function(){
			$("#attach_field").val($(this).val());
		});

        if($("#attach_field").val()==""){
            msg += "请先上传导入文件<br/>";
            flg = false;
        }
        
        if(!flg){
            window.top.$.messager.alert('提示', msg);
            return flg;
        }
        return flg;
    }
    function openDownloadPage(type) {
        window.open("<%=path%>/download/downloadFile/" +type,"文件下载","height=500,width=500,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no");
    }
 </script>

<form id="form-dialog" data-parsley-validate="">
    <input type="hidden" name="attach_field" id="attach_field" value="">
    <table class="table-layout tb1" style="width:350px">
        <tr>
            <td class="table-layout-title">
                <span class="required">模板文件</span>
            </td>
            <td>
            <a href="javascript:openDownloadPage(3)" >计费号码导入模板</a>
            </td>
        </tr>        
        <tr>
            <td class="table-layout-title">
                <span class="required">上传附件</span>
            </td>
            <td>
                <%-- 参数说明
				   foreignid 业务表主键
				   category 同一个页面多个上传组件用于区分哪个业务分类
				   multiple 是否允许上传多个文件
				   allowedExtensions 允许的扩展名
				   sizeLimit 上传单个文件的
				   list 已上传的附件数据
						通过后台 java 代码 mv.addObject("list", list) 赋值,
						include 页面通过 getAttribute 获取, 所以这里是否赋值都可以
						一个页面有多个上传组件, list 可以包含所有数据, 通过 foreignid、category 区分是那部分的数据
				   readOnly 是否只读查看
				   onComplete 回调函数,每个文件上传后都会触发
				   --%>
				<jsp:include flush="true" page="/view/common/uploadfile.jsp">
					<jsp:param name="foreignid" value="${uuid}" />
					<jsp:param name="category" value="tel_info" />
					<jsp:param name="multiple" value="false" />
					<jsp:param name="allowedExtensions" value="['xls', 'xlsx']" />
					<jsp:param name="sizeLimit" value="50000000" />
					<jsp:param name="list" value="${list}" />
					<jsp:param name="readOnly" value="false" />
					<jsp:param name="onComplete" value="" />
				</jsp:include>
            </td>
        </tr>
     </table>


</form>
</body>
</html>
