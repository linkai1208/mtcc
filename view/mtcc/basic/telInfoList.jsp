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
        
    <script type="text/javascript">
        $(document).ready(function () {
            $('#grid').datagrid({
                nowrap: false,
                striped: false,
                url: '<%=path%>/telInfo/view/telInfoList/load',
                fitColumns: true,
                resizable: true,
                singleSelect: true,
                idField: 'uuid',
                sortName: '',
                sortOrder: '',
                remoteSort: true,
                pagination: true,
                pageNumber: 1,
                pageSize: 10,
                pageList: [10, 20, 50, 100],
                loadMsg: constant.Loading,
                columns: [[
                    { field: 'index', title: '序号', width: '50px', rowspan:2,align:'center',
                        formatter: function (value, row, index) {
                            return index+1;
                        }
                    },
                    { field: 'tel_number', title: '计费号码', width: '100px', rowspan:2,sortable: true, align:'center'},
                    { field: 'dept_name', title: '所属部门', width: '180px', rowspan:2,sortable: true, align:'center'},
                    { field: 'valid_start_date', title: '开通时间', rowspan:2, sortable: true, width:'100px',align:'center'},
                    { field: 'basic_cost', title: '基本套餐资费', rowspan:2, align:'center',
						formatter: function (value, row, index) {
							return value+"元";
						}
					},
                    { title: '套餐外本地资费', colspan:2},
                    { title: '套餐外长途资费', colspan:2},
                    { field: 'telec_operator_type', title: '运营商',width: '100px',  rowspan:2,sortable: true, align:'center'},
                    { field: 'cz', title: '操作', rowspan:2,align:'left',
                        formatter: function (value, row, index) {
                            var uuid = row.uuid;
                            return '<a href="#" onclick="editTelInfo(\''+uuid+'\')">修改</a>&nbsp;&nbsp;' +
                            '<a href="#" onclick="deleteTelInfo(\''+uuid+'\')">删除</a>';
                        }
                    }
                ],
                [
					{ field: 'out_local_cost', title: '资费', width: '100px',  align:'center',
						formatter: function (value, row, index) {
							var out_local_cost= row.out_local_cost;
							var out_local_cycle= row.out_local_cycle;
							var out_local_cycle_unit= "秒";
							if (out_local_cost != "") {
								if (out_local_cycle == "1") {
									return out_local_cost+ "元/"+out_local_cycle_unit;
								} else {
									return out_local_cost+ "元/"+out_local_cycle+out_local_cycle_unit;
								}
							} else {
								return "";
							}
					    }	
					},
					{ field: 'out_local_min_cycle', title: '最小计费周期', width: '100px',  align:'center',
						formatter: function (value, row, index) {
							var out_local_min_cycle= row.out_local_min_cycle;
							var out_local_min_cycle_unit= "秒";

							if (out_local_min_cycle != "") {
								return out_local_min_cycle+out_local_min_cycle_unit;
							} else {
								return "";
							}
					    }	
					},
					{ field: 'out_long_cost', title: '资费',width: '100px',  align:'center',
						formatter: function (value, row, index) {
							var out_long_cost= row.out_long_cost;
							var out_long_cycle= row.out_long_cycle;
							var out_long_cycle_unit= "秒";
							var together_flag = row.together_flag;
							if(together_flag == "0") {
								if (out_long_cost != "") {
									if (out_long_cycle == "1") {
										return out_long_cost+ "元/"+out_long_cycle_unit;
									} else {
										return out_long_cost+ "元/"+out_long_cycle+out_long_cycle_unit;
									}
								} else {
									return "";
								}
							} else {
								return "长市合一";
							}
					    }	
					},
					{ field: 'out_long_min_cycle', title: '最小计费周期',width: '100px',  align:'center',
						formatter: function (value, row, index) {
							var out_long_min_cycle= row.out_long_min_cycle;
							var out_long_min_cycle_unit= "秒";

							if (out_long_min_cycle != "") {
								return out_long_min_cycle+out_long_min_cycle_unit;
							} else {
								return "";
							}
					    }	
					}
				]],
				toolbar: [
		                    {
		                        id: 'btnAdd',
		                        text: '新增',
		                        iconCls: 'icon-add',
		                        handler: function () {
		                            addTelInfo();
		                        }
		                    },
		                    {
		                        id: 'btnImport',
		                        text: '导入',
		                        iconCls: 'icon-import',
		                        handler: function () {
		                        	imortTelInfo();
		                        }
		                    }
		                ],
                onBeforeLoad: function (param) {
                    attachParams(param);
                },
                onLoadSuccess: function (data) {
                	$('table.datagrid-htable').find('.datagrid-cell').css("text-align", 'center'); 
                    var rowData = data.rows;
                    $.each(rowData, function(index, row){
                    });
                }
            });
        });

        function attachParams(param) {
            var jsonObj = $('#form-search').serializeArray();
            var PageCount = {};
            PageCount.name = 'PageCount';
            PageCount.value = $('#grid').datagrid('getPager').data('pagination').options.total;
            jsonObj.push(PageCount);

            var QueryKey = {};
            QueryKey.name = 'QueryKey';
            QueryKey.value = $('#grid').datagrid('getData').QueryKey;
            jsonObj.push(QueryKey);

            var jsonStr = $.toJSON(jsonObj);
            param.query = jsonStr;

        };

        function reloadGrid() {
            $('#grid').datagrid('reload');
            return false;
        };

        //采用jquery easyui loading css效果
        function ajaxLoading(){
            $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
        }
        function ajaxLoadEnd(){
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }
        
        var _topWindow;
        function imortTelInfo(){
            _topWindow = window.top.$('#topWindow');
            if (_topWindow.length <= 0){
                _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
            }

            //width:350 650 850  height:450
            _topWindow.dialog({
                title: '导入计费号码',
                href: '<%=path%>/telInfo/view/importTelInfo',
                width: 500,
                height: 300,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                cache: false,
                modal: true,
                closed: false,
                buttons: [{
                    text: '号码导入',
                    handler: function () {

                    	if(!window.top.parent.validate()) return;
                        var jsonObj = window.top.$('#form-dialog').serializeArray();
                        $.ajax({
                            type:'post',
                            url:'<%=path%>/telInfo/importData',
                            data:jsonObj,
                            dataType:'json',
                            loadMsg: constant.Loading,
                            beforeSend:function(XMLHttpRequest){
                            	ajaxLoading();
                            },
                            success:function(data){
                                if(data.result=="true"){
                                    window.top.$.messager.alert('提示', "导入成功");
                                    reloadGrid();
                                    _topWindow.dialog('close');
                                }else{
                                    window.top.$.messager.alert('提示', data.message);
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
                },
                    {
                        text: '取消',
                        handler: function () {
                            _topWindow.dialog('close');
                        }
                    }],
                onClose: function () {
                    _topWindow.window('destroy');
                }
            });                
        }
        
        function deleteTelInfo(uuid){
            $.messager.confirm("操作提示", "确定删除？", function (data) {
                if(data){
                    $.ajax({
                        type: 'post',
                        url: '<%=path%>/telInfo/deleteById',
                        data: {id: uuid},
                        dataType: 'json',
                        success: function (data) {
                            if(data.result=="true"){
                                window.top.$.messager.alert('提示', '删除成功！');
                                $('#grid').datagrid('reload');
                            }else{
                                window.top.$.messager.alert('提示', '操作失败！');
                                $('#grid').datagrid('reload');
                            }
                        }
                    });
                }
            });
        }
        function addTelInfo(){
            _topWindow = window.top.$('#topWindow');
            if (_topWindow.length <= 0){
                _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
            }

            //width:350 650 850  height:450
            _topWindow.dialog({
                title: '新增号码',
                href: '<%=path%>/telInfo/view/addModel',
                width: 700,
                height: 450,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                cache: false,
                modal: true,
                closed: false,
                buttons: [{
                    text: '保存',
                    handler: function () {

                        if(!window.parent.validate()) return;
                        var jsonObj = window.top.$('#form-dialog').serializeArray();
                        $.ajax({
                            type:'post',
                            url:'<%=path%>/telInfo/saveOrUpdate',
                            data:jsonObj,
                            dataType:'json',
                            loadMsg: constant.Loading,
                            beforeSend:function(XMLHttpRequest){
                            },
                            success:function(data){
                                if(data.result=="true"){
                                    window.top.$.messager.alert('提示', data.message);
                                    $('#grid').datagrid('reload');
                                    _topWindow.dialog('close');
                                }else{
                                    window.top.$.messager.alert('提示', data.message);
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
                },
                    {
                        text: '取消',
                        handler: function () {
                            _topWindow.dialog('close');
                        }
                    }],
                onClose: function () {
                    _topWindow.window('destroy');
                }
            });
        }

        function editTelInfo(uuid){
            _topWindow = window.top.$('#topWindow');
            if (_topWindow.length <= 0){
                _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
            }

            //width:350 650 850  height:450
            _topWindow.dialog({
                title: '编辑号码',
                href: '<%=path%>/telInfo/view/editModel/'+uuid,
                width: 700,
                height: 450,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                cache: false,
                modal: true,
                closed: false,
                buttons: [{
                    text: '保存',
                    handler: function () {

                        if(!window.parent.validate()) return;
                        var jsonObj = window.top.$('#form-dialog').serializeArray();
                        $.ajax({
                            type:'post',
                            url:'<%=path%>/telInfo/saveOrUpdate',
                            data:jsonObj,
                            dataType:'json',
                            loadMsg: constant.Loading,
                            beforeSend:function(XMLHttpRequest){
                            },
                            success:function(data){
                                if(data.result=="true"){
                                    window.top.$.messager.alert('提示', data.message);
                                    $('#grid').datagrid('reload');
                                    _topWindow.dialog('close');
                                }else{
                                    window.top.$.messager.alert('提示', data.message);
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
                },
                    {
                        text: '取消',
                        handler: function () {
                            _topWindow.dialog('close');
                        }
                    }],
                onClose: function () {
                    _topWindow.window('destroy');
                }
            });
        }
    </script>
</head>
<body>
<div class="easyui-panel" title="号码查询" style="width:99%;height:500px;">
<div class="content padding-space" data-options="region:'center'" >
    <form id="form-search"  method="post">
        <table class="table-search">
            <tr>
                <td class="table-search-title">部门</td>
                <td>
                    <input id="search_dept" name="search_dept"  type="text" class="easyui-textbox" data-options="width:200" value=""/>
                </td>
                <td class="table-search-title">计费号码</td>
                <td>
                    <input id="search_tel" name="search_tel"  type="text" class="easyui-textbox" data-options="width:100" value=""/>
                </td>
                <td>
                    <a href="javascript:void(0)" id="queryButton" class="easyui-linkbutton easyui-linkbutton-primary" onclick="reloadGrid()">查询</a>
                </td>
            </tr>
        </table>
    </form>
    <table id="grid" ></table>
</div>
</div>
</body>
</html>
