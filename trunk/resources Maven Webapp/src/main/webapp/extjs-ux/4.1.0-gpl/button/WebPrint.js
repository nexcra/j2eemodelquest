// author:YMQ
// version: 1.0.0
// web打印

Ext.define('com.ad.button.WebPrint', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			iconCls : '',
			tooltip : '打印当前记录',
			text : '打印',
			disabled : false,
			itemId : 'button_WebPrint',
			handler : function() {
				var me = this;
				var store = me._grid.getStore();
				if (!store)
					return;
				var columns = me._grid.columns;

				var datas = '<html>' 
								+ '<head>' 
								+ '<title>WEB打印</title>' 
								+ '<style>* {font-size:10pt;font-family:宋体;}' 
								+ '</style>' 
								+ '</head>' 
								+ '<body>'
								+ '<table style="border-collapse:collapse;" border="1" bordercolor="#000000"><tbody><tr>';
				for (var v in columns) {
					if (!columns[v].hidden)
						datas += '<th>' + columns[v].text + '</th>';
				}
				datas += '</tr>'
				store.each(function(record) {
							datas += '<tr>';
							for (var v in columns) {
								if (!columns[v].hidden)
									datas += '<td>' + record.get(columns[v].dataIndex) + '</td>';
							}
							datas += '</tr>';
						});
				datas += '</tbody></table>' 
							+ '<script type=\'text/javascript\'>(function(){window.print();})();</script>' 
							+ '</body></html>';
				var openWin = window.open('about:blank', 'WEBPRINT', 'height=400,width=600,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no');
				openWin.document.write(datas);
				openWin.focus();
			}
		});