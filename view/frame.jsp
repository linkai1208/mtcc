<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include flush="true" page="common/resource.jsp"></jsp:include>

    <script>
        var clickUrl = "";
        var oldUrl = "";
        $(function() {
            //左侧导航箭头切换
            $('.nav-header').click(function(e){
                $(this).children('.glyphicon-chevron-down').toggleClass('hidden');
                $(this).children('.glyphicon-chevron-up').toggleClass('hidden');
            })

            //左侧导航鼠标进入样式
            $('.nav-header').mouseenter(function(e){
                $(this).css("background-color","#167ecd");
                $(this).css("color","white");
                $(this).css("border","0px");
            })

            $('.nav-header').mouseenter();

            //展开左侧导航
            $('.nav-header').eq(0).click();

            $(".nav-list li a").mouseenter(function() {
//                $(this).css("background-color","#eeeeee");
//                $(this).css("color","black");
                var page = $(this).attr("data-url");
                if(typeof(page) != 'undefined') {
                    if (page != clickUrl) {
//                        $(this).focus();
                        $(this).css("background-color","#167ecd");
                        $(this).css("color","white");
                    } else {
                        $(this).css("background-color","white");
                        $(this).css("color","#1888de");
                    }
                } else {
                    $(this).css("background-color","#167ecd");
                    $(this).css("color","white");
                }

            })

            $(".nav-list li a").click(function() {
//                $("a[data-url='" + oldUrl + "']").css("background-color","#1888de");
//                $("a[data-url='" + oldUrl + "']").css("color","white");
//                $(this).css("background-color","#eeeeee");
//                $(this).css("color","black");
                $("a[data-url='" + oldUrl + "']").css("background-color","#1888de");
                $("a[data-url='" + oldUrl + "']").css("color","white");
                $(this).css("background-color","#eeeeee");
                $(this).css("color","#1888de");
                var page = $(this).attr("data-url");
                if(typeof(page) != 'undefined') {
                    clickUrl = page;
                }

            })

            $(".nav-list li a").mouseout(function() {
                var page = $(this).attr("data-url");
                if(typeof(page) != 'undefined') {
                    if (page != clickUrl) {
//                        $(this).focus();
                        $(this).css("background-color","#1888de");
                        $(this).css("color","white");
                    } else {
                        $(this).css("background-color","#eeeeee");
                        $(this).css("color","#1888de");
                    }
                } else {
                    $(this).css("background-color","white");
                    $(this).css("color","#167ecd");
                }
            })

            var pageLink = '${pageLink}';
            $("a").each(function() {
                var page = $(this).attr("data-url");

                if(typeof(page) != 'undefined') {
                    if (page == pageLink) {
                        clickUrl = pageLink;
                        oldUrl = pageLink;
//                        $(this).focus();
                        $(this).click();
                        return false;
                    }
                }
            })

            var height=document.documentElement.clientHeight;

            document.getElementById('iframe-page-content').style.height=height - 100 +'px';

            $("#sidebar").height(height - 60 +  'px');

            var t;
            $("#dropdownMenu1").mouseenter(function() {
                $(this).click();
            })
            $("#dropdownMenu1").mouseleave(function() {
                t = setTimeout('dropdownMenuClick()',500);
//                $(this).click();
            })
            $("#dropdownMenu2").mouseenter(function() {
                clearTimeout(t);
            })
            $("#dropdownMenu2").mouseleave(function() {
                $(this).click();
            })
        })

        function dropdownMenuClick() {
            $("#dropdownMenu1").click();
        }


        var menuClick = function(sender, parent) {
            var menuUrl = $(sender).attr("data-url");

            var str = parent + "&nbsp;>&nbsp;" + "<a href='javascript:openPage(\"" + menuUrl +　"\")'>" +  $(sender).html() + "</a>";
            $("#navDiv").html(str);

            if(typeof(menuUrl) != 'undefined') {
                oldUrl = clickUrl;
                clickUrl = menuUrl;
                $("#iframe-page-content").attr('src', '<%=path%>' + menuUrl + "?t=" + $.uuid());
            }

        }

        function openLogin() {
            window.location.href= "<%=path%>/logout";
        }

        function openPage(url) {
            $("#iframe-page-content").attr('src', '<%=path%>' + url + "?t=" + $.uuid());
        }
    </script>
