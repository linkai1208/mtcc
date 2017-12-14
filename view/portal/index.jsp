<%--
  Created by IntelliJ IDEA.
  User: linkai
  Date: 16-3-19
  Time: 20:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include flush="true" page="/view/common/resource.jsp"></jsp:include>
    <link href="<%=path%>/includes/styles/frame-r1.css" rel="stylesheet">
    <script src="<%=path%>/includes/jquery-mcustomscrollbar-3.1.3/jquery.mCustomScrollbar.concat.min.js"></script>
    <script>
        var userid = "${userid}";
        var menu = ${menu};
        var sysid = "${sysid}";
        $(function () {
            //初始化滚动条
            $("#sidebar").mCustomScrollbar();

            //初始化子系统
            var systemHtml = "";
            var firstParentId = "";
            for (var i = 0; i < menu.length; i++) {
                if (menu[i].parentId != '') continue;
                systemHtml += '<li data-sysid="' + menu[i].id + '">' + menu[i].text + '</li>';
                if (firstParentId == "") firstParentId = menu[i].id;
            }
            $('#system-list').html('<ul>' + systemHtml + '</ul>');

            //初始化菜单
            initMenu(sysid);

            //弹出切换子系统菜单
            $('#header-system-change').click(function (e) {
                var subsys = $(this);
                var subsysList = $('#system-list');

                var top = 30;
                var left = subsys.offset().left - (subsysList.css('width').replace('px', '') * 1.0) + (subsys.css('width').replace('px', '') * 1.0) + 20;
                subsysList.css('top', top + 'px');
                subsysList.css('left', left + 'px');

                if (subsys.hasClass('selected')) {
                    subsys.removeClass('selected');
                    subsysList.hide();
                }
                else {
                    subsys.addClass('selected');
                    subsysList.show();
                }
                hidePopMenu($(this).attr('id'));
                e.stopPropagation();
            })

            //切换子系统
            $('#system-list li').click(function () {
                var systemid = $(this).attr('data-sysid');
                if(sysid == systemid) return;

                //r1切换子系统
                //post http://localhost:8080/fsopr1/switch
                //Way:login
                //entrymode:switch
                //sysId:24
                //get  http://localhost:8080/fsopr1/login?Way=login&entrymode=switch&sysId=24
                window.location.href = '<%=basePath%>fsopr1/login?Way=login&entrymode=switch&sysId=' + systemid;

                initMenu(systemid);
                hidePopMenu('');
                $.cookie(userid + '_system_id', systemid, { expires: 7 });
            })

            //收缩展开左侧菜单
            $('#sidebar-button').click(function () {
                if ($('#sidebar').css('left') != '0px') {
                    //展开
                    expSidebar();
                }
                else {
                    //收缩
                    colSidebar();

                }
            }).mouseenter(function () {
                $(this).css('background-color', '#bfdbff');
                $(this).css('color', '#146FC0');
            }).mouseleave(function () {
                $(this).css('background-color', '#F0F5FB');
                $(this).css('color', '#dfdfdf');
            })

            $('#header').click(function (e) {
                hidePopMenu('');
            });

            $('#content').mouseenter(function () {
                hidePopMenu('');
            })

            resizeLayout();

            //水平菜单超出长度处理
            var menusWidth = 0;
            $('#menu-root li').each(function(){
                menusWidth += $(this).width();
            });
            //内部菜单大于外部, 显示左右隐藏按钮
            if(menusWidth > 1280){
                $('#ltgt').show();
            }

            $('#lt').click(function(){
                var length = $('#menu-root li').length;
                for(i=0; i< length; i++){
                    if(!$('#menu-root li:eq('+i+')').is(':hidden')){
                        $('#menu-root li:eq('+i+')').hide();
                        break;
                    }
                }
            });

            $('#gt').click(function(){
                var length = $('#menu-root li').length;
                for(i=length-1; i>=0; i--){
                    if($('#menu-root li:eq('+i+')').is(':hidden')){
                        $('#menu-root li:eq('+i+')').show();
                        break;
                    }
                }
            });

            //初始化左侧导航区滚动条
            $("#sidebar").mCustomScrollbar();

            $(window).resize(function () {
                setTimeout(function () {
                    resizeLayout();
                }, 200);
            });
        })

        var lastState = '';
        function expSidebar() {
            $('#sidebar').css('left', '0px');
            $('#sidebar-button').css('left', '230px');
            $('#sidebar-button').find('i').removeClass('fa-chevron-right');
            $('#sidebar-button').find('i').addClass('fa-chevron-left');
            $('#content').css('left', '240px');
            $('#content').css('width', $(document).width() - 242);
            lastState = 'exp';
        }

        function colSidebar() {
            $('#sidebar').css('left', '-1000px');
            $('#sidebar-button').css('left', '0px');
            $('#sidebar-button').find('i').removeClass('fa-chevron-left');
            $('#sidebar-button').find('i').addClass('fa-chevron-right');
            $('#content').css('left', '1px');
            $('#content').css('width', $(document).width() - 3);
            lastState = 'col';
        }

        function resizeLayout() {
            var height = $(document).height() - $('#header').height() + 2;
            $('#sidebar').css('height', height + 'px');
            $('#content').css('height', height + 'px');
            if(lastState == 'col'){
                colSidebar();
            }
            else{
                expSidebar();
            }
        }

        function hideSidebar() {
            var btn = $('#sidebar-button');
            if (!btn.is(":hidden")){
                btn.hide();
                colSidebar();
            }
        }

        function showSidebar() {
            var btn = $('#sidebar-button');
            if (btn.is(":hidden")) {
                btn.show();
                expSidebar();
            }
        }

        function hidePopMenu(sender) {
            if ('header-system-change' != sender) {
                $('#header-subsys').removeClass('selected');
                $('#system-list').hide();
            }
            if ('header-user' != sender) {
                $('#header-user').removeClass('selected');
                $('#user-info').hide();
            }
        }

        //初始菜单
        function initMenu(parentId) {
            //初始化top菜单
            var topHtml = "";
            var isFirst = true;
            for (var i = 0; i < menu.length; i++) {
                if (menu[i].id == parentId) {
                    $('#header-system-name').html(menu[i].text);
                }

                if (menu[i].parentId == parentId) {
                    if (isFirst) {
                        topHtml += '<li  class="selected" data-url="' + menu[i].url + '" data-sysid="' + menu[i].id + '" data-sysparentid="' + menu[i].parentId + '"><a href="javascript:void(0)" onclick="initNav(\'' + menu[i].id + '\')" >' + menu[i].text + '</a></li>';
                        if (menu[i].url != "") {
                            //直接打开页面
                            tomain(menu[i].url);
                            //隐藏左侧导航区域
                            hideSidebar();
                        }
                        else {
                            //初始化左侧菜单
                            initNav(menu[i].id);
                        }
                        isFirst = false;
                    }
                    else {
                        topHtml += '<li data-url="' + menu[i].url + '" data-sysid="' + menu[i].id + '" data-sysparentid="' + menu[i].parentId + '"><a href="javascript:void(0)" onclick="initNav(\'' + menu[i].id + '\')" >' + menu[i].text + '</a></li>';
                    }
                }
            }
            $('#menu-root').html(topHtml);
        }

        //初始化左侧导航
        function initNav(parentId) {
            var leftHtml = "";
            var isFirstRoot = true;
            var isFirstSub = true;
            var url = "";

            //处理选中样式
            $('#menu-root li').each(function () {
                $(this).removeClass('selected');
                if ($(this).attr('data-sysid') == parentId) {
                    $(this).addClass('selected');
                }
            })

            for (var i = 0; i < menu.length; i++) {
                if (menu[i].id == parentId) url = menu[i].url;

                if (menu[i].parentId == parentId) {
                    var subHtml = "";
                    for (var j = 0; j < menu.length; j++) {
                        if (menu[j].parentId == menu[i].id) {
                            if (isFirstSub) {
                                if (menu[j].url.length > 0) tomain(menu[j].url);
                                isFirstSub = false;
                                subHtml += '<li class="selected" data-url="' + menu[j].url + '" data-sysid="' + menu[j].id + '" data-sysparentid="' + menu[j].parentId + '" onclick="onNavClick(this)"><i class="fa fa-circle-thin"></i> ' + menu[j].text + '</li>';
                            }
                            else {
                                subHtml += '<li data-url="' + menu[j].url + '" data-sysid="' + menu[j].id + '" data-sysparentid="' + menu[j].parentId + '" onclick="onNavClick(this)"><i class="fa fa-circle-thin"></i> ' + menu[j].text + '</li>';
                            }
                        }
                    }

                    subHtml = '<ul class="nav-sub">' + subHtml + '</ul>';
                    leftHtml += '<li data-sysid="' + menu[i].id + '"><i class="fa fa-clone"></i> ' + menu[i].text + subHtml + '</li>';
                }
            }

            $('.nav-root').html(leftHtml);
            if (leftHtml.length > 0) {
                showSidebar();
            }
            else {
                hideSidebar();
                tomain(url);
            }
        }

        function onNavClick(sender) {
            $('.nav-sub li').each(function () {
                $(this).removeClass('selected');
            })
            $(sender).addClass('selected');

            tomain($(sender).attr('data-url'));
        }

        //打开一级页面 window.top.tomain()
        function tomain(url) {
            $('#childFrame').hide();
            if (url.indexOf('?') == -1) {
                url = url + '?t=' + $.uuid();
            }
            else {
                url = url + '&t=' + $.uuid();
            }
            $('#mainFrame').attr('src', url);
            $('#mainFrame').show();

            $('#childFrame').attr('src', '');
        }

        //打开二级页面 window.top.tochild()
        function tochild(url) {
            $('#mainFrame').hide();
            if (url.indexOf('?') == -1) {
                url = url + '?t=' + $.uuid();
            }
            else {
                url = url + '&t=' + $.uuid();
            }
            $('#childFrame').attr('src', url);
            $('#childFrame').show();
        }

        //返回一级页面 window.top.toback()
        function toback(url) {
            if (typeof (url) != 'undefined') {
                var src = $('#mainFrame').attr('src');
                if (src.indexOf(url) == -1) {
                    $('#mainFrame').attr('src', url);
                }
            }
            $('#mainFrame').show();
            $('#childFrame').hide();
            $('#childFrame').attr('src', '');

            setTimeout(function () {
                try {
                    $('#mainFrame')[0].contentWindow.reloadGrid();
                } catch (e) {

                }
            }, 800);
        }
    </script>
