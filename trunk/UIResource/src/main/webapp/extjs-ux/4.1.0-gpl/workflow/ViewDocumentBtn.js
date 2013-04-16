// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件办理
Ext.define('com.ad.workflow.ViewDocumentBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '查看详情',
			text : '查看',
			iconCls : 'manage-business',
			disabled : true,
			itemId : 'com_ad_workflow_ViewDocumentBtn',
			handler : function() {
				var me = this;
				var selection = me._grid.getView().getSelectionModel().getSelection()[0];
				if (!selection)
					return;
				Ext.create('com.ad.workflow.ViewDocumentWindow', {
							_document : selection.data,
							_grid : me._grid
						}).show();
			}
		});
