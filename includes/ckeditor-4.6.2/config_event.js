/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
    config.toolbar  = [
        { name: 'clipboard', items: [  'PasteText', 'Image', '-', 'Undo', 'Redo' ] },
        { name: 'basicstyles', items: [  'Bold', 'Italic', 'Strike' ] },
        { name: 'paragraph', items: [  'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent' ] },
        { name: 'document', items: [ 'ShowBlocks', 'Source' ] }
    ];

    config.format_tags = 'p;h1;h2;h3;h4;h5;h6';                 //格式
    config.removeDialogTabs = 'image:advanced;image:Link';      //移除图片上传页面的'高级','链接'页签
    config.image_prefillDimensions = false;                     //禁止图片上传完毕后自动填充图片长和宽
    config.extraPlugins = 'autogrow,tableresize';
    config.removePlugins= 'resize';


    // { name: 'document', items: ['Source', '-', 'Preview', 'Print', '-'] },
    // { name: 'clipboard', items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo'] },
    // { name: 'editing', items: ['Find', 'Replace', '-', 'Styles'] },
    // '/',
    // { name: 'basicstyles', items: ['Bold', 'Italic', 'Underline', 'Strike', '-', 'RemoveFormat'] },
    // { name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'] },
    // { name: 'links', items: ['Link', 'Unlink'] },
    // { name: 'insert', items: ['Image', 'Flash', 'Table'] },
    // { name: 'tools', items: ['Maximize', 'ShowBlocks'] }

    //config.removeButtons = 'Checkbox,Form,Radio,TextField,Textarea,Select,Button,ImageButton,
    // HiddenField,BidiLtr,BidiRtl,Language,Flash,Iframe,About,Scayt,Print,Font,CopyFormatting,
    // Anchor,Smiley,Preview,Save,Templates,SelectAll,Styles,FontSize,Cut,Copy,Paste,
    // Find,Superscript,Subscript,Underline,Italic,Bold,RemoveFormat,BulletedList,NumberedList,
    // Outdent,CreateDiv,Indent,JustifyLeft,Blockquote,JustifyCenter,JustifyRight,JustifyBlock,
    // Unlink,Link,Image,Table,HorizontalRule,SpecialChar,BGColor,TextColor,Maximize,ShowBlocks';
};