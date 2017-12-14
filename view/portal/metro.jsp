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
    <link href="<%=path%>/includes/styles/frame-metro.css" rel="stylesheet">

    <script type="text/javascript">

        var menu = [
            {"id": "its", "parentid": "", "text": "财政项目", "uri": "/ny/"},
            {"id": "its01", "parentid": "its", "text": "项目信息管理", "uri": "../ny/project/list?t=" + $.uuid()},
            {"id": "its02", "parentid": "its", "text": "项目执行监控", "uri": "../ny/project/monitor/list?t=" + $.uuid()},
            {"id": "its03", "parentid": "its", "text": "执行情况上报", "uri": "../ny/project/report/list?t=" + $.uuid()},

            {"id": "admin", "parentid": "", "text": "系统管理", "uri": "/admin/"},
            {"id": "admin02", "parentid": "admin", "text": "用户信息管理", "uri": "../admin/user/list?t=" + $.uuid()},
            {"id": "admin03", "parentid": "admin", "text": "角色信息管理", "uri": "../admin/role/list?t=" + $.uuid()},
            {"id": "admin04", "parentid": "admin", "text": "资源信息管理", "uri": "../admin/resource/list?t=" + $.uuid()},
            {"id": "admin05", "parentid": "admin", "text": "组织机构管理", "uri": "../admin/org/tree/list?t=" + $.uuid()},
            {"id": "admin01", "parentid": "admin", "text": "基础代码管理", "uri": "../admin/code/list?t=" + $.uuid()}

        ];

        function resizeLayout(){
            var height = $('#mainFrame').parent().css('height').replace('px', '');
            $('#mainFrame').css('height', (height - 4) + 'px'); //panding 2
            $('#childFrame').css('height', (height - 4) + 'px'); //panding 2
        }

        $(function () {

            resizeLayout();

            $(window).resize(function () {
                setTimeout(function () {
                    resizeLayout();
                }, 200);
            });

            initMenu();
            onMenuRootClick();
            onMenuItemClick();

            $('#userProfile').click(function(){
                utils.openWindow('个人信息', 1, '<%=path%>/admin/user/profile', function () {
                    if (!window.top.$('#form-dialog').form('validate')) return;
                    var jsonObj = window.top.$('#form-dialog').serializeArray();
                    $.post('<%=path%>/admin/user/profile/',
                        jsonObj,
                        function (data, status) {
                            if (status != 'success') {
                                window.top.$.messager.alert('提示', constant.OnlyOneSelected + status);
                                return;
                            }
                            if (!data.result) {
                                window.top.$.messager.alert('提示', constant.FailedToSubmit + data.message);
                                return;
                            }
                            _topWindow.dialog('close');
                        });
                });
            })
        });

        //初始化一级菜单
        function initMenu() {
            var firstRootMenu = null;
            for (i = 0; i < menu.length; i++) {
                if (menu[i].parentid == '') {
                    if (firstRootMenu == null) {
                        $('.header-menu ul').append('<li><a href="#" class="selected" id="' + menu[i].id + '">' + menu[i].text + '</a></li>');
                        firstRootMenu = menu[i];
                    }
                    else {
                        $('.header-menu ul').append('<li><a href="#" id="' + menu[i].id + '">' + menu[i].text + '</a></li>');
                    }

                }
            }
            initMenuItem(firstRootMenu.text, firstRootMenu.id);
        };

        //初始化下级菜单
        var lastParentText;
        function initMenuItem(parentText, parentid) {
            $('#combo-list div').remove();
            lastParentText = parentText;
            var index = 0;
            for (i = 0; i < menu.length; i++) {
                if (menu[i].parentid == parentid) {
                    if (index == 0) {
                        $('#combo-list').append('<div value="' + menu[i].uri + '" class="combobox-item admin-menu-item combobox-item-selected">' + menu[i].text + '</div>');

                        $('.layout-panel-center .panel-title').html(parentText + ' &gt; ' + menu[i].text);
                        tomain(menu[i].uri);
                        index = index + 1;
                    }
                    else {
                        $('#combo-list').append('<div value="' + menu[i].uri + '" class="combobox-item admin-menu-item">' + menu[i].text + '</div>');
                        index = index + 1;
                    }
                }
            }
        };

        //绑定一级菜单事件
        function onMenuRootClick() {
            $('.header-menu a').click(function () {
                initMenuItem($(this).html(), $(this).attr('id'));

                onMenuItemClick();

                $('.header-menu a').each(function () {
                    $(this).removeClass('selected');
                });
                $(this).addClass('selected');
            });
        };

        //绑定下级菜单事件
        function onMenuItemClick() {
            $('#combo-list div').each(function () {
                //取消绑定事件
                $(this).unbind();
                //$(this).addClass('selected');
            });

            $('#combo-list div').click(function () {
                $('#combo-list div').each(function () {
                    $(this).removeClass('combobox-item-selected');
                });
                $(this).addClass('combobox-item-selected');

                $('.layout-panel-center .panel-title').html(lastParentText + ' &gt; ' + $(this).html());
                tomain($(this).attr('value'));
            });

            $('#combo-list div').mouseenter(function () {
                $('#combo-list div').each(function () {
                    $(this).removeClass('combobox-item-hover');
                });
                $(this).addClass('combobox-item-hover');
            }).mouseleave(function () {
                $('#combo-list div').each(function () {
                    $(this).removeClass('combobox-item-hover');
                });
            });

            $('#btnLogout').click(function () {
                $.POST('/admin/logout/', null,
                        function (data) {
                            $('#btnLogin').show();
                            $('#btnUserInfo').hide();
                            $('#btnLogout').hide();
                            window.location.href = '/';
                        });
            })
        };

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

        function notySuccess(msg) {
            var n = noty({
                text: msg,
                type: 'success',
                dismissQueue: true,
                layout: 'topCenter',
                maxVisible: 3,
                timeout: 2000,
                theme: 'someOtherTheme'
            });
        }

        function notyError(msg) {
            var n = noty({
                text: msg,
                type: 'error',
                dismissQueue: true,
                layout: 'topCenter',
                maxVisible: 3,
                timeout: 2000000,
                theme: 'someOtherTheme'
            });
        }
    </script>
</head>
<body class="easyui-layout">
<div class="header" data-options="region:'north',border:false" style="height: 55px;">
    <div class="header-bg">
        <h1 class="header-logo">
            IT Support <sup>3.0</sup>
        </h1>
        <div class="header-user">
            &nbsp;|&nbsp;<a id="userProfile" href="#">您好，${userName}</a> <a id="btnLogout" href="<%=path%>/logout">注销</a>
        </div>
        <div class="header-menu">
            <ul>

            </ul>
        </div>
    </div>
</div>

<div class="aside" data-options="region:'west',split:true" title="系统导航菜单" style="width: 200px;">
    <div id="combo-list" class="panel-body panel-body-noheader panel-body-noboder">

    </div>
</div>
<div class="content" data-options="region:'center',title:'&nbsp;'" style="padding: 2px 2px 0px 2px !important;">
    <iframe id="mainFrame" name="mainFrame" class="iframe" width="100%" frameborder="0" src=""></iframe>
    <iframe id="childFrame" name="childFrame" class="iframe" width="100%" frameborder="0" src="" style="display:none"></iframe>
</div>
</body>
</html>