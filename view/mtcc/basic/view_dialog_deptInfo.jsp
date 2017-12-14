<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.sten.mtcc.model.BiTelInfo" %>
<%
    String path = request.getContextPath();

%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include flush="true" page="/view/common/resource.jsp"></jsp:include>
</head>
<body class="body">
<style>
    textarea {
        position: relative;
        border: 1px solid #D4D4D4;
        /*background-color: #CCE8CF;*/
        vertical-align: middle;
        display: inline-block;
        white-space: pre-wrap;
        margin: 0;
        padding: 0;
        -moz-border-radius: 5px 5px 5px 5px;
        -webkit-border-radius: 5px 5px 5px 5px;
        border-radius: 5px 5px 5px 5px;
        font-size:13px;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
    });
        
    function validate() {
        var msg = "";
        var flg = true;
        
        if($("#dept_name").val()==""){
            msg += "请填写部门名称<br/>";
            flg = false;
        } else if($("#dept_name").val().length>100){
            msg += "填写的部门名称字数不能超过100<br/>";
            flg = false;
        }
        
        if($("#dept_address").val().length>2000){
            msg += "填写的办公地址字数不能超过2000<br/>";
            flg = false;
        }
        
        var content = $("#remark").val();
        if(content!=null&&content.length>500){
            msg += "填写的备注字数不能超过500<br/>";
            flg = false;
        }
        if(!flg){
            window.top.$.messager.alert('提示', msg);
            return flg;
        }
        return flg;
    }

</script>

<form id="form-dialog" data-parsley-validate="">
    <input type="hidden" id="uuid" name="uuid" value="${deptInfo.uuid}"/>
    <table class="table-layout tb1" style="width:100%">
        <tr>
            <td class="table-layout-title">
                <span class="required">部门名称</span>
            </td>
            <td >
                <input type="text" id="dept_name" name="dept_name" class="easyui-textbox" data-options="width:160,required:true" value="${deptInfo.dept_name}"/>
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span class="required">办公地址</span>
            </td>
            <td >
                <input type="text" id="dept_address" name="dept_address" class="easyui-textbox" data-options="width:160,required:true" value="${deptInfo.dept_address}"/>
            </td>
        </tr>
		 <tr>
            <td class="table-layout-title">
                <span>备注</span>
            </td>
            <td colspan="1">
                <textarea style="height:50px;width:400px;" name="remark" id="remark">${deptInfo.remark}</textarea>
            </td>
        </tr>
        
     </table>


</form>
</body>
</html>
