// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 步骤列表
Ext.define('com.ad.workflow.ShowStepsBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '查看流程',
			text : '流程',
			iconCls : 'process-business',
			disabled : true,
			itemId : 'com_ad_workflow_ShowStepsBtn',
			handler : function() {
				var me = this;
				var selection = me._grid.getSelectionModel().getSelection()[0];
				if (!selection)
					return;
				Ext.create('Ext.window.Window', {
							title : '流程[' + selection.get('id') + ']',
							width : 600,
							height : 400,
							layout : 'fit',
							items : Ext.create('com.ad.mq.DefaultGridPanel', {
										_dataid : 203,
										_auth : -1,
										_extraParams : {
											did : selection.get('id')
										}
									})
						}).show();
			}
		});
		
		

