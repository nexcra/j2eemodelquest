Ext.define('com.ad.report.ReportSrcPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,
			initComponent : function() {
				var me = this;
				var srcPanel = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : -6000,
							_auth : 15,
							title:'模型',
							style : 'border-right: 1px solid #8db2e3;border-bottom: 1px solid #8db2e3;',
							region : 'center'
						});
				var clmnPanel = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : -6001,
							_auth : 15,
							title:'元素',
							region : 'east',
							style : 'border-left: 1px solid #8db2e3;',
							split : 1,
							width : '50%'
						});
				var clmnPanel1 = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : -6001,
							_auth : 15,
							title:'用户授权',
							region : 'south',
							style : 'border-right: 1px solid #8db2e3;border-top: 1px solid #8db2e3;',
							split : 1,
							height : '50%'
						});
				me.items = [{
							xtype : 'panel',
							layout : 'border',
							region : 'center',
							border : 0,
							items : [srcPanel, clmnPanel1]
						}, clmnPanel];
				me.callParent();
			}
		});