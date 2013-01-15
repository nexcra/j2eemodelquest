// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件办理
Ext.define('com.ad.workflow.CheckBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '办理操作',
			text : '办理',
			iconCls : 'manage-business',
			disabled : true,
			itemId : 'com_ad_workflow_CheckBtn',
			handler : function() {
				var me = this;
				var selection = me._grid.getView().getSelectionModel().getSelection()[0];
				if (!selection)
					return;
				Ext.create('com.ad.workflow.ApproveWindow', {
							_document : selection.data,
							_grid : me._grid,
							modal :false
						}).show();
			}
		});
