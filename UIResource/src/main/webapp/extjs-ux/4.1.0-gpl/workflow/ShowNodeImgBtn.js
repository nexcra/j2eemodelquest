// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 查看流程图
Ext.define('com.ad.workflow.ShowNodeImgBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '查看流程图',
			iconCls : 'chart-business',
			text : '流程图',
			disabled : true,
			itemId : 'com_ad_workflow_ShowNodeImgBtn',
			handler : function() {
				var me = this;
				var selection = me._grid.getView().getSelectionModel().getSelection()[0];
				if (!selection)
					return;
				Ext.create('Ext.window.Window', {
							title : '流程图[' + selection.get('id') + ']',
							autoScroll : true,
							width : 600,
							height : 400,
							layout : 'fit',
							items : Ext.create('Ext.Img', {
										src : 'images/' + selection.get('wfid') + '.png',
										autoScroll : true,
										autoEl : 'div'
									})
						}).show();
			}
		});