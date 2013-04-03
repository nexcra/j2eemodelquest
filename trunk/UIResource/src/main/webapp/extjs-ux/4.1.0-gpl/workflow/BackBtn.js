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
				me._window.query('tabpanel')[0].items.each( function(item){
					if(Ext.type(item.beforeBack)=='function'){
						ok =item.beforeBack();
					}
				} );
				if (!ok)return;
//				prompt( String title, String msg, [Function fn], [Object scope], [Boolean/Number multiline], [String value] ) : Ext.window.MessageBox
				Ext.Msg.prompt('提醒', '请填写回退原因:', function(btn ,txt) {
							if (Ext.isEmpty(txt))
							return false;
							if (btn === 'ok') {
								com.ad.ajax({
											params : {
												$actionid : 203,
												did : me._document.id,
												nid : me._document.nid,
												sid : me._document.stepid,
												msg : txt
											},
											callback : function(input) {
												if (Ext.isEmpty(input.message)) {
													me._window.close();
													me._grid.getStore().load();
												}else{
													Ext.Msg.alert('警告',input.message);
												}
												
											}
										});
							}
						}, this ,2);
			}
		});