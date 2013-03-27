Ext.define('com.ad.report.ReportPlanView', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,

			initComponent : function() {
				var me = this;
				me.autoScroll = true;
				var leftPanel = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : -6006,
							_auth : 0,
							title : '方案列表',
							style : 'border-right: 1px solid #8db2e3;border-bottom: 1px solid #8db2e3;',
							region : 'west',
							collapsible : true,
							animCollapse : true,
							autoScroll : 1,
							width : '20%',
							split:1,
							_gridcfg : {
								listeners : {
									selectionchange : function(model, records) {
										var selected = records[0];
										if (selected) {
											var cfg = selected.get('cfg');
											contentPanel.repaint(Ext.JSON.decode(cfg));
											contentPanel.setTitle(selected.get('name'));
										}
									}
								}
							}

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