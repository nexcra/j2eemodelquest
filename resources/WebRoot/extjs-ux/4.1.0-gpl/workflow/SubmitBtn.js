// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件提交

Ext.define('com.ad.workflow.SubmitBtn', {
			extend : 'Ext.Button',
			config : {
				_document : null,
				_grid : null,
				_window : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '提交至下一步',
			text : '提交',
			iconCls : 'submit-business',
			disabled : false,
			itemId : 'com_ad_workflow_SubmitBtn',
			handler : function() {
				var me = this;
				if (me._grid) {
					me._document = me._grid.getView().getSelectionModel().getSelection()[0].data;
				}
				Ext.create('com.ad.workflow.TransitionWindow', {
							_document : me._document,
							_aftersubmitFN : function(input) {
								// console.log(input);
								// console.log(me._window);
								// console.log(me._grid);
								if (Ext.isEmpty(input.message)) {
									if (me._window) {
										me._window.close();
									}
									if (me._grid) {
										me._grid.getStore().load();
									}
								} else {
									Ext.Msg.alert('警告', input.message);
								}
							}
						}).show();
			}
		});