/*
 * chairui
 *
 * 2016-04-16 easyui 扩展 combotree 取消选择
 * 2017-05-23 增加 vue datagrid
 */

; (function () {

    var utils = function () {
        this.formatBoolean = function (value, trueTitle, falseTitle) {
            if (value == 'True' || value == 'true' || value == '1') {
                if('undefined' !== typeof trueTitle){
                    return trueTitle;
                }
                else{
                    return "是";
                }
            }
            else {
                if('undefined' !== typeof falseTitle){
                    return falseTitle;
                }
                else{
                    return "否";
                }
            }
        };
        this.replaceDict = function(dict, value){
            //dict = [{ "name": "title0", "value": 0 }, { "name": "title1", "value": 1}];
            var name = '';
            for(var i=0; i< dict.length; i++){
                if(dict[i].value == value){
                    name = dict[i].name;
                    break; 
                }
            }
            return name;
        };
        this.formatGTM = function(value){
            //value = Sat, 08 Feb 2014 00:00:00 GMT
            var date = new Date(value);
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            return date.getFullYear() + "-" + month + "-" + day;
        };
        this.formatJsonDate = function (cellval) {
            //空值处理
            if(cellval == null || cellval == ''){
                return '';
            }

            //spring mvc 序列化 631209600000
            cellval = cellval + "";
            //.net mvc 序列化 /Date(1325696521000)/
            var intval = parseInt(cellval.replace("/Date(", "").replace(")/", ""));
            if (intval == -62135596800000) {
                return '';
            }

            var date = new Date(parseInt(cellval.replace("/Date(", "").replace(")/", ""), 10));
            var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
            var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            return date.getFullYear() + "-" + month + "-" + currentDate;
        };
        this.openWindow = function(title, size, url, fn){
            //size: 0,1,2 对应 width:350 650 850  height:450  自定义:'200,200'

            _topWindow = window.top.$('#topWindow');
            if (_topWindow.length <= 0){
                _topWindow = window.top.$('<div id="topWindow"/>').appendTo(window.top.document.body);
            }

            var width = 350;
            var height = 450;
            if(size == 0) {
                width = 350;
                height = 300;
            }
            else if(size == 1) {
                width = 650;
                height = 450;
            }
            else if(size == 2) {
                width = 850;
                height = 450;
            }
            else if(size.indexOf(",")>-1){
                var array = size.split(",");
                width = array[0] * 1.0;
                height = array[1] * 1.0;
            }

            _topWindow.dialog({
                title: title,
                href: url,
                width: width,
                height: height,
                collapsible: false,
                minimizable: false,
                maximizable: false,
                resizable: false,
                cache: false,
                modal: true,
                closed: false,
                buttons: [{
                    text: '确定',
                    handler: function () {
                        fn();
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

        };//end openWindow
        this.easyuiTextboxTypeahead = function(sender, id, url, filter, readOnly, fn){
            //辅助感知输入控件
            //sender    当前点击对象，动态生成的输入控件参考 sender 初始化位置和宽、高信息
            //id        选中后，结果赋值到该对象，传入的是 id 选择器，例如 #user
            //url       查询数据的 url 资源, 后台实现查询接口, 返回 json 类型数据,
            //          数据结构 [{"text": "", "value1":"", "value2":""}]
            //          注意, 必须有一个属性为 text
            //filter    查询过滤条件，作为 url 后面动态拼接的参数传递到后台，传入的是 id 选择器，例如 #user_filter
            //          使用 hidden 保存 filter 数据，例如<input id='user_filter' type='hidden' value='&orgCode=abc'>
            //          不需要过滤条件, 赋值 null
            //readOnly  true 只读，如果没选中返回 null，false 允许输入，如果没选中返回输入内容
            //fn        回调函数，选中、或点击删除调用 fn(id, suggestion), suggestion 为json 数据或 null
            //          回调函数如果为 null, 系统使用默认回调函数, 仅处理 text 属性值

            //默认回调函数
            function typeaheadCallBack(id, suggestion){
                if(suggestion == null){
                    $(id).textbox('setValue','');
                }
                else {
                    $(id).textbox('setValue',suggestion.text);
                }
            };

            var widget =
                '<div id="pnlTypeahead" class="print-hide" style="position:absolute; z-index:1; display:none; height:240px;">' +
                '   <input id="tbTypeahead" class="typeahead-input" />' +
                '   <a id="btnTypeaheadClear" class="typeahead-delete" src="javascript:void(0)" style=" margin-top: 0px; margin-left:2px; float: right; cursor: pointer" ></a>' +
                '</div>';
            $(document.body).append(widget);

            var top = $(sender).offset().top;
            var left = $(sender).offset().left;
            var width = $(sender).css('width').replace("px","") * 1;
            var height = $(sender).css('height').replace("px","") * 1;


            $('#pnlTypeahead').css('top', top - 1 + 'px');
            $('#pnlTypeahead').css('left', left - 1 + 'px');
            $('#pnlTypeahead').show();
            $('#pnlTypeahead').css('display', 'block');
            $('#pnlTypeahead').css('height', '27px');
            $('#tbTypeahead').css('height', height - 4 + 'px');
            $('#tbTypeahead').css('width', width + 0 + "px");
            //下拉列表最小宽度
            if(width < 200) {
                width = 200;
            }else{
                width = width - 5;
            }
            $('#pnlTypeahead .tt-hint').css('width', width + "px");


            var defaultValue = $(id).textbox('getValue');
            $('#tbTypeahead').val(defaultValue);

            setTimeout(function(){
                $('#tbTypeahead').focus();
                $('#tbTypeahead').select();
            }, 200);


            var filter_params = '';
            if(filter != null){
                filter_params = $(filter).val();
            }

            //远程数据源
            var remote_data = new Bloodhound({
                cache: false,
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('text'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                // 在文本框输入字符时才发起请求
                remote: {
                    url: url + '?SearchValue=%QUERY' + filter_params,
                    wildcard: '%QUERY'
                }
            });
            remote_data.initialize();

            var selected = false;
            //$('#tbTypeahead').typeahead('destroy');
            $('#tbTypeahead').typeahead(null, {
                minLength: 1,
                limit: 20,
                name: 'none',
                display: 'text',
                highlight: true,
                hint: false,
                source: remote_data
            }).on('typeahead:open', function () {
                setTimeout(function(){
                    $('.tt-menu').css({'width':width, 'height':'240px', 'overflow-y':'scroll'});
                }, 200);
            }).on('typeahead:close', function () {
                setTimeout(function(){
                    //给 btnTypeaheadClear click 时间预留触发时间
                    $('#pnlTypeahead').hide();
                    $('#pnlTypeahead').remove();
                }, 200);

                if(!selected && !readOnly) {
                    //如果没选中，默认使用输入的值
                    var suggestion = {'text': $('#tbTypeahead').val()};
                    if(fn != null) {
                        fn(id, suggestion);
                    }
                    else{
                        typeaheadCallBack(id, suggestion);
                    }
                }
            }).on('typeahead:selected', function (event, suggestion) {
                selected = true;
                //$(sender).html(suggestion.text);
                //调用回调函数, 处理具体业务
                if(fn != null) {
                    fn(id, suggestion);
                }
                else{
                    typeaheadCallBack(id, suggestion);
                }
            });

            $('#btnTypeaheadClear').click(function(){
                $('#pnlTypeahead').hide();
                $('#pnlTypeahead').remove();
                if(fn != null) {
                    fn(id, null);
                }
                else{
                    typeaheadCallBack(id, null);
                }
            })
        };//end easyuiTextboxTypeahead
    };

    window.utils = utils;
})();

var utils = new utils();
var _topWindow;




; (function () {
    var maskWindow = function () {
        //position true 在当前页提示, 不填写 或 false, 在最顶层页面
        this.show = function (message, position) {

            if (position) {
                var _body = document.body;
                var _maskWindow = $('#maskWindow');
                var _maskMessage = $('#maskMessage');

            } else {
                var _body = window.top.document.body;
                var _maskWindow = window.top.$('#maskWindow');
                var _maskMessage = window.top.$('#maskMessage');
            }

            if (_maskWindow.length <= 0) {
                _maskWindow = $('<div id="maskWindow" style="position:absolute; top:0; left:0; z-index:999; width:100%; height:auto;  background-color:#151410;"/>').appendTo(_body);
                _maskMessage = $('<div id="maskMessage" style="width:300px; height:30px; background-color:#fff; border: 1px solid #6593cf; color:#131d77; font-size:12px;  line-height:30px; text-align:center; margin-left:-150px; top:200px; left:50%; z-index:1000; position:absolute; display:block">'+message+'</div>').appendTo(_body);
            }

            var count = 0;
            setInterval(function(){
                if (count%4 == 0) {
                    _maskMessage.html(message);
                } else {
                    _maskMessage.html(_maskMessage.html() + '.');
                }
                count++;
            },1000);

            _maskWindow.height(_body.scrollHeight);
            _maskWindow.width(_body.scrollWidth);

            // fadeTo第一个参数为速度，第二个为透明度
            _maskWindow.fadeTo(200, 0.8);
        },
        this.hide = function (position) {
            if (position) {
                $("#maskWindow").remove();
                $("#maskMessage").remove();
            } else {
                window.top.$("#maskWindow").remove();
                window.top.$("#maskMessage").remove();
            }
        }
    };
    window.maskWindow = maskWindow;
})();
var maskWindow = new maskWindow();




//对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
};
//替换
String.prototype.replaceAll = function (oldstr, newstr) {
    return this.toString().replace(new RegExp(oldstr, "gm"), newstr);
}

/*
Usage 1: define the default prefix by using an object with the property prefix as a parameter which contains a string value; {prefix: 'id'}
Usage 2: call the function jQuery.uuid() with a string parameter p to be used as a prefix to generate a random uuid;
Usage 3: call the function jQuery.uuid() with no parameters to generate a uuid with the default prefix; defaul prefix: '' (empty string)

$.uuid();

*/
/*
Generate fragment of random numbers
*/
jQuery._uuid_default_prefix = '';
jQuery._uuidlet = function () {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};
/*
Generates random uuid
*/
jQuery.uuid = function (p) {
    if (typeof (p) == 'object' && typeof (p.prefix) == 'string') {
        jQuery._uuid_default_prefix = p.prefix;
    } else {
        p = p || jQuery._uuid_default_prefix || '';
        return (p + jQuery._uuidlet() + jQuery._uuidlet() + "-" + jQuery._uuidlet() + "-" + jQuery._uuidlet() + "-" + jQuery._uuidlet() + "-" + jQuery._uuidlet() + jQuery._uuidlet() + jQuery._uuidlet());
    };
};


jQuery.fn.outerHTML = function (s) {
    return (s) ? this.before(s).remove() : jQuery("<p>").append(this.eq(0).clone()).html();
}


/*
 checkbox 处理

 checkbox 如果没有选中 $('#form').serializeArray() 序列化会忽略当前表单对象的值
 本程序会自从创建一个 hidden， 选中、未选中的值会更新到 hidden，在表单序列化的时候，会自动包含并提交到后台
 选中为1，未选中为0，${checkvalue} 值可以为空，空会处理为 0
 checkbox 外面必须存在一个 lable 标签用于区分不同的 checkbox

 <label>
    <input type="checkbox" name="checkbox1" value="${checkvalue}"> Check me out
 </label>

 使用方式
 $(function(){
    $('input[type="checkbox"]').checkbox();
 })
 */
jQuery.fn.checkbox = function () {
    this.each(function () {
        var checkbox = this;
        var this_name = $(checkbox).attr('name');
        var this_value = $(checkbox).attr('value');

        //创建同name的hidden对象
        $(checkbox).before('<input name="' + this_name + '" type="hidden" value="' + this_value + '" />');
        //删除元对象name属性, 避免提交数组数据
        $(checkbox).removeAttr("name");
        //根据值初始化选中状态
        if (this_value == '1') {
            $(checkbox).prop("checked", true);
        }
        else {
            $(checkbox).prop("checked", false);
        }

        $(checkbox).parent().mouseup(function (e) {
            var hidden = $(this).find('input[type="hidden"]');
            if ($(checkbox).prop('checked')) {
                $(checkbox).attr("value", '0');
                hidden.attr("value", '0');
            }
            else {
                $(checkbox).attr("value", '1');
                hidden.attr("value", '1');
            }
            e.stopPropagation();
            return;
        })
    })
}



/*

 radiolist 处理

 根据配置的 data-options 自动批量初始化 radio

 <input id="source_type" name="source_type" type="hidden" value="${model.source_type}"
         data-options="[{'id': 0, 'text':'客户电话申报'},
         {'id': 1, 'text':'客户邮件申报'},
         {'id': 2, 'text':'客户qq申报'},
         {'id': 3, 'text':'主动发现'}]" />


 <script type="text/javascript">
 $(function(){
    $('#source_type').radiolist();
 })
 </script>

 */
jQuery.fn.radiolist = function () {
    var options = this.data('options');
    //单引号替换为双引号
    options = options.replace(new RegExp("'","gm"),"\"");
    options = options.replace(new RegExp("\t","gm"),"");
    options = options.replace(new RegExp("\r","gm"),"");
    options = options.replace(new RegExp("\n","gm"),"");
    options = $.parseJSON(options);

    //初始化 radio list
    var id = this.attr('id');
    for(i=0; i< options.length; i++){
        $('<label> <input type="radio" name="ui_' + id + '" class="' + id + '" data-key="' + options[i].id + '" />'+ options[i].text +' </label>').insertBefore(this);
    }

    var defaultValue = this.attr('value');
    //初始化默认值
    $("input[name='ui_" + id +"']").each(function(){
        if ($(this).data('key') == defaultValue){
            $(this).prop('checked', true);
        }
    })

    //更新选中的值
    $('.'+id).click(function(){
        $('#'+id).val($(this).data('key'));
    });
}


$.ajaxPrefilter(function (options) {
    var originalUrl = options.url;
    if (originalUrl.indexOf('?') > 0) {
        options.url = originalUrl + "&ajaxPrefilter=" + new Date().getTime();
    } else {
        options.url = originalUrl + "?ajaxPrefilter=" + new Date().getTime();
    }
});


/*
    打印相关
*/
function PageSetup(name, value) {
    var Wsh = new ActiveXObject("WScript.Shell");
    Wsh.RegWrite("HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\" + name, value);
}

function openPrintWindow(url, msg) {
    var owner = null;
    owner = window.open("", "打印窗口", "height=50, width=50, top=0, left=0, menubar=no, location=no, scrollbars=no, resizable=no, status=no");
    //owner = window.open("", "打印窗口");
    owner.window.document.title = "打印窗口";
    owner.location = url + "&Key=" + $.uuid();
    setTimeout(function () { execVBPrint(owner) }, 2000);
}

function execVBPrint(owner) {
    try {
        if (document.readyState == "complete") {
            if (!$.support.leadingWhitespace) {
                try {
                    //页面设置
                    PageSetup('header', '');
                    PageSetup('footer', '');
                    PageSetup('margin_left', 0.27559);      //6     0.23622
                    PageSetup('margin_right', 0.27559);     //7     0.27559
                    PageSetup('margin_bottom', 0.27559);    //8.13  0.32000
                    PageSetup('margin_top', 0.27559);
                }
                catch (e) {
                    alert('页面设置失败，请配置浏览器安全中的 Activex 控件和插件权限。');
                }
            }

            setTimeout(function () {
                //关闭打印窗口，仅适用IE6、7、8浏览器
                try{
                    owner.opener = null;
                    owner.open('', '_self');
                    owner.close();
                    owner = null;
                }
                catch(e){
                    console.warn(e);
                }
            }, 1000);

            if (!+[1, ]) {
                //直接打印，不显示打印设置窗口，仅适用IE6、7、8浏览器
                //vbPrintPage(6, 2, 3);
                execScript(n = "vbPrintPage 6, 2, 3", "vbscript");
                //注意：vbscript 后面的js脚本不会被执行
            } else {
                //IE9及以上IE版本
                document.WebBrowser.ExecWB(6, 6);
            }
        }
        else {
            //如果页面没有加载完毕，下一秒重新尝试打印
            setTimeout(function () { execVBPrint(owner) }, 1000);
        }
    }
    catch (e) {
        console.warn(e);
    }
}



/*
     easyui 验证函数扩展
 */
try{
    $.extend($.fn.validatebox.defaults.rules, {
        minLength: {
            validator: function(value, param){
                return value.length >= param[0];
            },
            message: '请至少输入 {0} 个字符'
        },
        equals: {
            validator: function(value, param){
                return value == $(param[0]).val();
            },
            message: '验证失败'
        },
        maxThan: {
            validator: function (value, param) {
                var s = $(param[0]).datebox('getValue');
                //alert(s);
                return value >= s;
            },
            message: '验证失败'
        }
    });
}
catch (e){
    //console.warn(e);
}


try{
    //combotree 取消选择 示例
    //var t = $('#tree').combotree('tree');
    //var n = t.tree('getSelected');
    //if (n != null) {
    //    t.tree("unSelect", n.target);
    //}
    $.extend($.fn.tree.methods, {
        unSelect: function (jq, target) {
            return jq.each(function () {
                $(target).removeClass("tree-node-selected");
            });
        }
    });
}
catch (e){
    //console.warn(e);
}


/*
 清除查询区域输入的值
 */
function clearValue() {
    //代码放在下面，会影响到 combobox 的隐藏域
    $('input[type="hidden"]').each(function () {
        $(this).val('');
    });
    $('.easyui-textbox').textbox('clear');
    $('.easyui-datebox').each(function () {
        $(this).datebox('setValue', '');
    });
    $('.easyui-combobox').each(function () {
        var data = $(this).combobox('getData');
        $(this).combobox('setValue', data[0].value);
    });
    $('.easyui-combotree').each(function () {
        var t = $(this).combotree('tree');
        var n = t.tree('getSelected');
        if (n != null) {
            t.tree("unSelect", n.target);
        }
        $(this).combotree('clear');
    });
}



/*
    全局方法 Vue.filter() 注册自定义过滤器, 必须放在Vue实例化前面
*/
try {
    Vue.filter("formatJsonDate", function (value) {
        return utils.formatJsonDate(value);
    });

    Vue.filter("formatBoolean", function(value, trueTitle, falseTitle) {
        return utils.formatBoolean(value, trueTitle, falseTitle);
    });
}
catch(e){

}

/*
    vue datagrid

    //一个页面如果存在多个表格，需要定义多个变量
    var datagrid1 = new datagrid();
    $(function() {
        //初始化配置
        datagrid1.init('#grid', {
            url: '<%=path%>/demo/easyui/load',
            sortName: 'createtime',
            sortOrder: 'asc',
            pageSize: 10,
            columns: [
                {'field': 'loginname', 'title': '登录名'},
                {'field': 'orgcode', 'title': '机构代码'},
                {'field': 'gender', 'title': '性别'},
                {'field': 'createtime', 'title': '创建时间'}
            ]
        });

        //页面加载完毕默认执行
        reloadGrid();
    })

    function reloadGrid(){
        //传入查询参数
        var jsonObj = $('#form-search').serializeArray();
        //查询数据
        datagrid1.reload(jsonObj);
    }
 */


; (function () {
    var datagrid = function () {
        var _grid = null;

        this.init = function (target, options) {

            // options 参数
            // url: '<%=path%>/demo/easyui/load',
            //     sortName: 'createtime',
            //     sortOrder: 'asc',
            //     pageSize: 10,
            //     columns: [
            //     {'field': 'loginname', 'title': '登录名'},
            //     {'field': 'orgcode', 'title': '机构代码'},
            //     {'field': 'gender', 'title': '性别'},
            //     {'field': 'createtime', 'title': '创建时间'}
            // ]

            options.sortOrders = [];
            options.total = 0;
            options.pageCount = 0;
            options.pageNumber = 0;
            options.rows = [];
            options.queryKey ='';
            options.queryJson = '';

            _grid = new Vue({
                el: target,
                data: options,
                methods: {
                    sortBy: function (column) {
                        var _this = this;

                        function updateSortOrders(field, order) {
                            for (var i = 0; i < _this.columns.length; i++) {
                                if (_this.columns[i].field == field) {
                                    _this.sortOrders.splice(i, 1, order);
                                    _this.sortOrder = order;
                                    break;
                                }
                            }
                        }

                        var _index = 0;
                        for (var i = 0; i < _this.columns.length; i++) {
                            if (_this.columns[i].field != column.field) {
                                //取消其他字段排序
                                _this.sortOrders.splice(i, 1, '');
                            }
                            else {
                                //当前排序字段索引
                                _index = i;
                            }
                        }

                        this.sortName = column.field;
                        if (this.sortOrders[_index] == 'asc') {
                            updateSortOrders(this.sortName, 'desc');
                        }
                        else {
                            updateSortOrders(this.sortName, 'asc');
                        }
                        this.reload();
                    },
                    init: function () {
                        var _this = this;
                        for (var i = 0; i < _this.columns.length; i++) {
                            if (_this.columns[i].field == _this.sortName) {
                                _this.sortOrders.splice(i, 1, _this.sortOrder);
                            }
                            else {
                                _this.sortOrders.splice(i, 1, '');
                            }
                        }
                        this.reload();
                    },
                    reload: function (params) {
                        var jsonObj = [];
                        jsonObj.push({'name': 'page', 'value': this.pageNumber});
                        jsonObj.push({'name': 'rows', 'value': this.pageSize});
                        jsonObj.push({'name': 'sort', 'value': this.sortName});
                        jsonObj.push({'name': 'order', 'value': this.sortOrder});
                        if(typeof(params) != "undefined") {
                            var PageCount = {};
                            PageCount.name = 'PageCount';
                            PageCount.value = this.total;
                            params.push(PageCount);

                            var QueryKey = {};
                            QueryKey.name = 'QueryKey';
                            QueryKey.value = this.queryKey;
                            params.push(QueryKey);
                            var jsonStr = $.toJSON(params);
                            this.queryJson = jsonStr;
                            jsonObj.push({'name': 'query', 'value': jsonStr});
                        }
                        else{
                            jsonObj.push({'name': 'query', 'value': this.queryJson});
                        }

                        var _data = [];
                        $.ajax({
                            type: 'post',
                            url: this.url,
                            data: jsonObj,
                            async: false,
                            success: function (data) {
                                _data = data;
                            }
                        });

                        this.rows = _data.rows;
                        this.total = _data.total;
                        this.queryKey = _data.queryKey;
                        this.pageCount = Math.ceil(this.total / this.pageSize);
                        if (this.pageCount == 0) {
                            this.pageNumber = 0;
                        } else if (this.pageNumber == 0) {
                            this.pageNumber = 1;
                        }
                    },
                    first: function () {
                        this.pageNumber = 1;
                        this.reload();
                    },
                    prior: function () {
                        this.pageNumber = parseInt(this.pageNumber) - 1;
                        if (this.pageNumber < 1) {
                            this.pageNumber = 1;
                        }
                        this.reload();
                    },
                    next: function () {
                        this.pageNumber = parseInt(this.pageNumber) + 1;
                        if (this.pageNumber > this.pageCount) {
                            this.pageNumber = this.pageCount;
                        }
                        this.reload();
                    },
                    last: function () {
                        this.pageNumber = this.pageCount
                        this.reload();
                    },
                    go: function () {
                        if (this.pageNumber > this.pageCount) {
                            this.pageNumber = this.pageCount;
                        }
                        this.reload();
                    }
                }
            });
        };
        this.reload = function (params) {
            _grid.reload(params);
        }
    };
    window.datagrid = datagrid;
})();



/*

 //子页面处理父页面对象
 $('#objid', window.parent.document).attr('src', '/fsopr1/portal/layout/standard/404.jsp?t=' + $.uuid());

 //父页面调用子页面的方法
 $('#mainFrame')[0].contentWindow.reloadGrid();

 //父页面通过子页面的jquery选择器处理页面对象
 $('#mainFrame')[0].contentWindow.$('input[data-foreignid="0bde641b-fe18-4f15-b24e-e04dbe306223"]')

 */



var constant = {
    "Loading" :"正在处理中，请稍候...",
    "OnlyOneSelected":"请至少选择一行记录！",
    "ConfirmDelete":"确认要删除吗？",
    "ConfirmSubmit" : "确认要提交吗？",
    "ConfirmArchive":"确认要归档吗？",
    "FailedToSubmitOnNetwork" : "提交失败，您的网速较慢请重新尝试提交。当前状态：",
    "FailedToSubmit":"提交失败：",
    "FailedToDeleteOnNetwork":"删除失败，您的网速较慢请重新尝试删除。当前状态：",
    "FailedToDelete":"删除失败：",
    "FailedToArchiveOnNetwork":"归档失败，您的网速较慢请重新尝试归档。当前状态：",
    "FailedArchive":"归档失败：",
    "ResizeTime":100
}

function isCardID(sId){
    var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};

    var iSum=0 ;
    var info="" ;
    //身份证长度或格式错误
    if(!/^\d{17}(\d|x)$/i.test(sId)) return false;
    sId=sId.replace(/x$/i,"a");
    //身份证地区非法
    if(aCity[parseInt(sId.substr(0,2))]==null) return false;
    var sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
    var d=new Date(sBirthday.replace(/-/g,"/")) ;
    //身份证上的出生日期非法
    if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())) return false;
    //输入的身份证号非法
    for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
    if(iSum%11!=1) return false;
    //aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
    return true;
}

var chars = ['0','1','2','3','4','5','6','7','8','9'];

function generateMixed(n) {
    var res = "";
    for(var i = 0; i < n ; i ++) {
        var id = Math.ceil(Math.random()*9);
        res += chars[id];
    }
    return res;
}
