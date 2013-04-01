// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件审批窗口
Ext.define('com.ad.workflow.WorkFlowImg', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			border : 0,
			autoScroll : 1,
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

				me.items = Ext.create('Ext.Img', {
							src : 'images/' + me._document.wfid + '.png',
							autoEl : 'div'
						});
				me.callParent();
			}
		});