/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
    config.toolbarGroups  = [
        { name: 'document', groups: [ 'doctools',  'document' ] },
        { name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
        { name: 'forms', groups: [ 'forms' ] },
        { name: 'insert', groups: [ 'insert' ] },
        { name: 'links', groups: [ 'links', 'colors' ] },
        { name: 'tools', groups: [ 'tools', 'mode' ] },

        '/',
        { name: 'basicstyles', groups: [  'styles', 'basicstyles', 'cleanup' ] },
        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
        '/',
        { name: 'others', groups: [ 'others' ] },
        { name: 'about', groups: [  'about' ] }
    ];

    config.format_tags = 'p;h1;h2;h3;h4;h5;h6';                 //格式
    config.removeDialogTabs = 'image:advanced;image:Link';      //移除图片上传页面的'高级','链接'页签
    config.image_prefillDimensions = false;                     //禁止图片上传完毕后自动填充图片长和宽
    //config.image_previewText = ' ';                           //图片信息面板预览区内容的文字内容，默认显示CKEditor自带的内容
    config.extraPlugins = 'autogrow,tableresize';
    config.removePlugins= 'resize';

    //config.removeButtons = 'Checkbox,Form,Radio,TextField,Textarea,Select,Button,ImageButton,
    // HiddenField,BidiLtr,BidiRtl,Language,Flash,Iframe,About,Scayt,Print,Font,CopyFormatting,
    // Anchor,Smiley,Preview,Save,Templates,SelectAll,Styles,FontSize,Cut,Copy,Paste,
    // Find,Superscript,Subscript,Underline,Italic,Bold,RemoveFormat,BulletedList,NumberedList,
    // Outdent,CreateDiv,Indent,JustifyLeft,Blockquote,JustifyCenter,JustifyRight,JustifyBlock,
    // Unlink,Link,Image,Table,HorizontalRule,SpecialChar,BGColor,TextColor,Maximize,ShowBlocks';
    config.removeButtons = 'Replace,PageBreak,Checkbox,Form,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,BidiLtr,BidiRtl,Language,Flash,Iframe,About,Scayt,Print,Font,CopyFormatting,CreateDiv,Anchor,Smiley,Preview,Save,Templates,SelectAll,Styles,FontSize,Cut,Copy,Paste';
};