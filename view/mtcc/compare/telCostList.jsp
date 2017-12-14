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
                url: '<%=path%>/uploadTel/view/uploadTelList/load',
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
                    { field: 'index', title: '序号', width: '40px', hidden:true, align:'center',
                        formatter: function (value, row, index) {
                            return index+1;
                        }
                    },
                    { field: 'uuid', title: 'uuid', hidden:true},
                    { field: 'year_month', title: '年月', width: '55px', sortable: true, align:'center',
                    	formatter: function (value, row, index) {
                    		var period_year = row.period_year;
                    		var period_month = row.period_month;
                    		if (period_year != "") {
                    			return period_year+ "-" + period_month;
                    		} else {
                    			return "";
                    		}
                    	}
                    },
                    { field: 'list_upload_status', title: '清单上传状态', width: '90px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var list_file_date_dis = row.list_file_date_dis;
                    		
                    		var result = "";
                    		if (list_file_date_dis != "") {
                    			result = "1";
                    		}
                    		if (result == "") {
                    			return '未导入';
                    		} else {
                    			return "已导入<br>("+ list_file_date_dis + ')';
                    		}
                    	}
                    },
                    { field: 'detail_upload_status', title: '详单上传状态', width: '90px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var detail_file_date_dis = row.detail_file_date_dis;
                    		
                    		var result = "";
                    		if (detail_file_date_dis != "") {
                    			result = "1";
                    		}
                    		if (result == "") {
                    			return '未导入';
                    		} else {
                    			return "已导入<br>("+ detail_file_date_dis + ')';
                    		}
                    	}
                    },
                    { field: 'pre_compare_date_begin', title: '预处理时间', width: '90px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var pre_compare_date_begin_dis = row.pre_compare_date_begin_dis;
                    		var pre_compare_date_end_dis = row.pre_compare_date_end_dis;
                    		return pre_compare_date_begin_dis + " 至 " + pre_compare_date_end_dis;
                    	}
                    },
                    { field: 'pre_compare_status', title: '预处理状态', width: '80px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var compare_result = row.compare_result;
                    		if (value == "1"){
								return "已预处理";
                    		} else {
                    			return "未预处理";
                    		}
                    	}
                    },
                    { field: 'compare_date_begin', title: '比对时间', width: '90px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var compare_date_begin_dis = row.compare_date_end_dis;
                    		var compare_date_end_dis = row.compare_date_end_dis;
                    		return compare_date_end_dis + " 至 " + compare_date_end_dis;
                    	}
                    },
                    { field: 'compare_status', title: '比对状态', width: '80px', sortable: true, align:'left',
                    	formatter: function (value, row, index) {
                    		var compare_result = row.compare_result;
                    		if (value == "0" || value == ""){
								return "未比对";
                    		} else if (value == "1"){
                    			if (compare_result == "0") {
                    				return "比对完成（结果一致）";
                    			} else if (compare_result =="1"){
                    				return "比对完成（清单多算）";
                    			} else if (compare_result == "2") {
                    				return "比对完成（清单少算）";
                    			}
                    		}
                    	}
                    },
                    { field: 'list_total_costs', title: '清单合计<br>费用（元）', width: '80px', sortable: true, align:'right'},
                    { field: 'detail_total_costs', title: '详单计算<br>费用（元）', width: '80px', sortable: true, align:'right'},
                    { field: 'total_costs_out', title: '销售费用<br>（元）', width: '80px', sortable: true, align:'right'},
                    { field: 'list_tel_shortage', title: '清单计费号码<br>缺少数', width: '80px', sortable: true, align:'right'},
                    { field: 'detail_tel_shortage', title: '详单计费号码<br>缺少数', width: '80px', sortable: true, align:'right'},
                    { field: 'cz', title: '操作', width: '90px', align:'left',
                        formatter: function (value, row, index) {
                            var uuid = row.uuid;
                			var list_file_date_dis = row.list_file_date_dis;
                			var detail_file_date_dis = row.detail_file_date_dis;
                			var compare_status = row.compare_status;
                			var pre_compare_status = row.pre_compare_status;
                			if (list_file_date_dis!="" && detail_file_date_dis!="") {
                				if(pre_compare_status != "1"){
                					return '<a href="#" onclick="preprocess(\''+uuid+'\')">预处理</a>&nbsp;&nbsp;' +
                							'<a href="#" onclick="preprocess_compare(\''+uuid+'\')">预处理+比对</a>';
                				} else {
	                				if (compare_status != "1") {
	                            		return '<a href="#" onclick="preprocess(\''+uuid+'\')">重新预处理</a>&nbsp;&nbsp;' + 
	                            				'<a href="#" onclick="compare(\''+uuid+'\')">比对</a>';
	                				} else {
	                					return '<a href="#" onclick="preprocess(\''+uuid+'\')">重新预处理</a>&nbsp;&nbsp;' + 
	                							'<a href="#" onclick="compare(\''+uuid+'\')">重新比对</a>';
	                				}
                				}
                			} else {
                				return "";
                			}
                        }
                    }
                ]],
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
	    function ajaxLoadingForTopWindow(){
	        $("<div class=\"datagrid-mask\" style='z-index: 9999;'></div>").css({display:"block",width:"100%",height:$(window.top).height()}).appendTo(window.top.document.body);
	        $("<div class=\"datagrid-mask-msg\" style='z-index: 9998;'></div>").html("正在处理，请稍候。。。").appendTo(window.top.document.body).css({display:"block",left:($(window.top.document.body).outerWidth(true) - 190) / 2,top:($(window.top).height() - 45) / 2});
	    }
	    function ajaxLoadEndForTopWindow(){
			window.top.$(".datagrid-mask").remove();
			window.top.$(".datagrid-mask-msg").remove();
	    }
	    

        //采用jquery easyui loading css效果
        function ajaxLoading(){
        	window.top.$("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
        	window.top.$("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
        }
        function ajaxLoadEnd(){
        	window.top.$(".datagrid-mask").remove();
        	window.top.$(".datagrid-mask-msg").remove();
        }
        
        var _topWindow;
        function compare(uuid) {
     	   
     	   _topWindow = window.top.$('#topWindow');
            if (_topWindow.length <= 0){
                _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
            }
            //width:350 650 850  height:450
            _topWindow.dialog({
                title: '比对',
                href: '<%=path%>/uploadTel/view/compare?uuid='+uuid,
                width: 500,
                height: 250,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                cache: false,
                modal: true,
                closed: false,
                buttons: [{
                    text: '比对',
                    handler: function () {
                        if(!window.parent.validate()) return;
                        var jsonObj = window.top.$('#form-dialog').serializeArray();
                        $.ajax({
                            type:'post',
                            url:'<%=path%>/uploadTel/compare?uuid='+uuid,
                            data:jsonObj,
                            dataType:'json',
                            loadMsg: constant.Loading,
                            beforeSend:function(XMLHttpRequest){
                            	ajaxLoadingForTopWindow();
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
                            	ajaxLoadEndForTopWindow();
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
        
        function preprocess(uuid) {
      	   
      	   _topWindow = window.top.$('#topWindow');
             if (_topWindow.length <= 0){
                 _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
             }
             //width:350 650 850  height:450
             _topWindow.dialog({
                 title: '预处理',
                 href: '<%=path%>/uploadTel/view/preprocess?uuid='+uuid,
                 width: 500,
                 height: 250,
                 collapsible: false,
                 minimizable: false,
                 maximizable: false,
                 resizable: false,
                 cache: false,
                 modal: true,
                 closed: false,
                 buttons: [{
                     text: '比对',
                     handler: function () {
                         if(!window.parent.validate()) return;
                         var jsonObj = window.top.$('#form-dialog').serializeArray();
                         $.ajax({
                             type:'post',
                             url:'<%=path%>/uploadTel/preprocess?uuid='+uuid,
                             data:jsonObj,
                             dataType:'json',
                             loadMsg: constant.Loading,
                             beforeSend:function(XMLHttpRequest){
                             	ajaxLoadingForTopWindow();
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
                             	ajaxLoadEndForTopWindow();
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
        
        function preprocess_compare(uuid) {
       	   
       	   _topWindow = window.top.$('#topWindow');
              if (_topWindow.length <= 0){
                  _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
              }
              //width:350 650 850  height:450
              _topWindow.dialog({
                  title: '预处理+比对',
                  href: '<%=path%>/uploadTel/view/preprocess_compare?uuid='+uuid,
                  width: 500,
                  height: 250,
                  collapsible: false,
                  minimizable: false,
                  maximizable: false,
                  resizable: false,
                  cache: false,
                  modal: true,
                  closed: false,
                  buttons: [{
                      text: '预处理+比对',
                      handler: function () {
                          if(!window.parent.validate()) return;
                          var jsonObj = window.top.$('#form-dialog').serializeArray();
                          $.ajax({
                              type:'post',
                              url:'<%=path%>/uploadTel/preprocess_compare?uuid='+uuid,
                              data:jsonObj,
                              dataType:'json',
                              loadMsg: constant.Loading,
                              beforeSend:function(XMLHttpRequest){
                              	ajaxLoadingForTopWindow();
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
                              	ajaxLoadEndForTopWindow();
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
<div class="easyui-panel" title="话费查询" style="width:99%;height:500px;">
<div class="content padding-space" data-options="region:'center'" >
    <form id="form-search">
        <table class="table-search">
            <tr>
                <td class="table-search-title">查询条件</td>
                <td>
                    <input id="search_year" name="search_year" class="easyui-numberspinner" data-options="increment:1,width:100" value="${search_year }"/>&nbsp;&nbsp;年&nbsp;&nbsp;
                    <!-- <input id="search_month" name="search_month" class="easyui-numberspinner" data-options="increment:1,min:01,max:12,width:80" value="${search_month }"/>&nbsp;&nbsp;月 -->
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
