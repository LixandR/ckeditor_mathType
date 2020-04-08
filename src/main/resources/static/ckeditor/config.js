/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

// CKEDITOR.editorConfig = function( config ) {
// 	// Define changes to default configuration here. For example:
// 	// config.language = 'fr';
// 	// config.uiColor = '#AADC6E';
// 	config.extraPlugins = 'ckeditor_wiris'; 
// };

CKEDITOR.editorConfig = function (config) {
	// Define changes to default configuration here.
	// For complete reference see:
	// https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		// { name: 'clipboard', groups: ['clipboard', 'undo'] },
		// { name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		// { name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'others' },
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		// { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi'] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'wiris', groups: ['ckeditor_wiris_formulaEditor', 'ckeditor_wiris_formulaEditorChemistry', 'ckeditor_wiris_CAS'] },
		{ name: 'about', item: ['ckeditor_wiris_formulaEditor', 'ckeditor_wiris_formulaEditorChemistry', 'ckeditor_wiris_CAS'] }
	];
	//mathType的配置
	// config.mathTypeParameters = {
	//     serviceProviderProperties : {
	//         // URI : '/pluginwiris_engine/app/configurationjs',
	//         URI : '/pluginwiris_engine/app/configurationjs',
	//         server : 'java'
	//     }
	// };

	// 保留CKEditor空白<u></u>
	config.protectedSource.push(/<u[^>]><\/u>/g);
	CKEDITOR.dtd.$removeEmpty['u'] = false;
	config.allowedContent = true;
	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';
	config.height = 411 /*编辑器高度*/
	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';
	// config.language = 'zh-cn';/*将编辑器的语言设置为中文*/
	config.language = 'en';/*将编辑器的语言设置为英文*/
	// config.image_previewText = ' ';/*去掉图片预览框的文字*/
	// Simplify the dialog windows.
	// config.extraPlugins = 'image2, uploadimage, ckeditor_wiris'; // 图片插件
	config.extraPlugins = 'ckeditor_wiris'; // mathtype
	// config.extraPlugins = 'uploadimage';
	// config.imageUploadUrl = '/ckeditor/UploadImage';
	/*开启工具栏“图像”中文件上传功能，后面的url为图片上传要指向的的action或servlet*/
	// config.filebrowserImageUploadUrl = "/ckeditor/UploadImage";
	config.removeDialogTabs = 'image:advanced;link:advanced';

	config.mathJaxLib = 'http://convertpdf.yoko100.com/static/mathjax/MathJax.js?config=TeX-MML-AM_CHTML';
	config.extraPlugins += (config.extraPlugins.length == 0 ? '' : ',') + 'ckeditor_wiris';
	// CKEDITOR.plugins.addExternal('ckeditor_wiris', 'https://ckeditor.com/docs/ckeditor4/4.12.1/examples/assets/plugins/ckeditor_wiris/', 'plugin.js')
	// CKEDITOR.plugins.addExternal('ckeditor_wiris', 'https://www.wiris.net/demo/plugins/ckeditor/', 'plugin.js');;
	config.allowedContent = true
	// 快捷键
	config.keystrokes = [
		[CKEDITOR.ALT + 121 /*F10*/, 'toolbarFocus'],  //获取焦点
		[CKEDITOR.ALT + 122 /*F11*/, 'elementsPathFocus'],  //元素焦点
		[CKEDITOR.SHIFT + 121 /*F10*/, 'contextMenu'],  //文本菜单
		[CKEDITOR.CTRL + 90 /*Z*/, 'undo'],  //撤销
		[CKEDITOR.CTRL + 89 /*Y*/, 'redo'],  //重做
		[CKEDITOR.CTRL + CKEDITOR.SHIFT + 90 /*Z*/, 'redo'],  //
		[CKEDITOR.CTRL + 76 /*L*/, 'link'],  //链接
		[CKEDITOR.CTRL + 66 /*B*/, 'bold'],  //粗体
		[CKEDITOR.CTRL + 73 /*I*/, 'italic'],  //斜体
		[CKEDITOR.CTRL + 85 /*U*/, 'underline'],  //下划线
		[CKEDITOR.ALT + 109 /*-*/, 'toolbarCollapse']
	]
};