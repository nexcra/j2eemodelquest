// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件审批窗口
Ext.define('com.ad.workflow.ApproveWindow', {
			extend : 'Ext.window.Window',
			height : 600,
			width : 800,
			layout : 'fit',
			shadow : false,
			modal : true,
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
				me.title = '审批视图[' + me._document.id + ']';
				me.tbar = [Ext.create('com.ad.workflow.SubmitBtn', {
									_document : me._document,
									_window : me,
									_grid : me._grid
								}), Ext.create('com.ad.workflow.BackBtn', {
									_document : me._document,
									_window : me,
									_grid : me._grid,
									disabled : (me._document.nodetype === 0) ? true : false
								}), Ext.create('com.ad.workflow.SuspendBtn', {
									_document : me._document,
									_window : me,
									_grid : me._grid,
									disabled : (me._document.nodetype === 0) ? true : false
								})];
				me.items = Ext.create('com.ad.workflow.ApproveView', {
							_document : me._document
						})
				me.callParent();
			}
		});