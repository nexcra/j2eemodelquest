// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件审批窗口
Ext.define('com.ad.workflow.WorkFlowImg', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			border : 0,
			config : {
				_document : null,
				_view : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.html = '<img src=\'images/' + me._document.wfid + '.png\' alt=\'流程图\'/>';
				me.callParent();
			}
		});