</head>
<body id="navBody">
<div style="width: 100%">

    <!-- 左侧菜单栏 -->

    <div id="main-Container" class="row-fluid">
        <div class="span12">
            <div class="adminHeadArea">
                <div style="float: left">
                    <span style="font-size: 14px">移动话费核对系统</span>
                </div>
                <div class="dropdown pull-right">
                    <a type="button" class="btn dropdown-toggle" id="dropdownMenu1"
                       data-toggle="dropdown" style="color: white">
                        <img src="./includes/images/gerenzhongxin.png" style="width: 18px; height: 15px;"/>&nbsp;欢迎，${memberName}
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="dropdownMenu2">
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" href="javascript:openPage('/admin/pw/modify')" style="color: grey"><i class="glyphicon glyphicon-lock"></i>&nbsp;&nbsp;修改密码</a>
                        </li>
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" href="javascript:openLogin()" style="color: grey"><i class="glyphicon glyphicon-off"></i>&nbsp;&nbsp;退出</a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid" >
                <div id="sidebar" class="col-md-2 column" style="padding-right:0px;background-color: #1888de;">

                    <!-- 创建菜单树 -->

                    <div class="col-md-13" style="margin-left:-20px;">

                        <ul id="main-nav" class="nav nav-stacked" style="background-color: #1888de;">
                            <li>

                                <a href="#systemSetting2" class="nav-header collapsed headNav" data-toggle="collapse">
                                    <i class="pull-left glyphicon glyphicon-home"></i>&nbsp;&nbsp;&nbsp;&nbsp;话费核对

                                    <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                                    <span class="pull-right glyphicon glyphicon-chevron-up hidden"></span>

                                </a>

                                <ul id="systemSetting2" class="nav nav-list collapse">
									<li><a href="#" onclick="menuClick(this,'话费导入')" data-url="/uploadTel/uploadTelImport">话费导入</a></li>
                                    <li><a href="#" onclick="menuClick(this,'话费核对')" data-url="/uploadTel/uploadTelList">话费管理</a></li>
                                </ul>

                            </li>
							<li>

                                <a href="#basic" class="nav-header collapsed headNav" data-toggle="collapse">

                                    <i class="pull-left glyphicon glyphicon-info-sign"></i>&nbsp;&nbsp;&nbsp;&nbsp;基础信息

                                    <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                                    <span class="pull-right glyphicon glyphicon-chevron-up hidden"></span>

                                </a>

                                <ul id="basic" class="nav nav-list collapse">
                                    <li><a href="#" onclick="menuClick(this,'号码管理')" data-url="/telInfo/telInfoList">号码管理</a></li>
                                    <li><a href="#" onclick="menuClick(this,'部门管理')" data-url="/deptInfo/deptInfoList">部门管理</a></li>
                                </ul>

                            </li>
                            
							<li>

                                <a href="#systemSetting3" class="nav-header collapsed headNav" data-toggle="collapse">

                                    <i class="pull-left glyphicon glyphicon-user"></i>&nbsp;&nbsp;&nbsp;&nbsp;用户

                                    <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                                    <span class="pull-right glyphicon glyphicon-chevron-up hidden"></span>

                                </a>

                                <ul id="systemSetting3" class="nav nav-list collapse">

                                    <li><a href="#" onclick="menuClick(this,'用户')" data-url="/admin/open/userList">用户</a></li>

                                </ul>

                            </li>
                            <li>

                                <a href="#systemSetting4" class="nav-header collapsed headNav" data-toggle="collapse">

                                    <i class="pull-left glyphicon glyphicon-cog"></i>&nbsp;&nbsp;&nbsp;&nbsp;设置

                                    <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                                    <span class="pull-right glyphicon glyphicon-chevron-up hidden"></span>

                                </a>

                                <ul id="systemSetting4" class="nav nav-list collapse">
                                    <li><a href="#" onclick="menuClick(this,'设置')" data-url="/admin/open/gAdminList">权限设置</a></li>
                                    <li><a href="#" onclick="menuClick(this,'设置')" data-url="/admin/cache/open">清理缓存</a></li>
                                    <li><a href="#" onclick="menuClick(this,'设置')" data-url="/admin/open/adminList">管理员设置</a></li>
                                    <li><a href="#" onclick="menuClick(this,'设置')" data-url="/admin/open/logList">操作日志</a></li>
                                </ul>

                            </li>

                            <li>

                                <a href="#static" class="nav-header collapsed headNav" data-toggle="collapse">

                                    <i class="pull-left glyphicon glyphicon-stats"></i>&nbsp;&nbsp;&nbsp;&nbsp;统计

                                    <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                                    <span class="pull-right glyphicon glyphicon-chevron-up hidden"></span>

                                </a>

                                <ul id="static" class="nav nav-list collapse">
                                    <li><a href="#" onclick="menuClick(this,'统计')" data-url="/admin/open/stats/registUser">用户统计</a></li>
                               </ul>

                            </li>
                        </ul>

                    </div>

                </div>
            </div>
        </div>

        <div class="col-md-10 column" style="background-color: #eeeeee;">

            <div style="padding-top: 10px;padding-left: 10px" id="navDiv">

            </div>
            <!-- 内容展示页 -->

            <div style="padding-top: 10px;padding-left: 0px;width:100%">

                <iframe id="iframe-page-content" src="" width="100%"  frameborder="no" border="0" marginwidth="0"
                        marginheight=" 0" scrolling="yes" allowtransparency="yes" style="overflow-x: hidden;"></iframe>

            </div>
        </div><!-- /.main-content -->
    </div><!-- /.main-container -->

</div>
</body>
</html>
