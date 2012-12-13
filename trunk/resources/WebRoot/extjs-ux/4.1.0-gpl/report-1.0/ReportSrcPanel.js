Ext.define('com.ad.report.ReportSrcPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,
			initComponent : function() {
				var me = this;

				var clmnPanel = Ext.create('com.ad.mq.DefaultEditGridPanel', {
							_dataid : -6001,
							_auth : 4,
							_autoLoad : false,
							title : '元素',
							region : 'east',
							style : 'border-left: 1px solid #8db2e3;',
							split : 1,
							width : '50%',
							disabled : true
						});
				var userAuth = Ext.create('com.ad.report.UserAuthTreePanel', {
							_dataid : -6007,
							title : '用户授权',
							region : 'south',
							style : 'border-right: 1px solid #8db2e3;border-top: 1px solid #8db2e3;',
							split : 1,
							height : '50%',
							disabled : true
						});

				var srcPanel = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : -6000,
							_auth : 15,
							title : '模型',
							style : 'border-right: 1px solid #8db2e3;border-bottom: 1px solid #8db2e3;',
							region : 'center',
							_gridcfg : {
								listeners : {
									selectionchange : function(model, records) {
										var selected = records[0];
										if (selected) {
											var srcid = selected.get('id');
											Ext.apply(clmnPanel.getGrid().getStore().getProxy().extraParams, {
														srcid : srcid
													});
											Ext.apply(userAuth.getStore().getProxy().extraParams, {
														srcid : srcid
													});
											clmnPanel.getGrid().getStore().load();
											clmnPanel.setDisabled(false);
											userAuth.getStore().load();
											userAuth.setDisabled(false);

										}
									},
									afterrender : function(comp, eOpts) {
										var store = comp.getStore();
										store.addListener('load', function(st, records, successful, eOpts) {
													clmnPanel.setDisabled(true);
													userAuth.setDisabled(true);
												});
									}
								}
							}
						});
				me.items = [{
							xtype : 'panel',
							layout : 'border',
							region : 'center',
							border : 0,
							items : [srcPanel, userAuth]
						}, clmnPanel];
				me.callParent();
			}
		});