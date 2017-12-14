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
    	setTogether($('input:radio[name="ck_together_flag"]:checked').val());
    });
        
    function validate() {
        var msg = "";
        var flg = true;
        if($("#tel_number").val()==""){
            msg += "请填写计费号码<br/>";
            flg = false;
        } else if($("#tel_number").val().length>50){
            msg += "填写的计费号码字数不能超过50<br/>";
            flg = false;
        }
        if($("#dept_uuid").combobox("getValue")==""){
            msg += "请选择所属部门<br/>";
            flg = false;
        }
        $("#dept_name").val($("#dept_uuid").combobox("getText"));
        
        var valid_start_date;
        $('input[name="valid_start_date"]').each(function(){
        	valid_start_date = $(this).val();
        });
        
        if(valid_start_date==""){
            msg += "请填写开通时间<br/>";
            flg = false;
        }
        
        if($("#basic_cost").val()==""){
            msg += "请填写基本套餐资费<br/>";
            flg = false;
        }
        
        if($("#out_local_cost").val()=="" || $("#out_local_cycle").val()=="" || $("#out_local_min_cycle").val()==""){
            msg += "请填写完整的套餐外本地资费<br/>";
            flg = false;
        }
        var checkValue = $("#together_flag").val(); 
        if (checkValue == '0') {
	        if($("#out_long_cost").val()=="" || $("#out_long_cycle").val()=="" || $("#out_long_min_cycle").val()==""){
	            msg += "请填写完整的套餐外长途资费<br/>";
	            flg = false;
	        }
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

    function setTogether(flag) {
    	if(flag=='0'){
			$("#trLong").show();
			$("#together_flag").val("0");
		} else {
			$("#trLong").hide();
			$("#together_flag").val("1");
		}
    }
</script>

<form id="form-dialog" data-parsley-validate="">
    <input type="hidden" id="uuid" name="uuid" value="${telInfo.uuid}"/>
    <input type="hidden" id="together_flag" name="together_flag" value="${telInfo.together_flag}"/>
    <input type="hidden" id="dept_name" name="dept_name" value="${telInfo.dept_name}"/>
    <table class="table-layout tb1" style="width:100%">
        <tr>
            <td class="table-layout-title">
                <span class="required">计费号码</span>
            </td>
            <td >
                <input type="text" id="tel_number" name="tel_number" class="easyui-textbox" data-options="width:160,required:true" value="${telInfo.tel_number}"/>
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span class="required">所属部门</span>
            </td>
            <td >
                <select class="easyui-combobox" id="dept_uuid" name="dept_uuid" data-options="width:200,editable:false,panelHeight:'auto',validType:'minLength[1]',required:true">
                    <option value="" >请选择</option>
                    <c:forEach var="deptInfo" items="${deptInfoList}">
                        <option value="${deptInfo.uuid}" <c:if test="${deptInfo.uuid eq telInfo.dept_uuid}">selected="selected"</c:if> >${deptInfo.dept_name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span class="required">开通时间</span>
            </td>
            <td >
            <input id="valid_start_date" name="valid_start_date" type="text" value="${telInfo.valid_start_date}"  class="easyui-datebox" data-options="editable:false,width:100,required:true"/>
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span class="required">基本套餐资费</span>
            </td>
            <td >
            <input type="text" id="basic_cost" name="basic_cost" class="easyui-numberbox" min="0.01" value="10" max="1000" precision="2" data-options="width:150,required:true" value="${telInfo.basic_cost}"/>&nbsp;（元）
            </td>
        </tr>
       <tr>
            <td class="table-layout-title">
                <span class="required">套餐外本地资费</span>
            </td>
            <td colspan="1">
                <input type="text" id="out_local_cost" name="out_local_cost" class="easyui-numberbox" min="0.01" value="0.06" max="1000" precision="2" data-options="width:80,required:true" value="${telInfo.out_local_cost}"/>&nbsp;（元）
            	<span class="required">计费周期</span>&nbsp;&nbsp;
                <input type="text" id="out_local_cycle" name="out_local_cycle" class="easyui-numberbox" min="1" value="1" max="60" data-options="width:50,required:true" value="${telInfo.out_local_cycle}"/>&nbsp;秒&nbsp;&nbsp;
                <span class="required">最小计费周期</span>&nbsp;&nbsp;
               <input type="text" id="out_local_min_cycle" name="out_local_min_cycle" class="easyui-numberbox" min="1" value="6" max="60" data-options="width:50,required:true" value="${telInfo.out_local_min_cycle}"/>&nbsp;秒&nbsp;&nbsp;
            </td>
       </tr>
       <tr>
            <td class="table-layout-title">
                <span class="required">是否长市合一</span>
            </td>
            <td>
                <label><input type="radio"  name="ck_together_flag" value="1" <c:if test="${telInfo.together_flag eq '1'}">checked="checked"</c:if> onClick="setTogether('1')" /> 是</label>&nbsp;&nbsp;
                <label><input type="radio"  name="ck_together_flag" value="0" <c:if test="${telInfo.together_flag eq '0'}">checked="checked"</c:if> onClick="setTogether('0')" /> 否</label>
            </td>
        </tr>
       <tr id="trLong">
            <td class="table-layout-title">
                <span class="required">套餐外长途资费</span>
            </td>
            <td colspan="1">
               <input type="text" id="out_long_cost" name="out_long_cost" class="easyui-numberbox" min="0.01" value="0.06" max="1000" precision="2" data-options="width:80,required:true" value="${telInfo.out_long_cost}"/>&nbsp;（元）
               <span class="required">计费周期</span>&nbsp;&nbsp;
               <input type="text" id="out_long_cycle" name="out_long_cycle" class="easyui-numberbox" min="1" value="1" max="60"  data-options="width:50,required:true" value="${telInfo.out_long_cycle}"/>&nbsp;秒&nbsp;&nbsp;
               <span class="required">最小计费周期</span>&nbsp;&nbsp;
               <input type="text" id="out_long_min_cycle" name="out_long_min_cycle" class="easyui-numberbox" min="1" value="6" max="60" data-options="width:50,required:true" value="${telInfo.out_long_min_cycle}"/>&nbsp;秒&nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td class="table-layout-title">
                <span >运营商</span>
            </td>
            <td colspan="1">
                <select class="easyui-combobox" id="telec_operator_type" name="telec_operator_type" data-options="width:100,editable:false,panelHeight:'auto',validType:'minLength[1]'">
                    <option value="" >请选择</option>
                    <c:forEach var="telecOperator" items="${telecOperatorList}">
                        <option value="${telecOperator.gbcb_id}" <c:if test="${telecOperator.gbcb_id eq telInfo.telec_operator_type}">selected="selected"</c:if> >${telecOperator.gbcb_name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
		
		 <tr>
            <td class="table-layout-title">
                <span>备注</span>
            </td>
            <td colspan="1">
                <textarea style="height:50px;width:400px;" name="remark" id="remark">${telInfo.remark}</textarea>
            </td>
        </tr>
        
     </table>


</form>
</body>
</html>
