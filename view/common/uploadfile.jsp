<%@ page import="java.util.UUID" %>
<%@ page import="com.sten.framework.model.UploadFile" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.owasp.esapi.ESAPI" %>
<%@ page import="org.owasp.esapi.errors.ValidationException" %><%--
  Created by IntelliJ IDEA.
  User: linkai
  Date: 16-1-18
  Time: 12:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String newUuid=UUID.randomUUID().toString();
    String localid = newUuid.replace("-","");
    String path = request.getContextPath();
    String foreignid = request.getParameter("foreignid");
    System.out.println(foreignid);
    String category = request.getParameter("category");
    if(category == null){
        category = "";
    }
    String multiple = request.getParameter("multiple");
    String allowedExtensions = request.getParameter("allowedExtensions");
    if(allowedExtensions == null || allowedExtensions.length() == 0){
        allowedExtensions = "[]";
    }
    String sizeLimit = request.getParameter("sizeLimit");
    if(sizeLimit == null){
        sizeLimit = "20480000"; //20M
    }
    //getAttribute 获取复杂对象, 通过后台 java 代码 mv.addObject("list", list) 赋值
    //getParameter 获取简单变量, 通过前台 jsp 代码 <jsp:param name="list" value="" /> 赋值
    List<UploadFile> list = (List<UploadFile>)request.getAttribute("list");

    String ids = "";
    if(list != null){
        for(int i=0; i< list.size(); i++){
            if(category.equals(list.get(i).getCategory())) {
                ids += list.get(i).getFileId() + ",";
            }
        }
    } else {
        list = new ArrayList<UploadFile>();
    }

    String _readOnly = request.getParameter("readOnly");
    boolean readOnly = true;
    if(_readOnly != null  && _readOnly.length()>0 ) {
        readOnly = Boolean.parseBoolean(_readOnly);
    }

    String onComplete = request.getParameter("onComplete");
%>

<input type="hidden"
       id="fileids_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>"
       data-foreignid="<%=ESAPI.encoder().encodeForHTMLAttribute(foreignid)%>"
       data-category="<%=ESAPI.encoder().encodeForHTMLAttribute(category)%>"
       value="<%=ESAPI.encoder().encodeForHTMLAttribute(ids)%>" />

<div id="fine-uploader_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>"></div>
<%if(!readOnly) {%>
<script type="text/template" id="qq-template_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>">
    <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="">
        <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
            <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
        </div>
        <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
            <span class="qq-upload-drop-area-text-selector"></span>
        </div>
        <div class="qq-upload-button-selector qq-upload-button">
            <div><img src="<%=path%>/includes/images/upload.png"/></div>
        </div>
        <span class="qq-drop-processing-selector qq-drop-processing">
            <span>处理拖动文件...</span>
            <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
        </span>
        <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals" style="display:none">
            <li>
                <div class="qq-progress-bar-container-selector">
                    <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                </div>
                <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                <img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale>
                <span class="qq-upload-file-selector qq-upload-file"></span>
                <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                <span class="qq-upload-size-selector qq-upload-size"></span>
                <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">取消</button>
                <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">重试</button>
                <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">删除</button>
                <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
            </li>
        </ul>

        <dialog class="qq-alert-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">关闭</button>
            </div>
        </dialog>

        <dialog class="qq-confirm-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">否</button>
                <button type="button" class="qq-ok-button-selector">是</button>
            </div>
        </dialog>

        <dialog class="qq-prompt-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <input type="text">
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">取消</button>
                <button type="button" class="qq-ok-button-selector">确定</button>
            </div>
        </dialog>
    </div>
</script>
<%}%>
<div id="div_file_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>" style="margin-bottom:20px; display:none;">
       <input type="file" name="file_upload_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>" id="file_upload_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>" />
</div>
<div>
<ul id="upload-file-list_<%=ESAPI.encoder().encodeForHTMLAttribute(localid)%>" class="upload-file-list">
<%
    for(int i=0; i< list.size(); i++){
        if(foreignid.equals(list.get(i).getForeignId()) && category.equals(list.get(i).getCategory())){
%>
            <li id="li_<%=list.get(i).getFileId()%>">
                <a href="<%=path%>/upload/download/<%=list.get(i).getFileId()%>" target="_blank" class="filename">
                    <span><%=list.get(i).getFileName()%><%=list.get(i).getExtension()%></span>
                    <span>[<%=list.get(i).getFileSize()%>]</span>
                </a>
                <%if(!readOnly) {%>
                <a href="javascript:void(0)" onclick="deleteFile_<%=localid%>('<%=list.get(i).getFileId()%>')">删除</a>
                <%}%>
            </li>
<%
        }
    }