</head>
<body>
<div id="header">
    <img class="logo" src="<%=path%>/includes/images/logo.png" height="32">
    <h1>民航飞行标准监督管理系统<span>FLIGHT STANDARDS OVERSIGHT PROGRAM</span></h1>
    <a id="header-system-name" href="javascript:void(0)"> 使用困难报告系统 </a>
    <a id="header-system-change" href="javascript:void(0)" data-sysid="1">
        快速切换
    </a>
    <a href="/fsopr1/logout">
        [注销]
    </a>
    <a id="header-user" href="javascript:void(0)">
        ${username}，您好
    </a>
    <div id="ltgt" style="display: none">
        <a id="gt" href="javascript:void(0)">&gt;</a>
        <a id="lt" href="javascript:void(0)">&lt;</a>
    </div>
    <div id="menu-bg">
        <ul id="menu-root">
            <li><a href="javascript:void(0)">首页</a></li>
            <%--<li class="selected"><a href="javascript:void(0)">使用困难</a></li>--%>
            <%--<li><a href="javascript:void(0)">使用月报</a></li>--%>
            <%--<li><a href="javascript:void(0)">变化信息</a></li>--%>
        </ul>
    </div>
</div>

<div id="sidebar">
    <ul class="nav-root">
        <li data-sysid="1">
        <i class="fa fa-clone"></i> 提示信息
        <ul class="nav-sub">
            <li data-url=""><i class="fa fa-circle-thin"></i> 显示菜单错误, 请联系管理员, 010-64092846</li>
        </ul>
        </li>
        <%--<li data-sysid="1">--%>
            <%--<i class="fa fa-clone"></i> 初始报告--%>
            <%--<ul class="nav-sub">--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 近期使用困难报告</li>--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 初始报告查询</li>--%>
            <%--</ul>--%>
        <%--</li>--%>
        <%--<li data-sysid="2" class="root-selected">--%>
            <%--<i class="fa fa-clone"></i> 调查报告--%>
            <%--<ul class="nav-sub">--%>
                <%--<li class="sub-selected" data-url="http://www.baidu.com"><i class="fa fa-circle-thin"></i> 调查报告签署</li>--%>
                <%--<li data-url="http://www.douban.com"><i class="fa fa-circle-thin"></i> 调查报告查询</li>--%>
                <%--<li data-url="http://www.zol.com.cn"><i class="fa fa-circle-thin"></i> 调查报告附件检索</li>--%>
            <%--</ul>--%>
        <%--</li>--%>
        <%--<li data-sysid="3">--%>
            <%--<i class="fa fa-clone"></i> 通航使用困难报告--%>
            <%--<ul class="nav-sub">--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 使用困难报告签署</li>--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 使用困难报告查询</li>--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 使用困难报告管理</li>--%>
            <%--</ul>--%>
        <%--</li>--%>
        <%--<li data-sysid="4">--%>
            <%--<i class="fa fa-clone"></i> 移交单--%>
            <%--<ul class="nav-sub">--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 移交单</li>--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 移交单查询</li>--%>
            <%--</ul>--%>
        <%--</li>--%>
        <%--<li data-sysid="5">--%>
            <%--<i class="fa fa-clone"></i> 使用困难报告管理--%>
            <%--<ul class="nav-sub">--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 初始报告管理</li>--%>
                <%--<li data-url=""><i class="fa fa-circle-thin"></i> 调查报告管理</li>--%>
            <%--</ul>--%>
        <%--</li>--%>
    </ul>
</div>

<div id="sidebar-button">
    <i class="fa fa-chevron-left"></i>
</div>

<div id="content">
    <iframe id="mainFrame" name="mainFrame" width="100%" height="99%" frameborder="0" src=""></iframe>
    <iframe id="childFrame" name="mainFrame" width="100%" height="99%" frameborder="0" src="" style="display:none"></iframe>
</div>

<div id="system-list" style="display:none">
    <ul>
        <%--<li data-sysid="1">审定监察子系统</li>--%>
        <%--<li data-sysid="2">运行规范子系统</li>--%>
        <%--<li data-sysid="3">电子规章子系统</li>--%>
        <%--<li data-sysid="4">使用困难报告子系统</li>--%>
        <%--<li data-sysid="5">体检合格证子系统</li>--%>
        <%--<li data-sysid="6">签派员委任代表子系统</li>--%>
    </ul>
</div>
</body>
</html>