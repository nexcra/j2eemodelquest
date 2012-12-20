Ext.define('com.ad.report.ReportDesign', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,

			initComponent : function() {
				var me = this;
				me.autoScroll = true;
				var leftPanel = Ext.create('com.ad.report.ReportDesignLeftPanel', {
							title : '统计分析元素',
							region : 'west',
							collapsible : true,
							animCollapse : true,
							autoScroll : 1,
							style : 'border-right: 1px solid #8db2e3;',
							split : 1,
							width : 250
						});
				var contentPanel = Ext.create('com.ad.report.ReportDesignContentPanel', {
							autoScroll : 1,
							itemId : 'reportDesignContent',
							region : 'center',
							style : 'border-left: 1px solid #8db2e3;'
						});

				me.items = [leftPanel, contentPanel];
				me.callParent();
			}
		});