%>
</ul>
</div>
<%if(!readOnly) {%>
<script type="text/javascript" charset="UTF-8">
    $(function () {
        var uploader = new qq.FineUploader({
            debug: true,
            template: "qq-template_<%=localid%>",
            element: document.getElementById('fine-uploader_<%=localid%>'),
            request: {
                endpoint: '<%=path%>/upload/file',
                params: {
                    'foreignId': '<%=ESAPI.encoder().encodeForJavaScript(foreignid)%>',
                    'category': '<%=ESAPI.encoder().encodeForJavaScript(category)%>',
                    'allowedExtensions': <%=ESAPI.validator().getValidInput("javascript", allowedExtensions, "JS", 512, false)%>
                },
                paramsInBody: true
            },
            validation: {
                //allowedExtensions: ['jpeg', 'jpg', 'gif', 'png'],
                allowedExtensions: <%=ESAPI.validator().getValidInput("javascript", allowedExtensions, "JS", 512, false)%>,
                //sizeLimit: 20480000 // 20M
                sizeLimit: <%=ESAPI.encoder().encodeForJavaScript(sizeLimit)%>
            },
            deleteFile: {
                enabled: true,
                endpoint: '<%=path%>/upload/delete?qquuid=',
                forceConfirm: true
            },
            retry: {
                enableAuto: true
            },
            multiple: <%=ESAPI.encoder().encodeForJavaScript(multiple)%>,
            callbacks: {
                onSubmit: function (id, fileName) {

                },
                onUpload: function (id, fileName) {

                },
                onComplete: function (id, fileName, data) {

                    if (data.errormsg != '') {
                        window.top.$.messager.alert('提示', data.errormsg);
                        return;
                    }

                    <%
                    String safeOnComplete = "";
                    try {
                        if(!"".equals(onComplete)){
                            safeOnComplete = ESAPI.validator().getValidInput("javascript", onComplete, "JS", 512, false);
                        }
                    }
                    catch (ValidationException e) {
                        e.printStackTrace();
                    }
                    %>
                    <%=safeOnComplete%>

                    if(<%=ESAPI.encoder().encodeForJavaScript(multiple)%>) {
                        var ids = $('#fileids_<%=localid%>').val();
                        ids = ids + data.qquuid + ",";
                        $('#fileids_<%=localid%>').val(ids);
                    }
                    else{
                        $('#fileids_<%=localid%>').val(data.qquuid);
                        $("#upload-file-list_<%=localid%> li").remove();
                    }

                    var html = "<li id=\"li_" + data.qquuid + "\">" +
                            "   <a href=\"<%=path%>/upload/download/" + data.qquuid + "\" target=\"_blank\" class=\"filename\"><span>" + data.qqfilename + " [" + data.qqtotalfilesize + "]</span></a>" +
                            "   <a href=\"javascript:void(0)\" onclick=\"deleteFile_<%=localid%>('" + data.qquuid + "')\">删除</a>" +
                            "</li>";
                    $("#upload-file-list_<%=localid%>").append(html);
                }
            }
        });
    	
    });

	
    function deleteFile_<%=localid%>(qquuid) {
        window.top.$.messager.confirm('提示', '确定删除吗?', function (value) {
            if (value) {

                var ids = $('#fileids_<%=localid%>').val();
                var idarray = ids.split(",");
                ids = "";
                for(i=0; i< idarray.length; i++){
                    if(idarray[i] == qquuid || idarray[i] == "") continue;
                    ids += idarray[i] + ",";
                }
                $('#fileids_<%=localid%>').val(ids);

                $('#li_' + qquuid).remove();

                <%--$.ajax({--%>
                <%--url: '<%=path%>/upload/delete?fileid=' + qquuid,--%>
                <%--type: 'POST',--%>
                <%--success: function (data, status) {--%>
                <%--if (data.result) {--%>
                <%--$('#ul_' + qquuid).remove();--%>
                <%--}--%>
                <%--}--%>
                <%--});--%>
            }
        });
        return false;
    }

</script>
<%}%>