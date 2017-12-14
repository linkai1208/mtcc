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
    function save(flag) {
    	if(flag == "list"){
			if(validate("list")){
		        var jsonObj = window.$('#form-dialog').serializeArray();
		        $.ajax({
		            type:'post',
		            url:'<%=path%>/uploadTel/saveOrUpdateListfile',
		            data:jsonObj,
		            dataType:'json',
		            loadMsg: constant.Loading,
		            beforeSend:function(XMLHttpRequest){
		            	ajaxLoading();
		            },
		            success:function(data){
		                if(data.result=="true"){
		                	window.top.$.messager.alert('提示', "导入成功。");
		                }else{
		                    window.top.$.messager.alert('提示', "导入失败："+data.message);
		                }
		            },
		            complete:function(XMLHttpRequest,textStatus){
		                ajaxLoadEnd();
		            },
		            error:function(XMLHttpRequest,textStatus,errorThrown){
		                window.top.$.messager.alert('提示', "提交异常，请重新填写");
		            }
		        });
			}
    	} 
    	if(flag == "detail"){
			if(validate("detail")){
		        var jsonObj = window.$('#form-dialog').serializeArray();
		        $.ajax({
		            type:'post',
		            url:'<%=path%>/uploadTel/saveOrUpdateDetailfile',
		            data:jsonObj,
		            dataType:'json',
		            loadMsg: constant.Loading,
		            beforeSend:function(XMLHttpRequest){
		            	ajaxLoading();
		            },
		            success:function(data){
		                if(data.result=="true"){
		                	window.top.$.messager.alert('提示', "导入成功。");
		                }else{
		                    window.top.$.messager.alert('提示', "导入失败："+data.message);
		                }
		            },
		            complete:function(XMLHttpRequest,textStatus){
		                ajaxLoadEnd();
		            },
		            error:function(XMLHttpRequest,textStatus,errorThrown){
		                window.top.$.messager.alert('提示', "提交异常，请重新填写");
		            }
		        });
			}
    	} 
	}
    
    function validate(flag) {
    	if (flag == "list") {
			$('input[data-category="list_doc"]').each(function(){
				$("#list_file_id").val($(this).val());
			});
    	}
    	if (flag == "detail") {
			$('input[data-category="detail_doc"]').each(function(){
				$("#detail_file_id").val($(this).val());
			});
    	}
		
		var msg = "";
		
		if(msg!=""){
			window.top.$.messager.alert('提示', msg);
			return false;
		} else {
			return true;
		}
	}
    function ajaxLoading(){
        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
    }
    function ajaxLoadEnd(){
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }
    
    function openDownloadPage(type) {
        window.open("<%=path%>/download/downloadFile/" +type,"文件下载","height=500,width=500,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no");
    }
    
 </script>

<form id="form-dialog" data-parsley-validate="">
<input type="hidden" name="uuid" id="uuid" value="">
<input type="hidden" name="list_file_id" id="list_file_id" value="">
<input type="hidden" name="detail_file_id" id="detail_file_id" value="">
	<div style="margin-left:20px;">
		<span><input id="period_year" name="period_year" class="easyui-numberspinner" data-options="increment:1,width:100" value="${period_year }"/>&nbsp;&nbsp;年</span>&nbsp;&nbsp;
		<span><input id="period_month" name="period_month" class="easyui-numberspinner" data-options="increment:1,min:01,max:12,width:80" value="${period_month }"/>&nbsp;&nbsp;月</span>
    </div>
    <div style="margin-top:20px;">
    <table class="table-layout tb1" style="width:100%">
    	<tr>
            <td class="table-layout-title">
                <span class="required">清单模板文件</span>
            </td>
            <td>
            <a href="javascript:openDownloadPage(1)" >话费清单模板</a>
            </td>
        </tr>        
    	<tr>
            <td class="table-layout-title">
                <span class="required">清单上传</span>
            </td>
            <td >
				<jsp:include flush="true" page="/view/common/uploadfile.jsp">
					<jsp:param name="foreignid" value="${uuid}" />
					<jsp:param name="category" value="list_doc" />
					<jsp:param name="multiple" value="false" />
					<jsp:param name="allowedExtensions" value="['xls', 'xlsx']" />
					<jsp:param name="sizeLimit" value="100000000" />
					<jsp:param name="list" value="${list}" />
					<jsp:param name="readOnly" value="false" />
					<jsp:param name="onComplete" value="" />
				</jsp:include>
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span class="required">详单模板文件</span>
            </td>
            <td>
            <a href="javascript:openDownloadPage(2)" >话费详单模板</a>
            </td>
        </tr>   
		<tr>
            <td class="table-layout-title">
                <span class="required">详单上传</span>
            </td>
            <td >
				<jsp:include flush="true" page="/view/common/uploadfile.jsp">
					<jsp:param name="foreignid" value="${uuid}" />
					<jsp:param name="category" value="detail_doc" />
					<jsp:param name="multiple" value="false" />
					<jsp:param name="allowedExtensions" value="['xls', 'xlsx']" />
					<jsp:param name="sizeLimit" value="100000000" />
					<jsp:param name="list" value="${list}" />
					<jsp:param name="readOnly" value="false" />
					<jsp:param name="onComplete" value="" />
				</jsp:include>
            </td>
        </tr>
     </table>
     </div>
<div data-options="region:'south',border:false" style="height:40px; width:100%; background:#eee; text-align:center; padding:2px;">
    <div style="margin:5px 0">
	<a href="#" class="easyui-linkbutton c5 easyui-linkbutton-primary l-btn l-btn-small" style="width:120px" id="btn_saveList" onClick="save('list')">清单导入</a>&nbsp;&nbsp;
	<a href="#" class="easyui-linkbutton c5 easyui-linkbutton-primary l-btn l-btn-small" style="width:120px" id="btn_saveDetail"  onClick="save('detail')">详单导入</a>
	</div>
</div>

</form>
</body>
</html>
