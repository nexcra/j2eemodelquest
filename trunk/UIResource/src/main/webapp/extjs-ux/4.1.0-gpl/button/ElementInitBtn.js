Ext.define('com.ad.button.ElementInitBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			iconCls : 'icon-grid',
			tooltip : '初始化元素',
			text : '初始化',
			disabled : false,
			itemId : 'com_ad_button_ElementInitBtn',
			handler : function() {
				var me = this;
				var dataid = me._grid.getStore().getProxy().extraParams.did;

				if (Ext.Msg.confirm('提醒', '初始化数据将删除现有数据，是否确定 ？', function(btn) {
							if (btn == 'yes') {
								com.ad.ajax({
											params : {
												$actionid : 1102,
												dataid : dataid
											},
											callback : function(input) {
												me._grid.getStore().load();
												if (Ext.isEmpty(input.message)) {

												} else {
													Ext.Msg.alert('警告', input.message);
												}
											}
										});
							}
						}))
					;

			}
		});