//author:YMQ
//version: 1.0.0
//将grid导出excel

Ext.define('com.ad.button.ExpExcel', {
	extend : 'Ext.Button',
	config : {
		_grid : null
	},
	constructor : function(cfg) {
		this.callParent(arguments);
		this.initConfig(cfg);
		return this;
	},
	iconCls : 'expxsl',
	tooltip : '导出Excel',
	disabled : false,
	itemId : 'button_ExpExcel',
	handler : function() {
		var me = this;
		var store = me._grid.getStore();
		if (!store)return;
		if (Ext.isIE){
			me.ieExpXsl(me._grid.columns ,store);
		}else {
			me.ffExpXsl(me._grid.columns ,store);
		}
	},
	ieExpXsl : function(columns,store) {
		/*
　    try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			Ext.Msg.alert('警告','您所设定的安全级别太高，或者您的电脑没有安装Microsoft Excel软件！');
			return false;
		}
//    	window.clipboardData.setData("Text", "");
		ExWBk.worksheets(1).Paste;
		ExWBk.worksheets(1).Columns.AutoFit;
		ExWBk.worksheets(1).Rows.AutoFit;
		
		*/
		Ext.Msg.show({ 
			        title:"导出Excel", 
			        msg:"确定要将当前数据导出成Excel?<br/>[是]->导出全部列数据。<br/>[否]->只导出显示列的数据。<br/>[取消]->不导出。", 
			        buttons:Ext.Msg.YESNOCANCEL,   
           			icon:Ext.Msg.QUESTION, 
			        fn:function(btn){ 
			            switch(btn){ 
			            case "yes": 
			            	var oXL =null;
			            	try {
			            		oXL = new ActiveXObject("Excel.Application");
			            	}catch (e) {
								alert('您所设定的安全级别太高，或者您的电脑没有安装Microsoft Excel软件！');
								return false;
							}
							var oWB = oXL.Workbooks.Add();
							var oSheet = oWB.ActiveSheet;
							var i =0 ,j = 0;
							for (var v in columns ){
								oSheet.Cells(i + 1, j + 1).Value = columns[v].text ;
								oSheet.Cells(i + 1, j + 1).Borders.Weight = 2;
								j++;
							}
							i = 1;
							j = 0;
							store.each(function(record){
								for (var v in columns ){
									oSheet.Cells(i + 1, j + 1).Value = columns[v]['xtype']=='rownumberer' ? i : record.get(columns[v].dataIndex) ;
									oSheet.Cells(i + 1, j + 1).Borders.Weight = 2;
									j++;
								}
								i++;
								j = 0;
							});
							oXL.Visible = true;
							oXL.UserControl = true;
			                break; 
			            case "no": 
							var oXL =null;
			            	try {
			            		oXL = new ActiveXObject("Excel.Application");
			            	}catch (e) {
								alert('警告','您所设定的安全级别太高，或者您的电脑没有安装Microsoft Excel软件！');
								return false;
							}
							var oWB = oXL.Workbooks.Add();
							var oSheet = oWB.ActiveSheet;
							var i =0 ,j = 0;
							for (var v in columns ){
								if (!columns[v].hidden){
									oSheet.Cells(i + 1, j + 1).Value = columns[v].text ;
									oSheet.Cells(i + 1, j + 1).Borders.Weight = 2;
									j++;
								}
							}
							i = 1;
							j = 0;
							store.each(function(record){
								for (var v in columns ){
									if (!columns[v].hidden){
										oSheet.Cells(i + 1, j + 1).Value = columns[v]['xtype']=='rownumberer' ? i : record.get(columns[v].dataIndex) ;
										oSheet.Cells(i + 1, j + 1).Borders.Weight = 2;
										j++;
									}
								}
								i++;
								j = 0;
							});
							oXL.Visible = true;
							oXL.UserControl = true;
			                break; 
			            case "cancel": 
			                break; 
			            } 
			        } 
			    });
			    
			    /*
		if (confirm("确认要导出吗?")) {
			var oXL = new ActiveXObject("Excel.Application");
			var oWB = oXL.Workbooks.Add();
			var oSheet = oWB.ActiveSheet;
			var hang = table.rows.length;
			var lie = table.rows(0).cells.length;
			for (i = 0; i < hang; i++)
			{
				for (j = 0; j < lie; j++)
				{
					oSheet.Cells(i + 1, j + 1).Value = table.rows(i).cells(j).innerText;
				}
			}
			oXL.Visible = true;
			oXL.UserControl = true;
		}*/
	},
	ffExpXsl : function(columns,store) {
		var uri = 'data:application/vnd.ms-excel;base64,', 
			template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40">' +
					'<head>' +
					'<!--[if gte mso 9]>' +
					'<xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>' +
					'<x:Name>{worksheet}</x:Name>' +
					'<x:WorksheetOptions>' +
					'<x:DisplayGridlines/>' +
					'</x:WorksheetOptions>' +
					'</x:ExcelWorksheet>' +
					'</x:ExcelWorksheets>' +
					'</x:ExcelWorkbook>' +
					'</xml>' +
					'<![endif]-->' +
					'</head>' +
					'<body>' +
					'<table border=\'1\'>' +
					'{table}' +
					'</table>' +
					'</body>' +
					'</html>', 
			   base64 = function(s) {
						return window.btoa(unescape(encodeURIComponent(s)));
				},
			   format = function(s, c) {
					return s.replace(/{(\w+)}/g, function(m, p) {return c[p];	});
				};
				Ext.Msg.show({ 
			        title:"导出Excel", 
			        msg:"确定要将当前数据导出成Excel?<br/>[yes]->导出全部列数据。<br/>[no]->只导出显示列的数据。<br/>[cancel]->不导出。", 
			        buttons:Ext.Msg.YESNOCANCEL,   
           			icon:Ext.Msg.QUESTION, 
			        fn:function(btn){ 
			            switch(btn){ 
			            case "yes": 
				            var datas = '<tr>';
							for (var v in columns ){
								datas += '<th>' + columns[v].text + '</th>' ;
//								console.log(columns[v]);
							}
							datas += '</tr>';
							store.each(function(record){
								datas += '<tr>';
								for (var v in columns ){
									datas += '<td>' + (columns[v]['xtype']=='rownumberer' ? i : record.get(columns[v].dataIndex)) + '</td>' ;
								}
								datas += '</tr>';
							});
							var ctx = {
										worksheet : 'DATA',
										table : datas
									};
							window.location.href = uri + base64(format(template, ctx));
			                break; 
			            case "no": 
			            var datas = '<tr>';
							for (var v in columns ){
								if (!columns[v].hidden)
									datas += '<th>' + columns[v].text + '</th>' ;
							}
							datas += '</tr>';
							store.each(function(record){
								datas += '<tr>';
								for (var v in columns ){
									if (!columns[v].hidden)
										datas += '<td>' + (columns[v]['xtype']=='rownumberer' ? i : record.get(columns[v].dataIndex))+ '</td>' ;
								}
								datas += '</tr>';
							});
							var ctx = {
										worksheet : 'DATA',
										table : datas
									};
							window.location.href = uri + base64(format(template, ctx));
			                break; 
			            case "cancel": 
			                break; 
			            } 
			        } 
			    });
	}
});