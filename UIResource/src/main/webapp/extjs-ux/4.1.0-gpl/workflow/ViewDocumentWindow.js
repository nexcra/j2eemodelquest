// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件审批窗口
Ext.define('com.ad.workflow.ViewDocumentWindow', {
			extend : 'Ext.window.Window',
			height : 600,
			width : 900,
			layout : 'fit',
			modal : true,
			minimizable : true,
			maximizable : true,
			resizable : true,
			closable : true,
			config : {
				_document : null,
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.title = '查看[' + me._document.id + ']';
				me.items = Ext.create('com.ad.workflow.ApproveView', {
							_document : me._document
						})
				me.callParent();
			}
		});