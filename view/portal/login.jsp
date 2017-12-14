<%@ page import="java.security.SecureRandom" %><%--
  Created by IntelliJ IDEA.
  User: linkai
  Date: 16-2-26
  Time: 20:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();

    String jitangs = "又一天过去了，今天过的怎么样啊?梦想是不是更远了。\n" +
        "善良没用，你得漂亮。\n" +
        "人作的程度不能超过自己的颜值。\n" +
        "谁说女追男隔层网，除非那男的本来就对你有好感，不然隔的基本都是铁丝网，还是带电的那种。\n" +
        "经过十年不断的努力和奋斗，我终于从一个懵懂无知的少年变成了一个懵懂无知的青年。\n" +
        "只要你每天坚持自习，认真刻苦，态度端正，忍受孤独，最终的胜利肯定是属于那些考场上发挥好的人。\n" +
        "你以为有了钱就会像你想象中那样快乐吗?不，你错了。有钱人的快乐，你根本想象不到。\n" +
        "灰姑娘嫁给了王子，但请不要忘记她也是伯爵的女儿。\n" +
        "知道为什么自古红颜多薄命吗?因为没有人在意丑的人活多久。\n" +
        "等忙完这一阵，就可以接着忙下一阵了。\n" +
        "你以为只要长得漂亮就有男生喜欢?你以为只要有了钱漂亮妹子就自己贴上来了?你以为学霸就能找到好工作?我告诉你吧，那这些都是真的!\n" +
        "长得丑就是病，不然整形医院为什么叫医院。\n" +
        "岁月是把杀猪刀，是针对那些好看的人，它对长得丑的人一点办法都没有。\n" +
        "有时候你不努力一下你都不知道什么叫绝望。\n" +
        "年轻人嘛，现在没钱算什么，以后没钱的日子还长着呢。\n" +
        "当你觉得自己又丑又穷，一无是处时，不要灰心也不要绝望，因为至少你的判断是对的。\n" +
        "“为什么我总是感觉自己特别普通?”“可能是因为你确实比较普通吧。”\n" +
        "很多人不是心理疾病，而是心理残疾，治不好的。\n" +
        "我发现很多混得不好的人看得都很开。也不知道他们是因为看得透彻而不屑于世俗的成功，还是因为不成功而不得不看得开。\n" +
        "除了有钱人，世上还有两种人：其一是省吃俭用买奢侈品装逼，其二是省吃俭用也买不起奢侈品的。\n" +
        "一场说走就走的旅行归来后，除了该做的事情被拖延的更久了，什么都没有改变。\n" +
        "你努力后的成功，不能弥补你成功前的痛苦。\n" +
        "假如今天生活欺骗了你，不要悲伤，不要哭泣，因为明天生活还会继续欺骗你。\n" +
        "要是有个地方能出卖自己的灵魂换取物质享受就好了。\n" +
        "好身材的因素很多。不是节食和锻炼就能拥有好身材的。\n" +
        "每天显得无聊或寂寞了，去找朋友一起吃饭和逛，只不过是为了满足自己与人交往的需求，算不上是社交。\n" +
        "优秀的女生在脆弱的时候(比如分手，工作不如意)，会需要比平时更多的关爱，于是会和更多的人交流。如果她在恢复正常后，回到了自己原来的圈子，不再理你，请你不要奇怪。\n" +
        "朋友，那不是懒，懒是可以克服的。你只是脑子比较弱(笨)，没办法长时间经受高强度的思考，去搞逻辑太复杂的东西和处理太多的信息量。\n" +
        "有些年轻人，一毕业就到某些大型国企和机关中工作。每天没什么事儿，就是吃饭喝酒，福利好得不得了。人生还没有奋斗过就开始养老，自己的理想被丢在一旁，用民脂民膏来享受特权。对于这样的年轻人，我只想问你们四个字：哪投简历?\n" +
        "所有抱怨社会不公和制度的人翻译过来只有一句话：请给我金钱，女人和社会地位。\n" +
        "常听到别人说：我希望他/她有什么话当面说，不要在别后中伤人。一些人信以为真，而实际上，我的实践证明，当面说别人坏话，别人会非常愤怒，难堪。所以中伤别人一定要在背后。\n" +
        "其实找谁做女朋友都差不多，都是在不停地争吵。只不过一些人是和比较漂亮的女孩子在争吵。\n" +
        "爱情开始时都差不多。但当两个人平淡到左手牵右手时，是加班挤地铁还房贷然后给他农村父母寄钱假期在屋里大眼瞪小眼，还是开小车朝九晚五住大房子周末采购装点自己的小家出国旅游，区别就非常大了。\n" +
        "坏女人爱男人的钱和权;好女人爱男人因有钱和有权儿产生的自信宽大精力充沛乐观进取。还好，殊途同归。\n" +
        "精神追求应当是物质追求得到满足后的自然反应。而不是在现实受挫后去寻求的安慰剂。\n" +
        "我的梦想就是一手拿着相机，一手拉着你，四处旅行。每天日落时的歇脚处都是我们的家。然后在三十多岁的时候还在初级职位上拿着微薄的薪水，和刚毕业的年轻人一起被呼来喝去。\n" +
        "又一天过去了。今天过得怎么样，梦想是不是更远了?\n" +
        "只要是石头，到哪里都不会发光的。\n" +
        "我有位家境一般的朋友，一直觉得如果自己有钱一定会更幸福。后来他妈做婴幼教育发财了。快十年后我见他，问：现在你倒是有钱了，你真的幸福吗? 他回答：爽翻啦!我默默地走开了。\n" +
        "只有能力强会被当成纯技术人员;而光会社交拍马又会被认为没有真才实学;所以，要想在单位中脱颖而出，最重要的是有关系。\n" +
        "我发现没有任何一个煤矿工人靠挖煤多又快当上了煤老板。\n" +
        "我有个朋友，在几年前停止了抱怨和自怨自艾，开始努力改变自己。到今天，他的物质生活和精神状态都没有什么改善。\n" +
        "很多时候，乐观的态度和好听的话帮不了你。\n" +
        "回首青春，我发现自己失去了很多宝贵的东西。但我并不难过，因为我知道，以后会失去的更多。\n" +
        "秋天是收获的季节。别人的收获是成功与快乐，你的收获是认识到并不是每个人都会成功与快乐。\n" +
        "父母一直注重对我的品德教育。到了社会上，我按照父母教我的接人待物，却发现自己并不受人待见。\n" +
        "最靠得住的是金钱，最靠不住的是人心。\n" +
        "对今天解决不了的事情，也不必着急。因为明天还是解决不了。\n" +
        "青年靠什么混日子?头等青年靠出身，二等青年靠关系，三等青年靠天资，四等青年靠努力，五等青年耍文艺，六等青年打游戏，穷游，看美剧。\n" +
        "每次看到穷游日志的感受都是：那么穷就别TM出去浪了。";

    String[] array = jitangs.split("\n");
    SecureRandom random = new SecureRandom();
    int index = random.nextInt(array.length - 1);
    String jitang = array[index];
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include flush="true" page="/view/common/resource.jsp"></jsp:include>
    <link href="<%=path%>/includes/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet" />
    <script src="<%=path%>/includes/bootstrap-3.3.7/js/bootstrap.min.js"></script>
    <style>
        body {
            font-family: 'Microsoft YaHei';
            background: url(<%=path%>/includes/images/login-bg2.jpg) no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }

        .container {
            padding-top: 100px;
            height: 500px;
            display: block;
        }

        .row {
            width:80%\9;
            margin:0 auto\9;
        }

        .col-md-6 {
            color: #fff;
            float:left\9;
        }

        .col-md-5{
            float:right\9;
        }

        h1 span{
            padding-left:10px;
            font-size:16px;
            vertical-align: super;
        }

        h6 {
            margin-top: 0;
            font-size: 22px;
            font-weight: normal;
            text-align: center;
            width: 100%;
            margin-bottom: 15px;
        }

        .control-label {
            font-weight: normal;
        }

        #imgValidateCode{
            border-radius: 4px;
        }

        .login {
            width: 300px;
            display: block;
            background-color: #f6f8f9;
            border-radius: 4px;
            padding: 20px;
            position: relative;
            margin-left: 0px;
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="row">
            <div class="col-md-1"></div>

            <div class="col-md-6">
                <h1 class="l-b-margin">技术支持服务平台<span>3.0</span></h1>
                <p style="font-size:16px; margin-top:-8px">
                    IT Support
                </p>
                <p style="font-size:20px;">
                    <span style="font-size:12px;  filter:alpha(opacity=50);">
                        <%=jitang%>
                    </span>
                    <br/><br />
                    <span style="font-size:80%">建议使用Chrome、火狐、IE8以上版本浏览器</span>
                </p>
            </div>
            <div class="col-md-5">
                <div class="login">
                    <form id="form_login" data-parsley-validate="">
                        <div class="form-group">
                            <h6>
                                用户登录
                            </h6>
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="loginname"> 账号</label>
                            <input id="loginname" name="loginname" class="form-control" type="text" data-parsley-trigger="change" data-parsley-required="true" />
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="password"> 密码</label>
                            <input id="password" name="password" class="form-control" type="password" data-parsley-trigger="change" data-parsley-required="true" autocomplete="off"/>
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="validatecode"> 验证码</label>
                            <input id="validatecode" name="validatecode" class="form-control" type="text" data-parsley-trigger="change" data-parsley-required="true" style="width:140px" />
                            <img id="imgValidateCode" src="<%=path%>/portal/validateCode" style="height:34px; float:right; margin-top:-34px"/>
                        </div>
                        <div class="form-group">
                                <label class="checkbox">
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input id="rememberme" type="checkbox" > 记住密码
                                </label>
                        </div>
                        <div class="form-group">
                            <div class="controls">

                                <!---------- message partial begin ---------->
                                <div id="alert" role="alert" class="alert alert-danger alert-dismissible fade in" style="display: none;">
                                    <button class="close" type="button"><span id="alertclose">×</span><span class="sr-only">关闭</span></button>
                                    <h4 id="alertmsg">提示信息</h4>
                                    <p id="alertinfo"></p>
                                </div>

                                <div id="info" role="alert" class="alert alert-success alert-dismissible fade in" style="display: none;">
                                    <button class="close" type="button"><span id="infoclose">×</span><span class="sr-only">关闭</span></button>
                                    <p id="infomsg">操作成功</p>
                                </div>

                                <script type="text/javascript">
                                    $(function () {
                                        $('#alertclose').click(function () {
                                            $('#alert').hide();
                                        })

                                        $('#infoclose').click(function () {
                                            $('#info').hide();
                                        })
                                    });
                                </script>

                                <!---------- message partial end ---------->
                                <button id="btnLogin" type="button" class="btn btn-primary" data-loading-text="Loading..." autocomplete="off" style="width:100%"> 登录 </button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </div>



    <script type="text/javascript">

        $(function () {
            $("#loginname").keyup(function (event) {
                if (event.keyCode == 13 &&
                    $(this).val().length > 0) {
                    $("#password").focus();
                }
            });

            $("#password").keyup(function (event) {
                if (event.keyCode == 13 &&
                    $(this).val().length > 0) {
                    $("#validatecode").focus();
                }
            });

            $("#validatecode").keyup(function (event) {
                if (event.keyCode == 13 &&
                   $(this).val().length > 0) {
                    userlogin();
                }
            });

            $('#btnLogin').click(function () {
                userlogin();
            })

            $('#imgValidateCode').click(function () {
                $(this).attr('src', '<%=path%>/portal/validateCode?t=' + $.uuid());
            })

            $('#alert h4').css('display', 'none');

            $('#loginname').focus();

            if (self != top) {
                top.location.href = self.location.href;
            }

            if($.cookie('rememberme') == 'true'){
                $('#rememberme').prop('checked', true);
                $('#loginname').val($.cookie('loginname'));
                $('#password').val($.cookie('password'));
                $('#validatecode').focus();
            }
        })


        function userlogin() {
            $('#btnLogin').text('正在登录中...');
            $('#btnLogin').attr('disabled', 'disabled');

            if($('#rememberme').prop('checked')){
                $.cookie('rememberme', 'true', { expires: 90 });
                if ($.cookie('loginname') != $('#loginname').val()) {
                    $.cookie('loginname', $('#loginname').val(), { expires: 90 });
                }
                if ($.cookie('password') != $('#password').val()){
                    $.cookie('password', $('#password').val(), { expires: 90 });
                }
            }else{
                $.cookie('rememberme', 'false', { expires: 90 });
            }

            var jsonObj = $('#form_login').serializeArray();
            $.post('<%=path%>/login',
                    jsonObj,
                    function (data, status) {
                        if (status != 'success') {
                            $('#alert').show();
                            $('#btnLogin').text('登录');
                            $('#btnLogin').removeAttr('disabled');
                            $('#alertinfo').html('请检查网络或刷新页面后重新提交');
                        }
                        if (data.result) {
                            window.location.href = '<%=path%>/portal/100';
                        }
                        else {
                            <%--if (data.message == '请输入验证码') {--%>
                                <%--$('#imgValidateCode').attr('src', '<%=path%>/portal/validateCode?t=' + $.uuid());--%>
                            <%--}--%>
                            $('#btnLogin').text('登录');
                            $('#btnLogin').removeAttr('disabled');
                            $('#alert').show();
                            $('#alertinfo').html(data.message);

                            if(data.message == '验证码错误') {
                                $('#validatecode').val('');
                                $('#validatecode').focus();
                            }
                        }
                    });
        }
    </script>
</body>
</html>




