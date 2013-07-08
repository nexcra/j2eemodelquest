// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件回退
Ext.define('com.ad.workflow.BackBtn', {
			extend : 'Ext.Button',
			config : {
				_document : null,
				_window : null,
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '回退到一步',
			text : '回退',
			iconCls : 'back-business',
			disabled : true,
			itemId : 'com_ad_workflow_BackBtn',
			handler : function() {
				var me = this;
				var ok = 1;
				me._window.query('tabpanel')[0].items.each(function(item) {

							var o = item.items.getAt(0);
							if (Ext.type(o.beforeBack) === 'function') {
								ok = o.beforeBack();
							}
							if (!ok)
								return false;

						});
				if (!ok)
					return;
				Ext.Msg.confirm('提醒', '确认要回退当前案件？', function(opt) {
							if (opt == 'yes') {
								com.ad.ajax({
											params : {
												$actionid : 203,
												did : me._document.id,
												nid : me._document.nodeid,
												sid : me._document.stepid
											},
											callback : function(input) {
												if (Ext.isEmpty(input.message)) {
													me._window.close();
													me._grid.doRefresh();
												} else {
													Ext.Msg.alert('警告', input.message);
												}

											}
										});
							}
						});
			}
		});