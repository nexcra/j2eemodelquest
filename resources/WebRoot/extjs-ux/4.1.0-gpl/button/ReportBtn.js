Ext.define('com.ad.button.ReportBtn', {
			extend : 'Ext.Button',
			iconCls : 'icon-grid',
			tooltip : '分组统计',
			text : '分组统计',

			disabled : false,
			enableToggle : true,
			itemId : 'com_ad_button_ReportBtn',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			// handler : function() {
			//				
			// },
			listeners : {
				toggle : function(button, pressed, eOpts) {
					var me = this;
					var grid = me._grid;
					var store = grid.getStore();
					store.clearGrouping();
					if (pressed) {
						// if (!me.initValidate(grid.query('gridcolumn'),
						// grid.idProperty)) {
						// grid.showMsg('没有找到可用于分组统计的数字型字段!', me.msgType.ERR);
						// return;
						// }
						grid.grouping = true;
						store.remoteGroup = true;
//						store.remoteSort = false;
						grid.preGroup(grid);
//						grid.showMsg('请将需要分组的列锁住!');
					} else {
						grid.grouping = false;
						store.remoteGroup = false;
//						store.remoteSort = true;
//						store.load();
						grid.showMsg(':)');
					}

				}
			}

		});